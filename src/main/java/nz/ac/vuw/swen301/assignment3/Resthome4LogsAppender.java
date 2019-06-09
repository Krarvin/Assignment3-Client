package nz.ac.vuw.swen301.assignment3;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class Resthome4LogsAppender extends AppenderSkeleton {
    ArrayList<JSONObject> jsonList = new ArrayList<>();
    @Override
    protected void append(LoggingEvent loggingEvent) {
        JSONObject json = new JSONObject();
        json.put("id", UUID.randomUUID());
        json.put("message", loggingEvent.getMessage());
        json.put("timestamp", loggingEvent.timeStamp);
        json.put("thread", loggingEvent.getThreadName());
        json.put("logger", loggingEvent.getLoggerName());
        json.put("level", loggingEvent.getLevel());
        json.put("errorDetails", "");
        jsonList.add(json);
    }

    public void generateRandomLogs(){
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("RandomLogs");
        logger.addAppender(appender);
        while(true){

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
