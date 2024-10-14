package com.push.service.refresh;

import java.util.Date;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class RefreshService {
    //这里是单机锁  分布式环境会使用Redis分布式锁
    static final ReentrantLock lock = new ReentrantLock();
    //这是是全局变量 分布式环境会使用redis里面的数据
    static volatile Long lastRefreshTime = new Date().getTime();
    static Object originToken = new Object();

    public static void request() throws InterruptedException {
        Long requestTime = new Date().getTime();
        try {
            mockRequest();
        } catch (RuntimeException e) {
            System.out.println(Thread.currentThread().getName() + "请求异常开始刷新token: " + requestTime);
            boolean result = refreshOriginToken(requestTime);
            System.out.println(Thread.currentThread().getName() + "刷新token结果: " + result);
        }
    }

    private static boolean refreshOriginToken(Long requestTime) throws InterruptedException {
        int refreshNum = 0;
        while (refreshNum < 3) {
            if (lock.tryLock(200, TimeUnit.MILLISECONDS)) {
                try {
                    String.format("%s:抢占到锁 开始刷新originToken requestTime: %s lastRefresh: %s", Thread.currentThread().getName());
                    //todo 防止锁被提前释放
                    if (requestTime < lastRefreshTime) {
                        System.out.println(Thread.currentThread().getName() + "已经被其他线程刷新过 跳过刷新 直接返回成功");
                        return true;
                    }

                    //正式开始刷新
                    doRefreshOriginToken();
                    System.out.println(Thread.currentThread().getName() + "刷新originToken成功" + new Date().getTime());
                    return true;
                } finally {
                    lock.unlock();
                }
            }
            //再对比一下时间是否其他线程已经刷新过了 刷新过直接返回 否则继续重试 避免多次加锁
            if (requestTime < lastRefreshTime) {
                System.out.println(Thread.currentThread().getName() + "已经被其他线程刷新过 跳过刷新 直接返回成功");
                return true;
            }
            refreshNum++;
            System.out.println(Thread.currentThread().getName() + "抢占锁失败 第" + " 已经尝试刷新第"+ refreshNum + "次重试");
        }
        //三次自旋还是没有刷新成功 只能返回false
        return false;
    }

    /**
     * 顺序不能变
     * 1.请求
     * 2.更新originToken
     * 3.跟新lastRefreshTime
     */
    private static void doRefreshOriginToken() {
        //模拟刷新originToken
        try {
            System.out.println(Thread.currentThread().getName() + "模拟请求睡眠200ms");
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        originToken = new Object();
        lastRefreshTime = new Date().getTime();
    }

    private static void mockRequest() {
        throw new RuntimeException("请求失败 refreshToken已过期");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(() -> {
                try {
                    request();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        }
    }

}
