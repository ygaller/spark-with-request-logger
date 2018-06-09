package app.utils;

import org.eclipse.jetty.server.AbstractNCSARequestLog;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

public class EmbeddedJettyFactoryConstructor {
    AbstractNCSARequestLog requestLog;

    public EmbeddedJettyFactoryConstructor(AbstractNCSARequestLog requestLog) {
        this.requestLog = requestLog;
    }

    EmbeddedJettyFactory create() {
        EmbeddedJettyServerFactory embeddedJettyServerFactory = new EmbeddedJettyServerFactory(this);

        return new EmbeddedJettyFactory(embeddedJettyServerFactory);
    }

}
