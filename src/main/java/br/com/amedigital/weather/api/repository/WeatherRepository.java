package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import br.com.amedigital.weather.api.mapper.WeatherMapper;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class WeatherRepository extends BaseRepository {

    public WeatherRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Flux<WeatherEntity> save(List<WeatherEntity> entities, List<WeatherEntity> entities2) {

        if (entities2 != null) {
            entities2.stream().forEach(weatherEntity1 -> {
                entities.removeIf(weatherEntity2 -> weatherEntity2.getDate().equals(weatherEntity1.getDate()));
            });
        }

        List<WeatherEntity> weatherEntities = new ArrayList<>();

        return async(() -> jdbi.inTransaction(handle -> {

            entities.stream().forEach(entity -> {
                entity.setId(UUID.randomUUID().toString());
                handle.createUpdate(sqlLocator.locate("sql.save-weather"))
                        .bindBean(entity)
                        .execute();
                weatherEntities.add(entity);
            });

            if (entities2 == null) {
                return weatherEntities;
            } else {
                entities2.addAll(weatherEntities);
                return entities2;
            }
        })).flatMapIterable(e -> e);
    }

    public Flux<WeatherEntity> findAllWeather(WeatherRequest weatherRequest) {
        return async(() -> jdbi.inTransaction(handle ->
            handle.createQuery(sqlLocator.locate("sql.findAll-weather"))
                    .define("where", setWhere(weatherRequest))
                    .mapToBean(WeatherEntity.class)
                    .list()
        )).flatMapIterable(e -> e);
    }

    public Flux<WeatherEntity> findAllCityWeather(WeatherRequest weatherRequest) {
        return async(() -> jdbi.inTransaction(handle ->
            handle.createQuery(sqlLocator.locate("sql.findAll-city-weather"))
                    .bind("cityCode", weatherRequest.getCityCode())
                    .bind("qtDays", Integer.valueOf(weatherRequest.getQtDays()))
                    .mapToBean(WeatherEntity.class)
                    .list()
        )).flatMapIterable(e -> e);
    }

    public Mono<WeatherEntity> findOneWeather(String id) {
        return async(() -> jdbi.inTransaction(handle ->
            handle.createQuery(sqlLocator.locate("sql.findOne-weather"))
                    .bind("id", id)
                    .mapToBean(WeatherEntity.class)
                    .one()
        ));
    }

    public Mono<WeatherEntity> insertWeather(WeatherRequest weatherRequest) {
        weatherRequest.setId(UUID.randomUUID().toString());

        WeatherEntity weatherEntity = new WeatherMapper().entitytoRequest(weatherRequest);

        return async(() -> jdbi.inTransaction(handle -> {
            int count = handle.createUpdate(sqlLocator.locate("sql.save-weather"))
                    .bindBean(weatherEntity)
                    .execute();

            if (count > 0) {
                return weatherEntity;
            } else {
                return null;
            }
        }));
    }

    public Mono<WeatherEntity> updateWeather(WeatherRequest weatherRequest) {
        WeatherEntity weatherEntity = new WeatherMapper().entitytoRequest(weatherRequest);

        return async(() -> jdbi.inTransaction(handle -> {
            int count = handle.createUpdate(sqlLocator.locate("sql.update-weather"))
                    .bindBean(weatherEntity)
                    .execute();

            if (count > 0) {
                return weatherEntity;
            } else {
                return null;
            }

        }));
    }

    public Mono<Integer> deleteWeather(String id) {
        return async(() -> jdbi.inTransaction(handle ->
                handle.createUpdate(sqlLocator.locate("sql.delete-weather"))
                        .bind("id", id)
                        .execute()
        ));
    }

    private String setWhere(WeatherRequest weatherRequest) {
        StringBuilder where = new StringBuilder("WHERE ");

        if (!weatherRequest.getDate().isEmpty()) {
            where.append("date = '").append(weatherRequest.getDate()).append("'");
        }

        if (!weatherRequest.getMaximumTemperature().isEmpty()) {
            if (where.length() > 6) {
                where.append(" and ");
            }
            where.append("maximumTemperature = ").append(weatherRequest.getMaximumTemperature());
        }

        if (!weatherRequest.getMinimumTemperature().isEmpty()) {
            if (where.length() > 6) {
                where.append(" and ");
            }
            where.append("minimumTemperature = ").append(weatherRequest.getMinimumTemperature());
        }

        if (where.length() > 6) {
            where.append(" and ");
        }

        where.append("isdelete <> 1");

        return where.toString();
    }
}
