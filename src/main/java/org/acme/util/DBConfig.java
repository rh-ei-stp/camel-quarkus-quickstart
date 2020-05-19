package org.acme.util;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.camel.CamelContext;
import org.apache.camel.component.sql.SqlComponent;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class DBConfig {

    @Inject
    @DataSource("users")
    AgroalDataSource usersDataSource;

    @Inject
    CamelContext camelContext;

    @PostConstruct
    public void setup() {

        log.info("setup DBConfig......");
        SqlComponent sql = (SqlComponent) camelContext.getRegistry().lookupByName("sql");
        sql.setDataSource(usersDataSource);
        log.info("{}", sql.getDataSource());

    }

}