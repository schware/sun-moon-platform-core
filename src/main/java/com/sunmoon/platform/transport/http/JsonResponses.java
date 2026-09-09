package com.sunmoon.platform.transport.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public final class JsonResponses {

    /**
     * java.time is registered because a default ObjectMapper refuses to
     * serialize an {@code Instant} at all — it throws rather than
     * degrading, so a single timestamp in a response body turns the whole
     * endpoint into a 500. Written as ISO-8601 rather than epoch numbers,
     * which is what a browser client can parse without being told how.
     */
    private static final ObjectMapper JSON = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static FullHttpResponse of(HttpResponseStatus status, Object body) {
        byte[] bytes;
        try {
            bytes = JSON.writeValueAsBytes(body);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(bytes));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        return response;
    }

    private JsonResponses() {
    }
}
