package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.controller.request.WeatherRequest;
import br.com.amedigital.weather.api.controller.response.WeatherResponse;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.UUID;

@Repository
public class WeatherRepository extends BaseRepository {

    public WeatherRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Flux<WeatherResponse> findAllWeather(WeatherRequest weatherRequest) {
        return asyncFlux(() -> jdbi.withHandle(handle -> {
                    String query = sqlLocator.locate("sql.weather.find-all-weather");

                    StringBuilder conditionals = new StringBuilder();

                    if (weatherRequest.getDate() != null) {
                        conditionals
                                .append("and date = ")
                                .append(weatherRequest.getDate());
                    }

                    if (weatherRequest.getMinimumTemperature() != null) {
                        conditionals
                                .append(" and minimumTemperature >= ")
                                .append(weatherRequest.getMinimumTemperature());
                    }

                    if (weatherRequest.getMaximumTemperature() != null) {
                        conditionals
                                .append("and maximumTemperature <= ")
                                .append(weatherRequest.getMaximumTemperature());
                    }

                    query = query.replace("and :conditionals", conditionals.toString());

                    return handle.createQuery(query)
                            .mapTo(WeatherResponse.class)
                            .list()
                            .stream();
                }
        ));
    }

    public Mono<WeatherEntity> save(WeatherEntity entity) {
        return async(() -> jdbi.inTransaction(handle -> {

            entity.setId(UUID.randomUUID().toString());
            handle.createUpdate(sqlLocator.locate("sql.weather.save-weather"))
                    .bindBean(entity)
                    .execute();

            return entity;
        }));
    }

    public Mono<WeatherResponse> findById(String weatherId) {
        return async(() -> jdbi.withHandle(handle ->

                handle.createQuery(sqlLocator.locate("sql.weather.find-weather-by-id"))
                        .bind("id", weatherId)
                        .mapTo(WeatherResponse.class)
                        .findFirst()

        )).flatMap(Mono::justOrEmpty);
    }

    public Mono<Void> update(WeatherEntity weatherEntity) {
        return async(() -> jdbi.inTransaction(handle -> {

            handle.createUpdate(sqlLocator.locate("sql.weather.update-weather-by-id"))
                    .bindBean(weatherEntity)
                    .execute();

            return null;
        }));
    }

    public Mono<Void> delete(String id) {
        return async(() -> jdbi.inTransaction(handle -> {

            handle.createUpdate(sqlLocator.locate("sql.weather.delete-weather-by-id"))
                    .bind("id", id)
                    .execute();

            return null;
        }));
    }

    public Flux<WeatherEntity> findByCityCode(Integer cityCode, Integer plusDays) {
        return asyncFlux(() -> jdbi.withHandle(handle -> handle
                .createQuery(sqlLocator.locate("sql.weather.find-weather-by-city-code"))
                .bind("cityCode", cityCode)
                .bind("plusDays", plusDays)
                .mapTo(WeatherEntity.class)
                .list()
                .stream()
        ));
    }
}
