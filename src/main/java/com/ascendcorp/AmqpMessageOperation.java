package com.ascendcorp;

import org.springframework.amqp.core.Message;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.snippet.RestDocumentationContextPlaceholderResolverFactory;
import org.springframework.restdocs.snippet.StandardWriterResolver;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.StandardTemplateResourceResolver;
import org.springframework.restdocs.templates.TemplateEngine;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.restdocs.templates.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class AmqpMessageOperation implements Operation {

    private Map<String, Object> attributes;
    private String identifier;
    private OperationRequest operationRequest;

    public AmqpMessageOperation(String identifier, RestDocumentationContextProvider document, Message message) {
        this.identifier = identifier;

        StandardWriterResolver standardWriterResolver = new StandardWriterResolver(
                new RestDocumentationContextPlaceholderResolverFactory(), "UTF-8", TemplateFormats.asciidoctor());
        TemplateEngine templateEngine = new MustacheTemplateEngine(new StandardTemplateResourceResolver(TemplateFormats.asciidoctor()));

        attributes = new HashMap<>();
        attributes.put(RestDocumentationContext.class.getName(), document.beforeOperation());
        attributes.put(WriterResolver.class.getName(), standardWriterResolver);
        attributes.put(TemplateEngine.class.getName(), templateEngine);

        operationRequest = new AmqpPublishOperation(message);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.identifier;
    }

    @Override
    public OperationRequest getRequest() {
        return operationRequest;
    }

    @Override
    public OperationResponse getResponse() {
        return null;
    }
}
