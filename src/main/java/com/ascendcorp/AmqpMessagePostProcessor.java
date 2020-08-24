package com.ascendcorp;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.restdocs.RestDocumentationContextProvider;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class AmqpMessagePostProcessor implements MessagePostProcessor {

    private String identifier;
    private RestDocumentationContextProvider document;
    private RabbitTemplate rabbitTemplate;
    private AmqpMessageTemplate amqpMessageTemplate;

    public AmqpMessagePostProcessor(String identifier, RestDocumentationContextProvider document, RabbitTemplate rabbitTemplate) {
        this.identifier = identifier;
        this.document = document;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpMessageTemplate = new AmqpMessageTemplate();
    }

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {

        lookupRoutingKey(message);
        AmqpMessageOperation amqpMessageOperation = new AmqpMessageOperation(identifier, document, message);
        try {
            amqpMessageTemplate.document(amqpMessageOperation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    private void lookupRoutingKey(Message message) {
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(this.rabbitTemplate).send(any(), routingKeyCaptor.capture(), any(), any());

        Optional.ofNullable(routingKeyCaptor.getValue())
                .ifPresent(value -> message.getMessageProperties().setReceivedRoutingKey(value));
    }
}
