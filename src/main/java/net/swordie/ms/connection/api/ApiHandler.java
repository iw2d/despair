package net.swordie.ms.connection.api;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import net.swordie.ms.ServerConfig;
import net.swordie.ms.ServerConstants;
import net.swordie.ms.handlers.http.HttpHandler;
import net.swordie.ms.util.Util;
import net.swordie.ms.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ApiHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger log = LogManager.getLogger(ApiHandler.class);
    private static final Map<Endpoint, Method> handlers = new HashMap<>();

    public static void initHandlers() {
        String handlersDir = ServerConstants.HANDLERS_DIR;
        Set<File> files = new HashSet<>();
        Util.findAllFilesInDirectory(files, new File(handlersDir));
        for (File file : files) {
            try {
                // grab all files in the handlers dir, strip them to their package name, and remove .java extension
                String className = file.getPath()
                        .replaceAll("[\\\\|/]", ".")
                        .split("src\\.main\\.java\\.")[1]
                        .replaceAll("\\.java", "");
                Class clazz = Class.forName(className);
                for (Method method : clazz.getMethods()) {
                    HttpHandler handler = method.getAnnotation(HttpHandler.class);
                    if (handler != null) {
                        Endpoint endpoint = new Endpoint(handler.method(), handler.path());
                        if (handlers.containsKey(endpoint)) {
                            throw new IllegalArgumentException(String.format("Multiple handlers found for endpoint %s %s! " +
                                    "Had method %s, but also found %s.", endpoint.getLeft(), endpoint.getRight(), handlers.get(endpoint).getName(), method.getName()));
                        }
                        handlers.put(endpoint, method);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (ServerConfig.DEBUG_MODE) {
            log.debug(String.format("[API]\t| %s %s", request.method(), request.uri()));
        }
        Method method = handlers.get(Endpoint.from(request));
        if (method != null) {
            JSONObject responseData = null;
            try {
                if (method.getParameterCount() == 0) {
                    responseData = (JSONObject) method.invoke(this);
                } else {
                    Class<?> clazz = method.getParameterTypes()[0];
                    String requestData = request.content().toString(CharsetUtil.UTF_8);
                    if (clazz == String.class) {
                        responseData = (JSONObject) method.invoke(this, requestData);
                    } else if (clazz == JSONObject.class) {
                        responseData = (JSONObject) method.invoke(this, new JSONObject(new JSONTokener(requestData)));
                    } else {
                        log.error("Unhandled first param type of handler " + method.getName() + ", type = " + clazz);
                        handleUnknown(ctx);
                        return;
                    }
                }
                handleResponse(ctx, responseData);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // e.printStackTrace();
                handleError(ctx, e);
            }
        } else {
            handleUnknown(ctx);
        }
    }

    protected void handleResponse(ChannelHandlerContext ctx, JSONObject responseData) {
        if (responseData != null) {
            HttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.copiedBuffer(responseData.toString(), CharsetUtil.UTF_8)
            );
            response.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            ctx.writeAndFlush(response);
        } else {
            ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
        }
        ctx.channel().close();
    }

    protected void handleError(ChannelHandlerContext ctx, Throwable cause) {
        ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
        ctx.channel().close();
    }

    protected void handleUnknown(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private static class Endpoint extends Tuple<String, String> {
        public Endpoint(String left, String right) {
            super(left, right);
        }

        public static Endpoint from(HttpRequest request) {
            return new Endpoint(request.method().name(), request.uri());
        }
    }

}
