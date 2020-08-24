package com.ascendcorp;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.RequestCookie;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public class AmqpPublishOperation implements OperationRequest {
    private Message message;
    private HttpHeaders httpHeaders;

    AmqpPublishOperation(Message message) {
        this.message = message;
        this.httpHeaders = new HttpHeaders();

        Optional.ofNullable(message.getMessageProperties().getContentType())
                .ifPresent(value -> httpHeaders.add(AmqpHeaders.CONTENT_TYPE, value));

        Optional.ofNullable(message.getMessageProperties().getReceivedRoutingKey())
                .ifPresent(value -> httpHeaders.add(AmqpHeaders.RECEIVED_ROUTING_KEY, value));
    }

    @Override
    public byte[] getContent() {
        return message.getBody();
    }

    @Override
    public String getContentAsString() {
        return new String(message.getBody());
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    @Override
    public HttpMethod getMethod() {
        return null;
    }

    @Override
    public Parameters getParameters() {
        return null;
    }

    @Override
    public Collection<OperationRequestPart> getParts() {
        return null;
    }

    @Override
    public URI getUri() {
        return null;
    }

    @Override
    public Collection<RequestCookie> getCookies() {
        return null;
    }
}
