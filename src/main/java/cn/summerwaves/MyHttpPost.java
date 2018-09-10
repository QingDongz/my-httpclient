package cn.summerwaves;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;

public class MyHttpPost {
    public static void main(String... args) throws IOException {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            File file = new File("F:\\onedrive\\图片\\壁纸\\wallhaven-149790.jpg");
            String message = "This is a multipart post";

            // build multipart upload request
            HttpEntity data = MultipartEntityBuilder.create()
                    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                    .addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName())
                    .addTextBody("text", message, ContentType.DEFAULT_BINARY)
                    .build();

            // build http request and assign multipart upload data
            HttpUriRequest request = RequestBuilder
                    .post("http://localhost:8888/upload/customer")
                    .setEntity(data)
                    .build();

            System.out.println("Executing request " + request.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            String responseBody = httpclient.execute(request, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            if (jsonObject.containsKey("url")) {
                System.out.println("取回的url为:"+jsonObject.get("url"));
            } else {
                throw new JSONException("未发现键值");
            }

        }
    }
}
