package com.havens.siamese.utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by havens on 16-6-24.
 */
public class HttpRequestHelper {
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /*
 * 打印结果{map={"sex":"female","age":"23","name":"Alexia"}, employ={"sex":"female","age":"24","name":"wjl"}}
 */
    public static Map<String, Object> toMap(String jsonString) {
        //JSONObject必须以"{"开头
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Iterator<String> iter = jsonObject.keys();
        String key=null;
        while (iter.hasNext()) {
            key=iter.next();
            resultMap.put(key, jsonObject.get(key));
        }
        return resultMap;
    }

    public static void main(String[] args){
        //发送 GET 请求
//        String result=HttpRequest.sendGet(AppConfig.LOGIN_URL, "platform="+AppConfig.PLATFORM+"&version="+AppConfig.versionCode+"&userName=havenspool@126.com&password=333333");
//       String result=HttpRequest.sendGet("http://192.168.2.135/app/platform_login.php", "userName=test1&password=test1&version=20248");
//        System.out.println(AppConfig.LOGIN_URL+ "platform="+AppConfig.PLATFORM+"&version="+AppConfig.versionCode+"&userName=havenspool@126.com&password=333333");
//        String result=HttpRequest.sendGet("http://hr.timenewgames.com/apptencent/sdkIsOpen.php","");
//        System.out.println(result);
        String result="============= cookie info ================cookies : Array(    [session_id] => openid    [session_type] => kp_actoken    [org_loc] => %2Fmpay%2Fget_balance_m)" +
                "============= request info ================method : geturl    : http://msdktest.qq.com/mpay/get_balance_m?openid=5AAAB9A25F64E10C23D3B42C9D9D819C&openkey=9B844EA9CD3104D9927DA68286841164&pay_token=&ts=1460458959&pf=desktop_m_qq-73213123-android-73213123-qq-1105213087-5AAAB9A25F64E10C23D3B42C9D9D819C&pfkey=14c3644606a9250a1048ba70decc4dc0&zoneid=1&appid=1105213087&format=json&sig=07woFAZNLPYzGfxQ8ZZfLaQXYiw%3Dparams : Array(    [openid] => 5AAAB9A25F64E10C23D3B42C9D9D819C    [openkey] => 9B844EA9CD3104D9927DA68286841164    [pay_token] =>     [ts] => 1460458959    [pf] => desktop_m_qq-73213123-android-73213123-qq-1105213087-5AAAB9A25F64E10C23D3B42C9D9D819C    [pfkey] => 14c3644606a9250a1048ba70decc4dc0    [zoneid] => 1    [appid] => 1105213087    [format] => json    [sig] => 07woFAZNLPYzGfxQ8ZZfLaQXYiw=)\n" +
                "============= respond info ================Array(    [ret] => 0    [balance] => 2821    [gen_balance] => 0    [first_save] => 0    [save_amt] => 2875    [gen_expire] => 0    " +
                "[tss_list] => Array        (        ))" +
                "============= cookie info ================cookies : Array(    [session_id] => openid    [session_type] => kp_actoken    [org_loc] => %2Fmpay%2Fpay_m)" +
                "============= request info ================method : geturl    : http://msdktest.qq.com/mpay/pay_m?openid=5AAAB9A25F64E10C23D3B42C9D9D819C&openkey=9B844EA9CD3104D9927DA68286841164&pay_token=&ts=1460458959&pf=desktop_m_qq-73213123-android-73213123-qq-1105213087-5AAAB9A25F64E10C23D3B42C9D9D819C&pfkey=14c3644606a9250a1048ba70decc4dc0&zoneid=1&amt=68&appid=1105213087&format=json&sig=fkYDGCtK6efpFxR9O55vaZrBbN0%3Dparams : Array(    [openid] => 5AAAB9A25F64E10C23D3B42C9D9D819C    [openkey] => 9B844EA9CD3104D9927DA68286841164    [pay_token] =>     [ts] => 1460458959    [pf] => desktop_m_qq-73213123-android-73213123-qq-1105213087-5AAAB9A25F64E10C23D3B42C9D9D819C    [pfkey] => 14c3644606a9250a1048ba70decc4dc0    [zoneid] => 1    [amt] => 68    [appid] => 1105213087    [format] => json    [sig] => fkYDGCtK6efpFxR9O55vaZrBbN0=)\n" +
                "============= respond info ================Array(    [ret] => 0    [billno] => 783636417    [balance] => 2753    [gen_balance] => 2753    [used_gen_amt] => 0)" +
                "<burstyx>{\"cmd\":\"confirm_payment\",\"result\":true,\"comment\":null,\"orderId\":\"44990196621049856\",\"product_id\":\"10003\",\"billno\":\"783636417\",\"balance\":2753}</burstyx>";
        System.out.println(result);
        result=result.substring(result.indexOf("<burstyx>")+9,result.indexOf("</burstyx>"));
        System.out.println(result);
        Map map=toMap(result);
        String res=map.get("result").toString();
        System.out.println(res);
//        String cmd=map.get("cmd").toString();
//        String userId=map.get("userId").toString();
//        String token=map.get("token").toString();
//        System.out.println(userId+":"+token);
        //发送 POST 请求
//        String sr=HttpRequest.sendPost("http://localhost:6144/Home/RequestPostString", "key=123&v=456");
//        System.out.println(sr);

//        AppEntry.RootDir="C:\\Users\\Administrator\\Desktop\\appzero\\";
//        try {
//            Properties props = new Properties();
//            OutputStream fos = new FileOutputStream(AppEntry.RootDir+AppConfig.LOCAL_USER_PROPERTIES);
//            props.setProperty("userName","h");
//            props.setProperty("password","");
//            props.store(fos, "Update 'userName' value");
//            props.store(fos, "Update 'password' value");
//        } catch (IOException e) {
//            System.err.println("属性文件更新错误");
//        }
//
//        try {
//            Properties props = new Properties();
//            InputStream in = new BufferedInputStream(new FileInputStream(AppEntry.RootDir+AppConfig.LOCAL_USER_PROPERTIES));
//            props.load(in);
//            String userName = props.getProperty("userName");
//            String password = props.getProperty("password");
//            System.err.println(userName);
//            System.err.println(password);
//        } catch (IOException e) {
//            System.err.println("属性文件更新错误");
//        }
    }
}
