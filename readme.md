---
layout: tutorial
title: "Creating an embedded Jetty server with a request log"
author: <a href="https://www.linkedin.com/in/yairgaller" target="_blank">Yair Galler</a>
date: 2017-07-24 21:00:00
comments: true
permalink: /tutorials/jetty-request-log
category: simple
summarytitle: Embedded Jetty with request logger
summary: Create a Spark instance with an embedded Jetty server configuration with a log4j request logger
---

## Creating an embedded Jetty server with a request logger

Spark 2.6.0 introduced the option of providing a configurable embedded Jetty server. 
We are going to use this capability in order to configure such a server
that supports logging incoming requests.

