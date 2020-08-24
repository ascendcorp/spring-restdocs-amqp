package com.ascendcorp;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.preprocess.PrettyPrintingContentModifier;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import java.util.*;

public class AmqpMessageTemplate extends TemplatedSnippet {

    protected AmqpMessageTemplate(){
        super("message-output", null);
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {

        OperationRequest requestMessage = operation.getRequest();
        String body = getJsonBody(requestMessage.getContent());

        HashMap<String, Object> map = new HashMap<>();
        map.put("body", body);

        List<Map<String, Object>> headers = new ArrayList<>();
        Optional.of(requestMessage.getHeaders())
                .ifPresent(c -> c.forEach((key, values) -> {
                    Optional.ofNullable(values).ifPresent(v -> v.forEach(value -> {
                        HashMap<String, Object> h = new HashMap<>();
                        h.put("name", key);
                        h.put("value", value);
                        headers.add(h);
                    }));
                }));
        map.put("headers", headers);
        return map;
    }

    private String getJsonBody(byte[] body) {

        PrettyPrintingContentModifier pretty = new PrettyPrintingContentModifier();
        byte[] jsonPretty = pretty.modifyContent(body, MediaType.APPLICATION_JSON);

        return new String(jsonPretty);
    }
}
