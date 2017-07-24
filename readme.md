# Embedded Jetty Spark instance with request logger

How to create a Spark instance with an embedded Jetty server containing a log4j request logger

## Creating an embedded Jetty server with a request logger

Spark 2.6.0 introduced the option of providing a configurable embedded Jetty server. 
This tutorial shows how to use this capability in order to configure such a server
that supports logging of incoming requests using log4j.

### Initial setup

We'll start with a basic hello world instance of Spark

~~~java

public class ApplicationMain {

    public static void main(String[] args) {
        get("/hello", (request, response) -> "world");
    }

}

~~~

### Creating the logger

First up, let's create the access logger. Given a log4j logger, we will want to log messages in a standard format. For this purpose, we can implement an instance of `AbstractNCSARequestLog` that takes our logger as an argument

~~~java

public class RequestLogFactory {

    private Logger logger;

    public RequestLogFactory(Logger logger) {
        this.logger = logger;
    }

    AbstractNCSARequestLog create() {
        return new AbstractNCSARequestLog() {
            @Override
            protected boolean isEnabled() {
                return true;
            }

            @Override
            public void write(String s) throws IOException {
                logger.info(s);
            }
        };
    }
}

~~~

### The embedded server factory

We can't just provide Spark with a server instance. Rather, we need to provide a factory that Spark will invoke when it decides to create the server. This factory can take the request log as an argument

~~~java

public class EmbeddedJettyFactoryConstructor {
    AbstractNCSARequestLog requestLog;

    public EmbeddedJettyFactoryConstructor(AbstractNCSARequestLog requestLog) {
        this.requestLog = requestLog;
    }

    EmbeddedJettyFactory create() {
        return new EmbeddedJettyFactory((maxThreads, minThreads, threadTimeoutMillis) -> {
            Server server;
            if (maxThreads > 0) {
                int max = maxThreads > 0 ? maxThreads : 200;
                int min = minThreads > 0 ? minThreads : 8;
                int idleTimeout = threadTimeoutMillis > 0 ? threadTimeoutMillis : '\uea60';
                server = new Server(new QueuedThreadPool(max, min, idleTimeout));
            } else {
                server = new Server();
            }

            server.setRequestLog(requestLog);
            return server;
        });
    }
}

~~~

The implementation here is identical to Spark's embedded Jetty with the addition of `server.setRequestLog(requestLog);`

### Creating a utitily function for the embedded server

Let's tie it together in a utility function that accepts our original log4j logger and overrides Spark's default Jetty implementation with ours

~~~java

public class SparkUtils {
    public static void createServerWithRequestLog(Logger logger) {
        EmbeddedJettyFactory factory = createEmbeddedJettyFactoryWithRequestLog(logger);
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, factory);
    }

    private static EmbeddedJettyFactory createEmbeddedJettyFactoryWithRequestLog(org.apache.log4j.Logger logger) {
        AbstractNCSARequestLog requestLog = new RequestLogFactory(logger).create();
        return new EmbeddedJettyFactoryConstructor(requestLog).create();
    }
}

~~~

Now all that remains is to define the log4j logger and call the utility function in our main 

~~~java

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(ApplicationMain.class);
        SparkUtils.createServerWithRequestLog(logger);

        get("/hello", (request, response) -> "world");
    }

~~~

## The result

After we spin up our Spark instance and go to [http://localhost:4567/hello], we will see the following output in logs:

~~~console

2017-07-24 21:29:52 INFO  ApplicationMain:25 - 0:0:0:0:0:0:0:1 - - [24/Jul/2017:18:29:52 +0000] "GET /hello HTTP/1.1" 200 5 

~~~
