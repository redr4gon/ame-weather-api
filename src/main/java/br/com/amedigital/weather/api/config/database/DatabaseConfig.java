package br.com.amedigital.weather.api.config.database;

import br.com.amedigital.weather.api.mapper.WeatherMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Handles;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.sql.DataSource;
import java.util.concurrent.Executors;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class DatabaseConfig {

    private static final Logger LOG = getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.hikari.maximumPoolSize}")
    private Integer maximumPoolSize;

    @Bean
    public DataSource dataSource() {
        LOG.info("Conectando no banco de dados [{}] com o numero máximo de pool [{}]...", url, maximumPoolSize);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.url);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setMaximumPoolSize(this.maximumPoolSize);

        return new HikariDataSource(config);
    }

    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        LOG.info("Registrando JDBI Mappers...");

        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.setSqlLogger(new DefaultSqlLogger());
        jdbi.getConfig(Handles.class).setForceEndTransactions(false);
        jdbi.registerRowMapper(new WeatherMapper());

        return jdbi;
    }

    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.fromExecutor(Executors.newFixedThreadPool(this.maximumPoolSize));
    }

}
