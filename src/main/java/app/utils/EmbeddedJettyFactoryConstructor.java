package app.utils;

import org.eclipse.jetty.server.AbstractNCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

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
