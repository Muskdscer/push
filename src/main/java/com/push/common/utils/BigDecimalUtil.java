package com.push.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Title: BigDecimalUtil.class
 * Description: 数据计算util
 * Create DateTime: 2018/7/25 13:43
 *
 * 

 */
public class BigDecimalUtil {


    /**
     * 基本单位
     */
    private static final BigDecimal UNIT = new BigDecimal(100000000);

    /**
     * 对double数据进行取精度.
     * @param value  double数据.
     * @param scale  精度位数(保留的小数位数).
     * @param roundingMode  精度取值方式.
     * @return 精度计算后的数据.
     */
    public static double round(double value, int scale,
                               int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }


    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(Double d1,Double d2){
        BigDecimal bd1 = new BigDecimal(d1.toString());
        BigDecimal bd2 = new BigDecimal(d2.toString());
        return bd1.add(bd2).doubleValue();
    }

    public static double sum(String a,String b){
        BigDecimal pa = new BigDecimal(a);
        BigDecimal pb = new BigDecimal(b);
        return pa.add(pb).doubleValue();
    }


    /**
     * double 相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double 乘法
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * double 除法
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    public static double div(double d1,double d2,int scale){
        //  当然在此之前，你要判断分母是否为0，
        //  为0你可以根据实际需求做相应的处理

        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide
                (bd2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 长整数相乘
     * @param pa
     * @param pb
     * @return
     */
    public static long longMul(long pa,long pb){
        BigDecimal b1 = new BigDecimal(pa);
        BigDecimal b2 = new BigDecimal(pb);
        return b1.multiply(b2).longValue();
    }

    public static long longMul(long pa ,Double rate){
        BigDecimal b1 = new BigDecimal(pa);
        BigDecimal b2 = new BigDecimal(rate.toString());
        b2 = b2.setScale(6,BigDecimal.ROUND_HALF_UP);
        return b1.multiply(b2).longValue();
    }

    public static long longMul(long pa ,String rate){
        BigDecimal b1 = new BigDecimal(pa);
        BigDecimal b2 = new BigDecimal(rate);
        return b1.multiply(b2).longValue();
    }

    /**
     * 长整数相减
     * @param pa
     * @param pb
     * @return
     */
    public static long longSub(long pa,long pb){
        BigDecimal b1 = new BigDecimal(pa);
        BigDecimal b2 = new BigDecimal(pb);
        return b1.subtract(b2).longValue();
    }

    /**
     * 长整数相加
     * @param pa
     * @param pb
     * @return
     */
    public static long longAdd(long pa,long pb){
        BigDecimal b1 = new BigDecimal(pa);
        BigDecimal b2 = new BigDecimal(pb);
        return b1.add(b2).longValue();
    }

    /**
     * 入参转化
     * @param value
     * @return
     */
    public static long inputConvert(Double value){
       BigDecimal input =  new BigDecimal(value.toString());
       return input.multiply(UNIT).longValue();
    }

    /**
     * 输出转化
     * @param value
     * @return
     */
    public static double outputConvert(long value){
        BigDecimal output = new BigDecimal(value);
        return output.divide(UNIT,6, RoundingMode.DOWN).doubleValue();
    }

    /**
     * 判断source是否大于target
     */
    public static Boolean isMax(BigDecimal source, BigDecimal target){
        return source.compareTo(target) > 0;
    }

    /**
     * 判断source是否等于target
     */
    public static Boolean isEqual(BigDecimal source, BigDecimal target){
        return source.compareTo(target) == 0;
    }

    /**
     * 判断source是否小于target
     */
    public static Boolean isMin(BigDecimal source, BigDecimal target){
        return source.compareTo(target) < 0;
    }

    public static Boolean isMinOrEqual(BigDecimal source, BigDecimal target){
        return !isMax(source, target);
    }

    public static Boolean isMaxOrEqual(BigDecimal source, BigDecimal target){
        return !isMin(source, target);
    }
}
