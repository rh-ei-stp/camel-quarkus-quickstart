package org.acme;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.agroal.api.AgroalDataSource;
import io.quarkus.agroal.DataSource;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class AppLifecycleBean {

    @Inject
    @DataSource("users")
    AgroalDataSource usersDataSource;

    void onStart(@Observes StartupEvent ev) {
        log.info("The application is starting...");
        try {
           // NetworkServerControl server = new NetworkServerControl(InetAddress.getByName("localhost"), 1527);
          //  server.start(null);
            initTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        log.info("The application is stopping...");
    }

    void initTables() throws SQLException {
        log.info("Init tables .....");

        try (Connection con = usersDataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                try {
                    statement.execute("drop table users");
                } catch (Exception ignored) {
                }
                statement.execute(
                        "create table users (id int primary key, name varchar(60), email varchar(60), age int)");
                statement.execute(
                        "insert into users (id, name, email, age) values (1001, 'John', 'john@redhat.com', 40)");
                statement.execute(
                        "insert into users (id, name, email, age) values (1002, 'Jane', 'jane@redhat.com', 35)");
            }
        }
    }
}
