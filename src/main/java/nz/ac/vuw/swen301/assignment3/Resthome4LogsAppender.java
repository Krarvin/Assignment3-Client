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
        jsonObject.put("level", loggingEvent.getLevel().toString());
//        System.out.println(loggingEvent.getLevel().toString());
        jsonObject.put("errorDetails", "");
        jsonList.add(jsonObject);
        System.out.println(jsonList.size());
        if(jsonList.size() == 10) {
            try {
                URIBuilder builder = new URIBuilder();
                builder.setScheme("http").setHost("localhost").setPort(8080).setPath("/resthome4logs/logs");
                URI postUri = builder.build();
                HttpPost post = new HttpPost(postUri);
                String output = "[";
                for(int i =0; i<10; i++){
                    if(i != 9) {
                        output = output + jsonList.get(i).toString() + ",\n";
                    }else{
                        output = output + jsonList.get(i).toString();
                    }
                }
                output = output + "]";
                StringEntity json = new StringEntity(output);
                post.addHeader("content-type", "application/json");
                post.setEntity(json);

                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpResponse response = httpClient.execute(post);
                System.out.println(response.getStatusLine());

            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonList.clear();
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
