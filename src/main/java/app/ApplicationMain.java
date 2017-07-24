package app;

import app.utils.SparkUtils;
import org.apache.log4j.Logger;

import static spark.Spark.get;

public class ApplicationMain {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ApplicationMain.class);
        SparkUtils.createServerWithRequestLog(logger);

        get("/hello", (request, response) -> "world");
    }

}
