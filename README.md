# Spring Rest Docs AMQP

[![Apache 2.0](https://img.shields.io/github/license/micrometer-metrics/micrometer.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Spring Rest Docs AMQP is extension for Spring Rest Docs and Spring Cloud Contract to generate snippet files from Contract, and we don't want repeat our self steps to manual create document specification of message schema. 

This way, the message schema (snippet) is generated from Spring Cloud Contract testing in Aciidoctor format, and you can compile it to beautiful HTML or PDF document.


## Pre-Requisite

Your application implement Spring Cloud Contract with stub AMQP message configure.

## Add test dependency 

```xml
<dependency>
    <groupId>com.ascendcorp</groupId>
    <artifactId>spring-restdocs-amqp</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
</dependency>
```

## Bind message processor 

Initial AMQP message post processor and bind with RabbitTemplate in contract test base class

```java
@Rule
public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

@Autowired
RabbitTemplate rabbitTemplate;

@Rule
public TestName testName = new TestName();
``
@Before
public void setup() {
    String testName = this.testName.getMethodName();

    AmqpMessagePostProcessor amqpMessagePostProcessor = new AmqpMessagePostProcessor(testName, restDocumentation, rabbitTemplate);
    rabbitTemplate.setBeforePublishPostProcessors(amqpMessagePostProcessor);
}
```

After run contract testing, the snippet file will be generated to target/generated-snippets/{methodName} folder



-------------------------------------
_Licensed under [Apache Software License 2.0](https://www.apache.org/licenses/LICENSE-2.0)_