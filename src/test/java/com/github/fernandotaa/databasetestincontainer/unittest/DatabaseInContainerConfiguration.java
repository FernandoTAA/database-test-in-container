package com.github.fernandotaa.databasetestincontainer.unittest;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;

import javax.sql.DataSource;
import java.net.URI;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;


@Configuration
@Profile("test")
@AutoConfigureBefore({DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class, HibernateJpaAutoConfiguration.class, FlywayAutoConfiguration.class})
public class DatabaseInContainerConfiguration extends FlywayAutoConfiguration.FlywayConfiguration {

    @Bean
    public FixedHostPortGenericContainer postgresqlContainer(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password
    ) {
        var jdbcUrl = new JdbcUrl(url);
        var postgresqlContainer = new FixedHostPortGenericContainer<>("postgres:10")
                .withEnv("POSTGRES_USER", username)
                .withEnv("POSTGRES_PASSWORD", password)
                .withEnv("POSTGRES_DB", jdbcUrl.getDatabaseName())
                .withFixedExposedPort(jdbcUrl.getPort(), 5432)
                .waitingFor(new LogMessageWaitStrategy()
                        .withRegEx(".*database system is ready to accept connections.*\\s")
                        .withTimes(2)
                        .withStartupTimeout(Duration.of(60, SECONDS))
                );
        postgresqlContainer.start();
        return postgresqlContainer;
    }

    @Bean
    @Primary
    public Flyway flyway(
            FixedHostPortGenericContainer postgresqlContainer,
            FlywayProperties properties, DataSourceProperties dataSourceProperties,
            ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource,
            @FlywayDataSource ObjectProvider<DataSource> flywayDataSource,
            ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers,
            ObjectProvider<JavaMigration> javaMigrations, ObjectProvider<Callback> callbacks
    ) {
        return super.flyway(properties, dataSourceProperties, resourceLoader, dataSource, flywayDataSource,
                fluentConfigurationCustomizers, javaMigrations, callbacks);
    }

    private static class JdbcUrl {

        private URI uri;

        public JdbcUrl(String url) {
            String cleanURI = url.substring(5);
            uri = URI.create(cleanURI);
        }

        public String getDatabaseName() {
            return uri.getPath().substring(1);
        }

        public int getPort() {
            return uri.getPort();
        }
    }
}
