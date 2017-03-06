package com.im.chat.engine;

import java.io.EOFException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by lizhanbei on 16/9/8.
 */
public class EmptyJsonLenientConverterFactory extends Converter.Factory {

    private final GsonConverterFactory mGsonConverterFactory;

    public EmptyJsonLenientConverterFactory(
            GsonConverterFactory gsonConverterFactory) {
        mGsonConverterFactory = gsonConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations,
                                                          Annotation[] methodAnnotations,
                                                          Retrofit retrofit) {
        return mGsonConverterFactory.requestBodyConverter(type,
                parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
                                                            Annotation[] annotations,
                                                            Retrofit retrofit) {
        final Converter<ResponseBody, ?> delegateConverter =
                mGsonConverterFactory.responseBodyConverter(type,
                        annotations, retrofit);
        return value -> {
            try {
                return delegateConverter.convert(value);
            } catch (EOFException e) {
                // just return null
                return null;
            }
        };
    }
}
