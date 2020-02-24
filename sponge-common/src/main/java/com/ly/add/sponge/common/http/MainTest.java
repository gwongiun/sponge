package com.ly.add.sponge.common.http;

import com.alibaba.fastjson.JSONObject;
import com.ly.add.sponge.common.utils.MessageDigestUtil;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author : qqy48861
 * date : 2019/4/1.
 */
public class MainTest {

//    @Test
//    public void testPostJson() {
//        String url = "https://api.e.qq.com/v1.0/user_actions/add?access_token=f7638fdffa4e3062df9cea0872383a06&timestamp=1555569470&nonce=074b3d8003994ae898e76c9078fc1ce1";
//        String paramString = "{\"account_id\":\"6643092\",\"actions\":[{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0W3yiHXBvWTdJEqNKJSyNOE\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"2019-04-20\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"92788383\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-04-19\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0bY6vrcogF-5sM5mUI6yoso\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"SEARCH\",\"action_param\":{\"end_date\":\"2019-04-22\",\"preferred_commercial_area\":[],\"content_type\":\"hotel\",\"preferred_star_ratings\":[],\"preferred_price_range\":[],\"product_ids_list\":[\"0\"],\"destination\":\"广州\",\"region_type\":\"国内\",\"object\":\"search_hotel_list\",\"start_date\":\"2019-04-19\"}},{\"action_time\":1555568520,\"user_id\":{\"wechat_openid\":\"o498X0UWO6HzS0rdH-iRbBMODVDA\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"Fri Apr 19 2019 00:00:00 GMT+0800 (CST)\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"90184930\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-04-18\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0WZF3vFtQoxcOqzwHN31EI8\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"2019-04-19\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"01410016\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-04-18\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0c-ou2t8SOcBIbIHTdd8ezg\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"2019-05-06\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"30101036\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-05-02\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0WfWS9VgdF3hpnx3DG2pllY\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"2019-04-20\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"01707060\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-04-19\"}},{\"action_time\":1555568522,\"user_id\":{\"wechat_openid\":\"o498X0TVXwupY7Y3maDoirsUcnR4\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"SEARCH\",\"action_param\":{\"end_date\":\"2019-04-19\",\"preferred_commercial_area\":[],\"content_type\":\"hotel\",\"preferred_star_ratings\":[],\"preferred_price_range\":[],\"product_ids_list\":[\"0\"],\"destination\":\"当涂县\",\"region_type\":\"国内\",\"object\":\"search_hotel_list\",\"start_date\":\"2019-04-18\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0S89Xig1IiRjCOdHvpaWYXI\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"VIEW_CONTENT\",\"action_param\":{\"end_date\":\"2019-05-10\",\"stars_level\":0,\"content_type\":\"hotel\",\"product_id\":\"51104007\",\"region_type\":\"国内\",\"object\":\"hotel\",\"start_date\":\"2019-05-08\"}},{\"action_time\":1555568521,\"user_id\":{\"wechat_openid\":\"o498X0dLdl-8RgUVK8x3UFHiRhrg\",\"wechat_app_id\":\"wx336dcaf6a1ecf632\"},\"action_type\":\"SEARCH\",\"action_param\":{\"end_date\":\"2019-04-19\",\"preferred_commercial_area\":[],\"content_type\":\"hotel\",\"preferred_star_ratings\":[],\"preferred_price_range\":[],\"product_ids_list\":[\"0\"],\"destination\":\"南宁\",\"region_type\":\"国内\",\"object\":\"search_hotel_list\",\"start_date\":\"2019-04-18\"}}],\"user_action_set_id\":\"1106942302\"}";
//        long l1 = System.currentTimeMillis();
//        HashMap<HttpRoute, Integer> route = new HashMap<>();
//        route.put(new HttpRoute(new HttpHost("api.e.qq.com", 443)), 500);
//        HttpReport post = HttpGatling.excute(
//                HttpBullet.custom()
//                        .url(url)
//                        .param(paramString)
//                        .method(HttpMethods.POST)
//                        .entity(HttpEntitys.JSON)
////                        .failed((result) -> {
////                            if (!"0".equals(result.getResJson().getString("code"))) {
////                                LogFactory.start()
////                                        .withMarker("重点监控", "动态创意")
////                                        .withMessage(result.getResJson().toJSONString())
////                                        .withExtraInfo("请求参数", paramString)
////                                        .withExtraInfo("请求链接", url)
////                                        .withExtraInfo("result", JSON.toJSONString(result))
////                                        .error();
////                            }
////                        })
////                        .client(HttpGun.create().pool(1000, 100, route).retry(1).timeout(10000).build())
//        );
//        System.out.println(post.toString());
//        long l3 = System.currentTimeMillis();
//        System.out.println("打印post:" + (l3 - l1));
//    }

    @Test
    public void testGet() {
        long l1 = System.currentTimeMillis();
        HashMap<HttpRoute, Integer> route = new HashMap<>();
        route.put(new HttpRoute(new HttpHost("mkcloud.17usoft.com", 80)), 500);
        String url = "http://mkcloud.17usoft.com/membermain/cancel/cancelRecord";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("unionId", "ohmdTtz5D9jDOGF9YgIZ6xBB_rqg");
        paramMap.put("requestIp", "qqy48861.OpenAPI.1080");
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("appKey", "bigdataapp.java.tcodp");
        headers[1] = new BasicHeader("appSecret", "a23ecdb7-d700-46da-98f5-43827581b244");

        HttpReport excute = HttpGatling.shoot(
                HttpBullet.custom()
                        .url(url)
                        .param(paramMap)
                        .headers(headers)
                        .method(HttpMethods.GET)
//                        .client(HttpGun.create().pool(1000, 100, route).retry(1).timeout(10000).build())
        );
        System.out.println(excute.getResJson());
        System.out.println(excute.toString());
        long l3 = System.currentTimeMillis();
        System.out.println("打印post:" + (l3 - l1));
    }

    @Test
    public void testGetTest() {
        long l1 = System.currentTimeMillis();
        HashMap<HttpRoute, Integer> map = new HashMap<>();
        map.put(new HttpRoute(new HttpHost("mkcloud.17usoft.com", 80)), 500);
        String url = "http://quanqiyun123.com/";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("unionId", "ohmdTtz5D9jDOGF9YgIZ6xBB_rqg");
        paramMap.put("requestIp", "qqy48861.OpenAPI.1080");
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("appKey", "bigdataapp.java.tcodp");
        headers[1] = new BasicHeader("appSecret", "a23ecdb7-d700-46da-98f5-43827581b244");

        HttpReport hr = HttpGatling.shoot(
                HttpBullet.custom()
                        .url(url)
                        .param(paramMap)
                        .headers(headers)
                        .method(HttpMethods.POST)
                        .retry((result, times) -> {
                            if (times <= 3 && !(result.getSignSuccess() && result.getResJson().getString("code").equals("0"))) {
                                return true;
                            }
                            return false;
                        })
                        .disaster(result -> {

                        })
                        .client(HttpGun.create().pool(1000, 100, map).retry(1).timeout(10000).build())
        );

        System.out.println(hr.getResJson());
        System.out.println(hr.toJSONObject());
        long l3 = System.currentTimeMillis();
        System.out.println("打印post:" + (l3 - l1));
    }

    @Test
    public void testPostForm() {
        long l1 = System.currentTimeMillis();
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("click_id", "");
        paramMap.put("muid", "ba74532d002810f673f334729a20f470");
        paramMap.put("appid", 100884090);
        paramMap.put("conv_time", 1554801300);
        paramMap.put("client_ip", "223.90.180.254");
        paramMap.put("encstr", "459c90d2381aec410ea61c75ecc36830");
        paramMap.put("encver", "1.0");
        paramMap.put("advertiser_id", 7656906);
        paramMap.put("app_type", "ANDROID");
        paramMap.put("conv_type", "MOBILEAPP_ACTIVITE");
        String url = "https://t.gdt.qq.com/conv/app/100884090/conv";
        HttpReport excute = HttpGatling.shoot(
                HttpBullet.custom()
                        .url(url)
                        .param(paramMap)
//                        .headers(headers)
                        .method(HttpMethods.POST)
                        .entity(HttpEntitys.STRING)
        );
        System.out.println(excute.toString());
        long l3 = System.currentTimeMillis();
        System.out.println("打印post:" + (l3 - l1));
    }

    @Test
    public void testPostFile() {
        long l1 = System.currentTimeMillis();
    }

    @Test
    public void testGetFailed() {
        long l1 = System.currentTimeMillis();

        HttpReport shibaiceshi = shibaiceshi(1);
        System.out.println(shibaiceshi.toString());
        long l3 = System.currentTimeMillis();
        System.out.println("打印post:" + (l3 - l1));
    }

    private static HttpReport shibaiceshi(Integer times) {
        HashMap<HttpRoute, Integer> route = new HashMap<>();
        route.put(new HttpRoute(new HttpHost("mkcloud.17usoft.com", 80)), 500);
        String url = "http://localhost/addap/qihu?refid=16019558&aaid=25&os=AND&clickid=__UniqueID__&clicktime=__clicktime__&ip=__IP__&ostype=__OS__&devicetype=__devicetype__&imei_md5=__imei_md5__&idfa=__IDFA__&mac_md5=__MAC_MD5__&callback=__callback_url__";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("unionId", "ohmdTtz5D9jDOGF9YgIZ6xBB_rqg");
        paramMap.put("requestIp", "qqy48861.OpenAPI.1080");
        Header[] headers = new Header[2];
        headers[0] = new BasicHeader("appKey", "bigdataapp.java.tcodp");
        headers[1] = new BasicHeader("appSecret", "a23ecdb7-d700-46da-98f5-43827581b244");
        AtomicReference<HttpReport> sy = new AtomicReference<>();
        HttpReport excute = HttpGatling.shoot(
                HttpBullet.custom()
                        .url(url)
//                        .param(paramMap)
                        .headers(headers)
                        .method(HttpMethods.GET)
                        .failed((result) -> {
                            if (!result.getSignSuccess() && times <= 3) {
                                Integer t = times;
                                t++;
                                HttpReport shibaiceshi = shibaiceshi(t);
                                sy.set(shibaiceshi);
                            }
                        })
//                        .client(HttpGun.create().pool(1000, 100, route).retry(1).timeout(10000).build())
        );
        if (sy.get() != null) {
            return sy.get();
        }
        return excute;
    }

    @Test
    public void crawlSalm() {
        long l = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                HttpReport hr = HttpGatling.shoot(HttpBullet.custom().url("http://tctj.t.ly.com/salm/entrance?cid=408&deviceId=FDEA4B08-53C5-44CE-83B0-8E3A7405D47E&userId=34847382"));
                System.out.println(hr);
            });
        }
        executorService.shutdown();
        System.out.println(System.currentTimeMillis() - l);
    }

    @Test
    public void executorCrawl() {
        long l = System.currentTimeMillis();
        final CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 1; i < 1000; i++) {
//            Sleep task = new Sleep(i,latch);
            int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(finalI);
                        Thread.sleep(finalI *1000);
                        latch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("over");
        executorService.shutdown();
//        executorService.shutdown();
//        while (executorService.isTerminated()){
//            System.out.println(System.currentTimeMillis() - l);
//        }
    }

//    class Sleep implements Runnable {
//        private int taskNum;
//        private CountDownLatch latch;
//
//        public Sleep(int taskNum,CountDownLatch latch) {
//            this.taskNum = taskNum;
//            this.latch = latch;
//        }
//
//        @Override
//        public void run() {
//            try {
//                System.out.println(taskNum);
//                Thread.sleep(taskNum*1000);
//                latch.countDown();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Test
    public void runnableCrawl() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        final CountDownLatch cdOrder = new CountDownLatch(1);//指挥官的命令，设置为1，指挥官一下达命令，则cutDown,变为0，战士们执行任务
        final CountDownLatch cdAnswer = new CountDownLatch(3);//因为有三个战士，所以初始值为3，

        for (int i = 0; i < 3; i++) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        System.out.println("线程" + Thread.currentThread().getName() +
                                "正准备接受命令");
                        cdOrder.await(); //战士们都处于等待命令状态
                        System.out.println("线程" + Thread.currentThread().getName() +
                                "已接受命令");
                        HttpReport hr = HttpGatling.shoot(HttpBullet.custom().url("http://tctj.t.ly.com/salm/entrance?cid=408&deviceId=FDEA4B08-53C5-44CE-83B0-8E3A7405D47E&userId=34847382"));
                        System.out.println(hr);
                        System.out.println("线程" + Thread.currentThread().getName() +
                                "回应命令处理结果");
                        cdAnswer.countDown(); //任务执行完毕，返回给指挥官，cdAnswer减1。
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            };
            executorService.execute(runnable);//为线程池添加任务
        }

        try {
            Thread.sleep((long) (Math.random() * 10000));

            System.out.println("线程" + Thread.currentThread().getName() +
                    "即将发布命令");
            cdOrder.countDown(); //发送命令，cdOrder减1，处于等待的战士们停止等待转去执行任务。
            System.out.println("线程" + Thread.currentThread().getName() +
                    "已发送命令，正在等待结果");
            cdAnswer.await(); //命令发送后指挥官处于等待状态，一旦cdAnswer为0时停止等待继续往下执行
            System.out.println("线程" + Thread.currentThread().getName() +
                    "已收到所有响应结果");
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown(); //任务结束，停止线程池的所有线程
    }

    @Test
    public void testListSublist() {
        List<Integer> a = new ArrayList<>();
        List<Integer> b = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        b = a.subList(0, 2);
        System.out.println(a);
        System.out.println(b);
    }

    @Test
    public void testAtomic() {
        AtomicReference<HttpReport> integerAtomicReference = new AtomicReference<>();
        HttpReport httpReport = integerAtomicReference.get();
        System.out.println(httpReport);
    }

    @Test
    public void testEncode() {
        String encode = URLEncoder.encode("Fri Apr 19 2019 00:00:00 GMT 0800 (CST)");
        System.out.println(encode);
    }

    @Test
    public void testDecode() {
        String encode = URLDecoder.decode("Mon Apr 22 2019 00:00:00 GMT+0800 (CST)");
        System.out.println(encode);
    }

//    public static void main6(String[] args) {
//        String s = TimeUtil.timestamp2String(TencentDynamicCreativeService.string2Timestamp("Thu Jan 03 2019 12:45:00 GMT+0800 (CST)", "EEE MMM dd yyyy HH:mm:ss 'GMT+0800 (CST)'"), "yyyy-MM-dd");
//        System.out.println(s);
//    }

//    public static void main(String[] args) throws ParseException {
//        Date date = new Date();
//        System.out.println(date.getTime());
//        String intotime = "2019-03-26 12:00:00";
//        long l = TimeUtil.convertUnixTimestamp(intotime, "yyyy-MM-dd HH:mm:ss");
//        System.out.println(l);
//    }

    public static void main2(String[] args) {
        String s = "Sat%20Jan%2005%202019%2010%3A34%3A00%20GMT%2B0800%20(CST)";
        String decode = URLDecoder.decode(s);
//        decode = decode.replaceAll(" GMT\\+0800 \\(CST\\)", "");
        System.out.println(decode);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MMM dd yyyy HH:mm:ss 'GMT+0800 (CST)'", Locale.US);
        LocalDateTime ldt = LocalDateTime.parse(decode, dtf);
        long l = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(l);
    }

    public static void main3(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MMM dd yyyy HH:mm:ss", Locale.US);
        LocalDateTime d = LocalDateTime.ofInstant(Instant.ofEpochMilli(1546053678921L), ZoneId.systemDefault());
        String str = d.format(dtf);
        System.out.println(str);
    }

//    public static void main5(String[] args) {
//        System.out.println(new Date());
//        String s = "Mon%20Jan%2021%202019%2017%3A39%3A00%20GMT%2B0800%20(CST)";
//        String s1 = TimeUtil.timestamp2String(TencentDynamicCreativeService.string2Timestamp(URLDecoder.decode(s), "E MMM dd yyyy HH:mm:ss 'GMT+0800 (CST)'"), "yyyy-MM-dd");
//        System.out.println(s1);
//    }

    private static String unicodeToCn(String unicode) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }

    @Test
    public void convert2Cn() {
        String a = "\\u586b\\u5199\\u7ed3\\u675f\\u65f6\\u95f4\\u9519\\u8bef\\uff0c\\u683c\\u5f0f\\uff1a";
        String s = unicodeToCn(a);
        System.out.println(s);
    }

//    @Test
//    public void testTimePattern() {
//        String s = "\"2019-04-19T00:00:00.000Z\"";
//        System.out.println(s.length());
//        String s1 = TimeUtil.timestamp2String(TencentDynamicCreativeService.string2Timestamp(URLDecoder.decode(s), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), "yyyy-MM-dd");
//        System.out.println(s1);
//    }

//    @Test
//    public void testTimePattern2() {
//        String s = "Fri Apr 19 2019 00:00:00 GMT+0800 (CST)";
//        System.out.println(s.length());
//        String s1 = TimeUtil.timestamp2String(TencentDynamicCreativeService.string2Timestamp(s, "E MMM dd yyyy HH:mm:ss 'GMT+0800 (CST)'"), "yyyy-MM-dd");
//        System.out.println(s1);
//    }

    @Test
    public void testMd5() {
        System.out.println(MessageDigestUtil.getMD5("1_1_1"));
    }

    //    public static void main(String[] args) throws InterruptedException {
//        //10名运动员
//        final CountDownLatch count = new CountDownLatch(5);
//
//        //java的线程池
//        final ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//        for(int index=1;index<=10;index++){
//            final int number = index;
//            executorService.submit(new Runnable() {
//                public void run() {
//                    try {
//
//                        System.out.println(number+": departed");
//                        Thread.sleep((long) (Math.random()*10000));
//                        System.out.println(number+": arrived");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally{
//                        //运动员到达终点,count数减一
//                        count.countDown();
//                    }
//                }
//            });
//        }
//        System.out.println("Game Started");
//        //等待count数变为0,否则会一直处于等待状态,游戏就没法结束了
//        count.await();
//        System.out.println("Game Over");
//        //关掉线程池
//        executorService.shutdown();
//    }

//    public static void main(String[] args){
//        long l = System.currentTimeMillis();
//        final CountDownLatch latch = new CountDownLatch(10);
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//        for (int i = 1; i < 1000; i++) {
////            Sleep task = new Sleep(i,latch);
//            Sleep sleep = new Sleep(i, latch);
//            executorService.execute(sleep);
//        }
//
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("over");
//        executorService.shutdown();
////        executorService.shutdown();
////        while (executorService.isTerminated()){
////            System.out.println(System.currentTimeMillis() - l);
////        }
//    }

    static class Sleep implements Runnable {
        private int taskNum;
        private CountDownLatch latch;

        public Sleep(int taskNum,CountDownLatch latch) {
            this.taskNum = taskNum;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                System.out.println(taskNum + "开始");
                Thread.sleep(taskNum*100);
                System.out.println(taskNum + "结束");
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args){
//        String url = "http://localhost/addap/test";
//        JSONObject params = new JSONObject();
//        params.put("a","a");
//        params.put("b","b");
//        params.put("c","c");
//        File file = new File("D:\\MyConfiguration\\qqy48861\\Downloads\\banner.zip");
//        JSONObject jsonObject = HttpClientUtil.doPostWithFile(url, params, file);
//        System.out.println(jsonObject);
//    }

    public static void main(String[] args){
        String url = "http://localhost/addap/test";
        JSONObject params = new JSONObject();
        params.put("a","a");
        params.put("b","b");
        params.put("c","c");
        File file = new File("D:\\MyConfiguration\\qqy48861\\Downloads\\banner.zip");
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put("file",file);
        HttpReport hr = HttpGatling.shoot(
                HttpBullet.custom()
                        .url(url)
                        .method(HttpMethods.POST)
                        .entity(HttpEntitys.FILE)
                        .param(params)
                        .file(fileMap)
        );
        JSONObject resJson = hr.getResJson();
        System.out.println(resJson);
    }

}

class Task implements Runnable {
    private int taskNum;

    public Task(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void run() {
        HttpReport hr = HttpGatling.shoot(HttpBullet.custom().url("http://tctj.t.ly.com/salm/entrance?cid=408&deviceId=FDEA4B08-53C5-44CE-83B0-8E3A7405D47E&userId=34847382"));
        System.out.println(hr);
    }
}