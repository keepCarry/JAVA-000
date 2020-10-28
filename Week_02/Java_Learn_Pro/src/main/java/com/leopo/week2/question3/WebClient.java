package com.leopo.week2.question3;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class WebClient {

    public static void main(String args[]) {
        String reqUri = "http://localhost:8801/";
        String parm = "data=0_0_1";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpResponse response = null;
            HttpPost httpPost = new HttpPost(reqUri + "?" + parm);
            Header ContentTypeHeader = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader(ContentTypeHeader);
            try {
                response = httpclient.execute(httpPost);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            int statusCode = response.getStatusLine().getStatusCode();
            String getResult = null;
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("error status code: " + statusCode);
            } else {
                try {
                    getResult = EntityUtils.toString(response.getEntity());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if (getResult != null) {
                System.out.println(getResult.toString());
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

