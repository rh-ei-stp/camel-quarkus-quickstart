package org.acme.util;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.component.sjms.SjmsComponent;
import org.apache.camel.component.sjms.batch.SjmsBatchComponent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jgroups.blocks.GridFile;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Singleton
@Slf4j
public class BrokerConfig {

    @ConfigProperty(name = "broker.url")
    String brokerUrl;

    @ConfigProperty(name = "broker.user")
    String brokerUser;

    @ConfigProperty(name = "broker.password")
    String brokerPassword;

    @Inject
    CamelContext camelContext;


    @PostConstruct
    public void setup() {
        log.info("setup......");
        log.info("broker url={}",brokerUrl);
        ActiveMQConnectionFactory cf1 = new ActiveMQConnectionFactory(brokerUrl, brokerUser, brokerPassword);
        cf1.setConsumerWindowSize(0);
        SjmsComponent sjmsComp = new SjmsComponent();
        sjmsComp.setConnectionFactory(cf1);
        camelContext.getRegistry().bind("ActiveMQ_CF1", cf1);
        log.info("add component ActiveMQ_CF1");
        camelContext.getRegistry().bind("artemis", sjmsComp);
        log.info("add component artemis");

        SjmsBatchComponent sjmsBatchComp = new SjmsBatchComponent();
        sjmsBatchComp.setConnectionFactory(cf1);
        camelContext.getRegistry().bind("artemis-batch", sjmsBatchComp);
        log.info("add component artemis-batch");
        
        camelContext.getRegistry().bind("groupedStrategy", AggregationStrategies.groupedBody());
    
    }

}
