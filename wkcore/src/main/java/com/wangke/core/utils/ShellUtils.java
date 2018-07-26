package com.wangke.core.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/7
 *     desc  : Shell相关工具类
 * </pre>
 */
public class ShellUtils {

    private final static String TAG = ShellUtils.class.getSimpleName();

    private ShellUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 是否是在root下执行命令
     *
     * @param command 命令
     * @param isRoot  是否需要root权限执行
     * @return CommandResult
     */
    public static CommandResult execCmd(String command, boolean isRoot) {
        return execCmd(new String[]{command}, isRoot, true);
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands 多条命令链表
     * @param isRoot   是否需要root权限执行
     * @return CommandResult
     */
    public static CommandResult execCmd(List<String> commands, boolean isRoot) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, true);
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands 多条命令数组
     * @param isRoot   是否需要root权限执行
     * @return CommandResult
     */
    public static CommandResult execCmd(String[] commands, boolean isRoot) {
        return execCmd(commands, isRoot, true);
    }

    /**
     * 是否是在root下执行命令
     *
     * @param command         命令
     * @param isRoot          是否需要root权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    public static CommandResult execCmd(String command, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRoot, isNeedResultMsg);
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands        命令链表
     * @param isRoot          是否需要root权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    public static CommandResult execCmd(List<String> commands, boolean isRoot, boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRoot, isNeedResultMsg);
    }

    /**
     * 是否是在root下执行命令
     *
     * @param commands        命令数组
     * @param isRoot          是否需要root权限执行
     * @param isNeedResultMsg 是否需要结果消息
     * @return CommandResult
     */
    public static CommandResult execCmd(String[] commands, boolean isRoot, boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, null, null);
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? "su" : "sh");
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes("\n");
                os.flush();
            }
            os.writeBytes("exit\n");
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
                errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
                String s;
                while ((s = successResult.readLine()) != null) {
                    successMsg.append(s);
                }
                while ((s = errorResult.readLine()) != null) {
                    errorMsg.append(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(os, successResult, errorResult);
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(
                result,
                successMsg == null ? null : successMsg.toString(),
                errorMsg == null ? null : errorMsg.toString()
        );
    }

    /**
     * 返回的命令结果
     */
    public static class CommandResult {
        /**
         * 结果码
         **/
        public int    result;
        /**
         * 成功信息
         **/
        public String successMsg;
        /**
         * 错误信息
         **/
        public String errorMsg;

        public CommandResult(int result, String successMsg, String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }
    }

    public static boolean pingIpAddress(String ipAddress) {
        try {
            ipAddress = ipAddress.replaceAll(" ","");
            if(TextUtils.isEmpty(ipAddress)){
                return false;
            }
            LogUtil.d(TAG, "ping cmd:" + "/system/bin/ping -c 1 -w 10 " + ipAddress);
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 10 " + ipAddress);
            StringBuffer stringBuffer = new StringBuffer();

            int status = process.waitFor();
            InputStream input;
            if (status == 0) {
                input = process.getInputStream();
                BufferedReader buf = new BufferedReader(new InputStreamReader(input));
                String str = new String();
                // 读出所有信息并显示
                while ((str = buf.readLine()) != null) {
                    LogUtil.d(TAG, "ping stream:" + str);
                    stringBuffer.append(str);
                }
                if(input != null){
                    input.close();
                }
                if(buf != null){
                    buf.close();
                }
                LogUtil.d(TAG, "ping stream:"+stringBuffer.toString());
                return true;
            } else {
                input = process.getErrorStream();
                BufferedReader buf = new BufferedReader(new InputStreamReader(input));
                String str = new String();
                // 读出所有信息并显示
                while ((str = buf.readLine()) != null) {
                    LogUtil.d(TAG, "ping error stream :" + str);
                }
                if(input != null){
                    input.close();
                }
                if(buf != null){
                    buf.close();
                }
                LogUtil.d(TAG, "ping 无响应 ");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getLocalDNS() {
        Process cmdProcess = null;
        String dnsIP = "";
        try {
            LogUtil.d(TAG, "get local dns cmd : " + "getprop net.dns1");
            cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
            InputStreamReader ir = new InputStreamReader(cmdProcess.getInputStream());
            BufferedReader input = new BufferedReader(ir);
            String line;
            while ((line = input.readLine()) != null) {
                if (!TextUtils.isEmpty(line)) {
                    LogUtil.d(TAG, "get local dns: " + line);
                    dnsIP = line;
                }
            }
            input.close();

            //读取标准错误流
            input = new BufferedReader(new InputStreamReader(cmdProcess.getErrorStream()));
            while ((line = input.readLine()) != null) {
                LogUtil.d(TAG, "get local dns error: " + line);
            }
            input.close();

            cmdProcess.waitFor();
            return dnsIP;
        } catch (IOException e) {
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(cmdProcess != null){
                cmdProcess.destroy();
            }
        }
    }
}