package com.push.common.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Title: RemoteExecuteCommand.class
 * Description:
 * <p>  连接远程linux服务器，执行指令
 * Create DateTime: 2018/6/11 17:03
 *
 * 

 */
public class RemoteExecuteCommand {

    public static String DEFAULT_CHARSET = "UTF-8";
    private static Connection conn;
    private String ip;
    private String userName;
    private String pem;

    public RemoteExecuteCommand(String ip, String userName, String pem) {
        this.ip = ip;
        this.userName = userName;
        this.pem = pem;
    }

    public RemoteExecuteCommand() {
    }

    public Boolean login() {
        boolean flag = false;
        try {
            conn = new Connection(ip);
            conn.connect();
            flag = conn.authenticateWithPublicKey(userName, pem.toCharArray(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    public String execute(String cmd) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULT_CHARSET);
                //如果为得到标准输出为空，说明脚本执行出错了
                if (StringUtils.isBlank(result)) {
                    result = processStdout(session.getStderr(), DEFAULT_CHARSET);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String executeSuccess(String cmd) {
        String result = "";
        try {
            if (login()) {
                Session session = conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                result = processStdout(session.getStdout(), DEFAULT_CHARSET);
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String processStdout(InputStream in, String charset) {
        InputStream stdout = new StreamGobbler(in);
        StringBuilder buffer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

}
