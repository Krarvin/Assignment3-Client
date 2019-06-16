package test.nz.ac.vuw.swen301;

import org.apache.http.client.methods.HttpGet;
import nz.ac.vuw.swen301.assignment3.Resthome4LogsAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.JVM)
public class Resthome4LogsAppenderTests{
    private static final String TEST_HOST = "localhost";
    private static final int TEST_PORT = 8080;
    private static final String TEST_PATH = "/resthome4logs/"; // as defined in pom.xml
    private static final String LOG_PATH = TEST_PATH + "logs"; // as defined in pom.xml and web.xml


    private HttpResponse get(URI uri) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(uri);
        return httpClient.execute(request);
    }

    private boolean isServerReady() throws Exception {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(TEST_PATH);
        URI uri = builder.build();
        try {
            HttpResponse response = get(uri);
            boolean success = response.getStatusLine().getStatusCode() == 200;

            if (!success) {
                System.err.println("Check whether server is up and running, request to " + uri + " returns " + response.getStatusLine());
            }

            return success;
        }
        catch (Exception x) {
            System.err.println("Encountered error connecting to " + uri + " -- check whether server is running and application has been deployed");
            return false;
        }
    }




    @Test
    public void AppenderCacheTest()throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i< 15;i++){
            Random rand = new Random();
            int level = rand.nextInt(5) + 1;
//            System.out.println(level);
            switch (level) {
                case 1:
                    logger.log(Level.FATAL, "fatal message");
                    break;
                case 2:
                    logger.log(Level.ERROR, "error message");
                    break;
                case 3:
                    logger.log(Level.WARN, "warn message");
                    break;
                case 4:
                    logger.log(Level.INFO, "info message");
                    break;
                case 5:
                    logger.log(Level.DEBUG, "debug message");
                    break;
                case 6:
                    logger.log(Level.TRACE, "trace message");
                    break;
            }
        }
        //client stores up to 10 logEvents before it posts to server, therefore if 15 are created, 5 should still remain in client
        System.out.println(appender.jsonList.size());
        assertEquals(5,appender.jsonList.size());
    }



    @Test
    public void AppenderUUIDTest()throws Exception{
        Assume.assumeTrue(isServerReady());
        JSONObject json = Resthome4LogsAppender.jsonList.get(0);
        //ensures appender appends the id as a UUID
        assertTrue(isUUID(json.get("id").toString()));
    }


    @Test
    public void AppenderTimeStampTest()throws Exception{
        Assume.assumeTrue(isServerReady());
        JSONObject json = Resthome4LogsAppender.jsonList.get(0);
        //ensures appender appends the timestamp in correct format
        assertTrue(isDate(json.get("timestamp").toString(),"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }


    @Test
    public void INFOAppenderTest3() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i<11; i++){
            logger.log(Level.INFO,"info message");
        }
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOG_PATH).setParameter("limit","5").setParameter("level","INFO");
        URI uri = builder.build();
        HttpResponse response = get(uri);
        String jsonString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(jsonString);
        //checks that appender posts the correct level, and the level matches the level stored in cache for INFO
        assertEquals(jsonArray.getJSONObject(0).get("level"), appender.jsonList.get(0).get("level"));
    }

    @Test
    public void ERRORAppenderTest3() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i<12; i++){
            logger.log(Level.ERROR,"error message");
        }
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOG_PATH).setParameter("limit","5").setParameter("level","ERROR");
        URI uri = builder.build();
        HttpResponse response = get(uri);
        String jsonString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(jsonString);
        //checks that appender posts the correct level, and the level matches the level stored in cache for ERROR
        assertEquals(jsonArray.getJSONObject(0).get("level"), appender.jsonList.get(0).get("level"));
    }

    @Test
    public void DEBUGAppenderTest3() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i<11; i++){
            logger.log(Level.DEBUG,"debug message");
        }
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOG_PATH).setParameter("limit","5").setParameter("level","DEBUG");
        URI uri = builder.build();
        HttpResponse response = get(uri);
        String jsonString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(jsonString);
        //checks that appender posts the correct level, and the level matches the level stored in cache for DEBUG
        assertEquals(jsonArray.getJSONObject(0).get("level"), appender.jsonList.get(0).get("level"));
    }

    @Test
    public void WARNAppenderTest3() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i<11; i++){
            logger.log(Level.WARN,"warn message");
        }
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOG_PATH).setParameter("limit","5").setParameter("level","WARN");
        URI uri = builder.build();
        HttpResponse response = get(uri);
        String jsonString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(jsonString);
        //checks that appender posts the correct level, and the level matches the level stored in cache for WARN
        assertEquals(jsonArray.getJSONObject(0).get("level"), appender.jsonList.get(0).get("level"));
    }

    @Test
    public void FATALAppenderTest3() throws Exception{
        Assume.assumeTrue(isServerReady());
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("testcache");
        logger.addAppender(appender);
        for(int i =0; i<15; i++){
            logger.log(Level.FATAL,"fatal message");
        }
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost(TEST_HOST).setPort(TEST_PORT).setPath(LOG_PATH).setParameter("limit","5").setParameter("level","FATAL");
        URI uri = builder.build();
        HttpResponse response = get(uri);
        String jsonString = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray = new JSONArray(jsonString);
        //checks that appender posts the correct level, and the level matches the level stored in cache for FATAL
        assertEquals(jsonArray.getJSONObject(0).get("level"), appender.jsonList.get(0).get("level"));
    }



    public boolean isUUID(String s){
        try{
            UUID uuid = UUID.fromString(s);
            return true;
        }catch(IllegalArgumentException e){
            return false;
        }

    }

    public void generateRandomLogs() {
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("RandomLogs");
        logger.addAppender( appender);
        for (int i = 0; i < 25; i++) {
            Random rand = new Random();
            int level = rand.nextInt(5) + 1;
//            System.out.println(level);
            switch (level) {
                case 1:
                    logger.log(Level.FATAL, "fatal message");
                    break;
                case 2:
                    logger.log(Level.ERROR, "error message");
                    break;
                case 3:
                    logger.log(Level.WARN, "warn message");
                    break;
                case 4:
                    logger.log(Level.INFO, "info message");
                    break;
                case 5:
                    logger.log(Level.DEBUG, "debug message");
                    break;
                case 6:
                    logger.log(Level.TRACE, "trace message");
                    break;
            }
        }
    }
    public boolean isDate(String dateToValidate, String dateFormat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }
}



