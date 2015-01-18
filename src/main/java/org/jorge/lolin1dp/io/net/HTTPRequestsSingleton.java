package org.jorge.lolin1dp.io.net;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public final class HTTPRequestsSingleton {

    private static final Object LOCK = new Object();
    public static final Integer IN_PLACE_ERROR_STATUS_CODE = 666;
    private static volatile HTTPRequestsSingleton mInstance;
    private final OkHttpClient mClient;

    private HTTPRequestsSingleton() {
        mClient = new OkHttpClient();
    }

    public static HTTPRequestsSingleton getInstance() {
        HTTPRequestsSingleton ret = mInstance;
        if (ret == null) {
            synchronized (LOCK) {
                ret = mInstance;
                if (ret == null) {
                    ret = new HTTPRequestsSingleton();
                    mInstance = ret;
                }
            }
        }
        return ret;
    }

    public Response performRequest(Request request) {
        try {
            return mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return new Response.Builder().code(IN_PLACE_ERROR_STATUS_CODE).build();
        }
    }
}
