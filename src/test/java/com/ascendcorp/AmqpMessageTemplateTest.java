package com.ascendcorp;


import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;


public class AmqpMessageTemplateTest  {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Mock
    RabbitTemplate rabbitTemplate;

    @Test
    public void generateSnippetFromAmqpMessageProcessor() throws IOException {

        // Given
        String testName = "test_name";
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType("application/json");
        messageProperties.setReceivedRoutingKey("this-is-routing-key");
        Message message = new Message("{\"name\":\"john\",\"age\":10}".getBytes(), messageProperties);

        rabbitTemplate = mock(RabbitTemplate.class);
        rabbitTemplate.send("dfsdfdf", "this-is-routing-key", message, new CorrelationData());

        AmqpMessagePostProcessor postProcessor = new AmqpMessagePostProcessor(testName, restDocumentation, rabbitTemplate);

        // When
        postProcessor.postProcessMessage(message);

        // Then
        String content = getSnippetContent(testName);
        assertEquals(content, "[source,options=\"nowrap\"]\n" +
                "----\n" +
                "contentType: application/json\n" +
                "amqp_receivedRoutingKey: this-is-routing-key\n" +
                "{\n" +
                "  \"name\" : \"john\",\n" +
                "  \"age\" : 10\n" +
                "}\n" +
                "----");
    }

    private String getSnippetContent(String testName) throws IOException {
        File file = new File(new File(restDocumentation.beforeOperation().getOutputDirectory(),
                testName), "message-output" + ".adoc");
        return FileCopyUtils.copyToString(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    }
}
