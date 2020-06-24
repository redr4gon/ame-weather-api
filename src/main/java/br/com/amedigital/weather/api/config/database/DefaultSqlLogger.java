package br.com.amedigital.weather.api.config.database;

import org.apache.commons.lang3.RegExUtils;
import org.jdbi.v3.core.statement.SqlLogger;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;

public class DefaultSqlLogger implements SqlLogger {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSqlLogger.class);

    @Override
    public void logAfterExecution(StatementContext context) {
        LOG.debug(" SQL: [{}]\tExecution time -> {} ms",
                RegExUtils.replaceAll(context.getParsedSql().getSql(), "\\R+", " "),
                context.getElapsedTime(ChronoUnit.MILLIS));
    }
}
