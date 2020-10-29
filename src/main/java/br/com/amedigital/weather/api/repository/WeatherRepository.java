package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.*;

@Repository
public class WeatherRepository extends BaseRepository {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherRepository.class);
    public WeatherRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Flux<WeatherEntity> save(List<WeatherEntity> entities) {

        List<WeatherEntity> weatherEntities = new ArrayList<>();

        return async(() -> jdbi.inTransaction(handle -> {

            entities.stream().forEach(entity -> {
                entity.setId(UUID.randomUUID().toString());
                handle.createUpdate(sqlLocator.locate("sql.save-weather"))
                        .bindBean(entity)
                        .execute();
                weatherEntities.add(entity);
            });

            return weatherEntities;
        })).flatMapIterable(e -> e);
    }

    public Flux<WeatherEntity> update(List<WeatherEntity> entities) {

        List<WeatherEntity> weatherEntities = new ArrayList<>();

        return async(() -> jdbi.inTransaction(handle -> {

            entities.stream().forEach(entity -> {
                handle.createUpdate(sqlLocator.locate("sql.update-weather"))
                        .bindBean(entity)
                        .execute();
                weatherEntities.add(entity);
            });

            return weatherEntities;
        })).flatMapIterable(e -> e);
    }

    public Mono<Void> delete(String id) {
        return async(() -> jdbi.inTransaction(handle ->
                handle.createUpdate(sqlLocator.locate("sql.delete-weather"))
                        .bind(0, id)
                        .execute()
        )).then();
    }

    public Mono<Void> softDelete(String id) {
        return async(() -> jdbi.inTransaction(handle ->
                handle.createUpdate(sqlLocator.locate("sql.update-soft-delete-weather"))
                        .bind("id", id)
                        .execute()
        )).then();
    }

    public Flux<WeatherEntity> findAll(WeatherRequest weather) {
        return async(() -> jdbi.inTransaction(handle -> handle
                .select(sqlLocator.locate("sql.find-all-weather"))
                .define("where", queryWhere(weather))
                .mapToBean(WeatherEntity.class)
                .list())).flatMapIterable(e -> e);
    }

    public Mono<Optional<WeatherEntity>> findOne(String id) {
        return async(() -> jdbi.inTransaction(handle -> handle
                .select(sqlLocator.locate("sql.find-one-weather"), id)
                .mapToBean(WeatherEntity.class)
                .findFirst())).map(e -> e);
    }

    public String queryWhere(WeatherRequest weatherRequest) {

        List<String> conditions = new ArrayList<>();

        if(Objects.nonNull(weatherRequest.getDate())
                && !StringUtils.isEmpty(weatherRequest.getDate())) {
            conditions.add("date = '".concat(weatherRequest.getDate().toString()).concat("'"));
        }

        if(Objects.nonNull(weatherRequest.getMaximumTemperature())
                && !StringUtils.isEmpty(weatherRequest.getMaximumTemperature())) {
            conditions.add("maximumTemperature = ".concat(weatherRequest.getMaximumTemperature().toString()));
        }

        if(Objects.nonNull(weatherRequest.getMinimumTemperature())
                && !StringUtils.isEmpty(weatherRequest.getMinimumTemperature())) {
            conditions.add("minimumTemperature = ".concat(weatherRequest.getMinimumTemperature().toString()));
        }

        conditions.add("deletedAt IS NULL");


        return "WHERE ".concat(String.join(" AND ", conditions));
    }
}
