package yanchan;

import java.net.*;
import java.io.*;
import java.util.*;

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
    public int RequestMethod = 1;//请求方式
    public int responseCode = 0;//返回码
    private boolean isStart = true;//是否继续执行
    private int successNumber = 0;//成功数量
    private int failNumber = 0;//失败数量
    private int dosNumber = 0;//总数量
    private long delayed = -1;//请求间隔

    public Go(String url) {
        this(url, null);
    }

    public Go(String url, String data) {
        this(url, data, 64);
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
    public void start() {
        isStart = true;
        successNumber = 0;
        failNumber = 0;
        for (int i = 0; i < threadNumber; i++) {
            thread();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        isStart = false;
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

    private void thread() {
        new Thread() {
            @Override
            public void run() {
                while (isStart) {
                    try {
                        play();
                        successNumber++;
                        if (delayed > 0) {
                            Thread.sleep(delayed);
                        } else {
                            Thread.sleep(100);
                        }
                    } catch (Exception e) {
                        failNumber++;
                        e.printStackTrace();
                    }
                    dosNumber++;
                }
            }
        }.start();
    }

    private void play() throws Exception {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        if (RequestMethod == 1) {
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
        } else {
            connection.setRequestMethod("GET");
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        if (randomData && datas != null && !datas.isEmpty()) {
            Random random = new Random();
            int id = random.nextInt(999999) + 111111;//随机六位数
            String name = datas.get("name") + id;
            String pass = datas.get("pass") + id;
            data = name + "&" + pass + "&" + datas.get("data");
        }
        if (data != null) {
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
}
