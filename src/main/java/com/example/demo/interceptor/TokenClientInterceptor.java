package com.example.demo.interceptor;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

@Slf4j
@GrpcGlobalClientInterceptor
public class TokenClientInterceptor implements ClientInterceptor {

    private static final String X_BEARER_TOKEN = "x-bearer-token";

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("client interceptor: calling server method {}", methodDescriptor.getFullMethodName());
        return new ForwardingClientCall.SimpleForwardingClientCall<>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(Metadata.Key.of(X_BEARER_TOKEN, Metadata.ASCII_STRING_MARSHALLER), "my-secret-token");
                super.start(responseListener, headers);
            }
        };
    }
}
