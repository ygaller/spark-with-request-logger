# Spark with mbedded Jetty with request logger

Create a Spark instance with an embedded Jetty server configuration with a log4j request logger

## Creating an embedded Jetty server with a request logger

Spark 2.6.0 introduced the option of providing a configurable embedded Jetty server. 
We are going to use this capability in order to configure such a server
that supports logging incoming requests.

