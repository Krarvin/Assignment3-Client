package nz.ac.vuw.swen301.assignment3;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import java.net.URI;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

public class Resthome4LogsAppender extends AppenderSkeleton {
    ArrayList<JSONObject> jsonList = new ArrayList<>();
    @Override
    protected void append(LoggingEvent loggingEvent) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", UUID.randomUUID());
        jsonObject.put("message", loggingEvent.getMessage());
        jsonObject.put("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(loggingEvent.getTimeStamp()));
        jsonObject.put("thread", loggingEvent.getThreadName());
        jsonObject.put("logger", loggingEvent.getLoggerName());
        jsonObject.put("level", loggingEvent.getLevel());
        jsonObject.put("errorDetails", "");
        jsonList.add(jsonObject);
        try{
            URIBuilder builder = new URIBuilder();
            builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs");
            URI postUri = builder.build();
            HttpPost post = new HttpPost(postUri);

            StringEntity json = new StringEntity("["+jsonObject.toString()+"]");
            post.addHeader("content-type","application/json");
            post.setEntity(json);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(post);
            System.out.println(response.getStatusLine());

            URIBuilder builder2 = new URIBuilder();
            builder2.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/stats");
            URI getUri = builder2.build();
            HttpGet get = new HttpGet(getUri);
            HttpClient httpClient2 = HttpClientBuilder.create().build();
            HttpResponse response1 = httpClient2.execute(get);
            System.out.println(response1.getStatusLine());
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
