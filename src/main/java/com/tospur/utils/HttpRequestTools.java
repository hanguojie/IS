package com.tospur.utils;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestTools {
    /**
     * 发送http请求
     *
     * @param requestUrl 请求地址
     * @return String
     */
    public static String httpRequest(String requestUrl) {
        return httpRequest(requestUrl, "utf-8");
    }

    /**
     * 发送http请求
     *
     * @param requestUrl  请求地址
     * @param charsetName 编码
     * @return String
     */
    public static String httpRequest(String requestUrl, String charsetName) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url
                    .openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, charsetName);
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}