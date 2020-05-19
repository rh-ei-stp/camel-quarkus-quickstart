package org.acme.route;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.acme.util.BrokerConfig;
import org.acme.util.DBConfig;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.support.SimpleUuidGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class Route extends RouteBuilder {

    @Inject
    BrokerConfig brokerConfig;
    
    @Inject
    DBConfig dbConfig;
    
    public void configure() {

            log.info("** building Route1");    
            

            SimpleUuidGenerator uuidGenerator = new SimpleUuidGenerator();
            
            from("direct:insertUser").routeId("InsertUser")
                .split().jsonpath("$")
                    .to("log:addUser?showAll=true&multiline=true")
                    .setHeader("id").method(uuidGenerator, "generateUuid")
                    .setHeader("name").jsonpath("$.name")
                    .setHeader("email").jsonpath("$.email")
                    .setHeader("age").jsonpath("$.age")
                    .setBody(constant(""))
                    .to("log:Before_insertUser?showAll=true&multiline=true")
                    .to("sql:insert into users (id, name, email, age) values (:#id, :#name, :#email, :#age)")
                    .to("log:insertUser?showAll=true&multiline=true");
                    
            from("direct:getUser").routeId("GetUser")
                .to("sql:select * from users")
                .to("log:getUser?level=DEBUG&showBody=true&showHeaders=true")
                .marshal().json(JsonLibrary.Jackson, true);


            from("direct:sendJMS").routeId("SendJMS")
                .split().jsonpath("$")
                .to("log:sendJMS?showAll=true&multiline=true")
                .toD("artemis:queue:${header.QUEUE}");

            from("direct:receiveJMS").routeId("ReceiveJMS")
                .pollEnrich()
                    .simple("artemis-batch:queue:${header.QUEUE}?completionSize=10&aggregationStrategy=#groupedStrategy")
                    .timeout(0)
                .to("log:receiveJMS?showAll=true&multiline=true")
                .marshal().json(JsonLibrary.Jackson, true);
/*
            from("artemis-batch:queue:TEST3?completionSize=10&aggregationStrategy=#groupedStrategy")
                .to("log:TEST?showAll=true&multiline=true");
*/

    }
}