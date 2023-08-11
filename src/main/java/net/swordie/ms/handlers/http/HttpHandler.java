package net.swordie.ms.handlers.http;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HttpHandler {

    String method() default "GET";

    String path();
}
