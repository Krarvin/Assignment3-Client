package nz.ac.vuw.swen301.assignment3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CreateRandomLogs {
    public static void main(String args[]){
        Resthome4LogsAppender appender = new Resthome4LogsAppender();
        Logger logger = Logger.getLogger("RandomLogs");
        logger.addAppender(appender);
        for(int i = 0; i < 25; i++){
//            try {
//                TimeUnit.SECONDS.wait(1);
//            }catch(InterruptedException e){
//                throw new RuntimeException(e);
//            }
            Random rand = new Random();
            int level = rand.nextInt(5) + 1;
            System.out.println(level);
            switch(level){
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
}
