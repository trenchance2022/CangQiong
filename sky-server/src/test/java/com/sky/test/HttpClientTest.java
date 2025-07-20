package com.sky.test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {
    @Test
    public void testGET() throws Exception{
        //创建httpClient对象
        //CloseableHttpClient 实现了 HttpClient
        CloseableHttpClient httpClient= HttpClients.createDefault();

        //创建请求对象
        HttpGet httpGet=new HttpGet("http://localhost:8080/user/shop/status");

        //发送请求
        CloseableHttpResponse response=httpClient.execute(httpGet);

        //获取状态码
        System.out.println("status code:"+response.getStatusLine().getStatusCode());//status code:200

        //获取响应数据
        HttpEntity entity=response.getEntity();
        String body=EntityUtils.toString(entity);
        System.out.println("data:"+body);//data:{"code":1,"msg":null,"data":0}

        //关闭资源
        response.close();
        httpClient.close();
    }

    @Test
    public void testPOST() throws Exception{
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpPost httpPost=new HttpPost("http://localhost:8080/admin/employee/login");

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");
        StringEntity entity=new StringEntity(jsonObject.toString());
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpResponse response=httpClient.execute(httpPost);

        HttpEntity httpEntity=response.getEntity();
        String body=EntityUtils.toString(httpEntity);
        System.out.println("data:"+body);

        response.close();
        httpClient.close();
    }
}
