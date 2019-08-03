package com.hamster5295.qq_harker_bomber.Bomber.yanchan;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hamster5295.qq_harker_bomber.Bomber.Bomber;
import com.hamster5295.qq_harker_bomber.Utils.SettingUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

/**
 * 半成品，还没有完善，如有错误，欢迎指出
 * 2019-07-29
 */

/**
 * Program by @眼馋QAQ form bilibili.
 */
public class Go {
    public String url, data;//地址，数据
    public Map<String, String> datas;//数据集
    public int threadNumber = 64;//线程数
    public final static int GO_TYPE_GET = 0;//GET请求方式
    public final static int GO_TYPE_POST = 1;//POST请求方式
    public boolean randomData = false;//是否随机账户密码，默认否
    public int requestMethod = 1;//请求方式
    public int responseCode = 0;//返回码
    private boolean isStart = true;//是否继续执行
    private int successNumber = 0;//成功数量
    private int failNumber = 0;//失败数量
    private int dosNumber = 0;//总数量
    private long delayed = -1;//请求间隔
    private Handler handler;

    private Bomber bomber;

    private int rThreadNumber = 0;

    public Go(String url) {
        this(url, null);
    }

    public Go(String url, String data) {
        this(url, data, 64);
    }

    public Go(Bomber bomber, Handler handler, int count) {
        this.bomber = bomber;
        this.handler = handler;
        threadNumber = count;
        isStart = false;
    }

    /**
     * 初始化
     *
     * @param url          链接地址
     * @param data         请求数据集
     * @param threadNumber 线程数量
     */
    public Go(String url, Map<String, String> data, int threadNumber) {
        this.url = url;
        this.datas = data;
        this.threadNumber = threadNumber;
    }

    /**
     * 初始化
     *
     * @param url          链接地址
     * @param data         请求数据
     * @param threadNumber 线程数
     */
    public Go(String url, String data, int threadNumber) {
        this.url = url;
        this.data = data;
        this.threadNumber = threadNumber;
    }

    /**
     * 设置单个线程的请求时间间隔
     *
     * @param time 时间间隔，单位毫秒
     */
    public void setDelayed(long time) {
        delayed = time;
    }

    /**
     * 开始
     */
    public void start(boolean isGet) {
        isStart = true;
        successNumber = 0;
        failNumber = 0;
        rThreadNumber = 0;
        if (!isGet)
            requestMethod = 1;
        else
            requestMethod = 0;

        new Thread(() -> {
            for (int i = 0; i < threadNumber; i++) {
                thread();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rThreadNumber++;
            }
        }).start();
    }

    public void start() {
        isStart = true;
        successNumber = 0;
        failNumber = 0;
        rThreadNumber = 0;

        new Thread(() -> {
            for (int i = 0; i < threadNumber; i++) {
                if (!isStart) return;

                thread();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                rThreadNumber++;
            }
        }).start();
    }

    /**
     * 停止
     */
    public void stop() {
        isStart = false;
        successNumber = 0;
        failNumber = 0;
        rThreadNumber = 0;
    }

    /**
     * 获取成功的请求数量
     */
    public int getSuccessNumber() {
        return successNumber;
    }

    /**
     * 获取失败的请求数量
     */
    public int getFailNumber() {
        return failNumber;
    }

    public int getThreadNumberRealtime() {
        return rThreadNumber;
    }

    private void thread() {
        new Thread(() -> {

            while (isStart) {
                try {
                    playWithBomber();
                } catch (Exception e) {
                    failNumber++;
//                        e.printStackTrace();
                    if (e instanceof SocketTimeoutException) {
                        Log.e("CheaterBomber:", "链接超时");
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        bundle.putInt("code", 1000);
                        msg.setData(bundle);
                        msg.what = 3;
                        handler.sendMessage(msg);
                    } else if (e instanceof MalformedURLException) {
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("data", data);
                        bundle.putInt("code", 1001);
                        msg.setData(bundle);
                        msg.what = 3;
                        handler.sendMessage(msg);
                    } else
                        e.printStackTrace();
                }
                dosNumber++;

                try {
                    if (delayed > 0) {
                        Thread.sleep(delayed);
                    } else {
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void play() throws IOException {
        URL u = new URL(url);

        HttpURLConnection connection;
        if (requestMethod == 1) {
            connection = (HttpURLConnection) u.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
        } else {
            String name = datas.get("name");
            String pass = datas.get("pass");
            String prefix = datas.get("prefix");
            data = "{\"" + name + "\":\"" + getRandom(false, false) + "\",\"" + pass + "\":\"" + getRandom(true, false) + "\"" + "}";

            if (SettingUtil.getSettingBoolean("setting_base64")) {
                u = new URL(url + "?" + prefix + "=" + android.util.Base64.encodeToString(data.getBytes(), android.util.Base64.DEFAULT));

            } else {
                u = new URL(url + "?" + prefix + "=" + data);
            }

            connection = (HttpURLConnection) u.openConnection();
            //connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        if (requestMethod == 1) {
            if (randomData && datas != null && !datas.isEmpty()) {
                String name = datas.get("name") + "=" + getRandom(false, true);
                String pass = datas.get("pass") + "=" + getRandom(true, true);

                data = name + "&" + pass + "&" + datas.get("data");
            }
            if (data != null) {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(data.getBytes("UTF-8"));
                out.flush();
                out.close();
            }
        }
        responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            sb.append(connection.getResponseCode() + " ");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }

            successNumber++;
            reader.close();
            System.out.println(sb.toString());
        }
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        bundle.putInt("code", responseCode);
        msg.setData(bundle);
        msg.what = 4;
        handler.sendMessage(msg);

        connection.disconnect();

    }

    private void playWithBomber() throws IOException {
        URL u = new URL(bomber.getUrl());
        HttpURLConnection connection;

        String qwp = "";

        String name = bomber.getUserKey();
        String pass = bomber.getPasswordKey();
        String extra = bomber.getExtra();
        String prefix = bomber.getPrefix();

        if (!bomber.getUseGet()) {
            connection = (HttpURLConnection) u.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            name = name + "=" + getRandom(false, true, bomber);
            pass = pass + "=" + getRandom(true, true, bomber);
            qwp = (bomber.getUsePrefix() ? prefix + "&" : "") + name + "&" + pass + (bomber.getUseExtra() ? "&" + extra : "");

        } else {
            qwp = "{\"" + name + "\":\"" + getRandom(false, false, bomber) + "\",\"" + pass + "\":\"" + getRandom(true, false, bomber) + "\"" + "}" + (bomber.getUsePrefix() ? extra : "");

            if (SettingUtil.getSettingBoolean("setting_base64")) {
                u = new URL(url + "?" + (bomber.getUsePrefix() ? prefix + "=" : "") + android.util.Base64.encodeToString(qwp.getBytes(), android.util.Base64.DEFAULT));

            } else {
                u = new URL(url + "?" + (bomber.getUsePrefix() ? prefix + "=" : "") + qwp);
            }

            connection = (HttpURLConnection) u.openConnection();
            //connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        if (bomber.getUseGet()) {

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(data.getBytes("UTF-8"));
            out.flush();
            out.close();
        }

        responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer();
            sb.append(connection.getResponseCode() + " ");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            reader.close();
            System.out.println(sb.toString());
        }

        if ((responseCode + "").startsWith("2") || (responseCode + "").startsWith("3")) {
            successNumber++;
        } else if ((responseCode + "").startsWith("4")) {
            failNumber++;
        }

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data", qwp);
        bundle.putInt("code", responseCode);
        msg.setData(bundle);
        msg.what = 4;
        handler.sendMessage(msg);

        connection.disconnect();

    }

    /**
     * 设置是否随机账户密码，
     *
     * @param randomData 是或否
     */
    public void setRandomData(boolean randomData) {
        this.randomData = randomData;
    }

    public String getRandom(boolean is16, boolean isBase64) {
        Random random = new Random();
        int id = random.nextInt(999999) + 111111;//随机六位数


        if (!SettingUtil.getSettingBoolean("setting_base64") || !isBase64) {
            return is16 ? Integer.toHexString(id) : id + "";
        } else {
            //When SDK version is above 26,use class Base64 at java.util to make it faster
            if (Build.VERSION.SDK_INT >= 26) {
                return Base64.getEncoder().encodeToString(is16 ? Integer.toHexString(id).getBytes() : Integer.toString(id).getBytes());
            } else {
                return android.util.Base64.encodeToString(is16 ? Integer.toHexString(id).getBytes() : Integer.toString(id).getBytes(), android.util.Base64.DEFAULT);
            }
        }
    }

    public String getRandom(boolean is16, boolean isBase64, Bomber bomber) {

        int id = (int) Math.round(Math.random() * 1000000000);


        if (!bomber.getUseBase64() || !isBase64) {
            return is16 ? Integer.toHexString(id) : id + "";
        } else {
            //When SDK version is above 26,use class Base64 at java.util to make it faster
            if (Build.VERSION.SDK_INT >= 26) {
                return Base64.getEncoder().encodeToString(is16 ? Integer.toHexString(id).getBytes() : Integer.toString(id).getBytes());
            } else {
                return android.util.Base64.encodeToString(is16 ? Integer.toHexString(id).getBytes() : Integer.toString(id).getBytes(), android.util.Base64.DEFAULT);
            }
        }
    }

    //下面的注释我就不写了(偷懒)

    public static String doPost(String urls, String data) throws Exception {
        URL url = new URL(urls);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //connection.setRequestProperty("Content-type","application/json; charset=UTF-8");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(data.getBytes("UTF-8"));
        out.flush();
        out.close();
        StringBuffer sb = new StringBuffer();
        //if((Code=connection.getResponseCode())!=401){
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        //StringBuffer sb = new StringBuffer("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }

    public static String doPost(String urls) throws Exception {
        URL url = new URL(urls);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        //connection.setConnectTimeout(5000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        //connection.setRequestProperty("User-Agent","Mozilla/5.0 (Linux; Android 5.1; OPPO R9tm Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043128 Safari/537.36 V1_AND_SQ_7.0.0_676_YYB_D PA QQ/7.0.0.3135 NetType/4G WebP/0.3.0 Pixel/1080");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }

    public static String doGet(String urls) throws Exception {
        URL url = new URL(urls);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        //connection.setDoInput(true);
        //connection.setDoOutput(true);
        //connection.setUseCaches(false);
        connection.setRequestProperty("Cookie", "PHPSESSID=o1fg44f7bgiulh2lr5l8fqel87");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 5.1; OPPO R9tm Build/LMY47I; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/53.0.2785.49 Mobile MQQBrowser/6.2 TBS/043128 Safari/537.36 V1_AND_SQ_7.0.0_676_YYB_D PA QQ/7.0.0.3135 NetType/4G WebP/0.3.0 Pixel/1080");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuffer sb = new StringBuffer();
        while ((lines = reader.readLine()) != null) {
            sb.append(lines);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }

    public boolean isRunning() {
        return isStart;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
