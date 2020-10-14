package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.entity.WavesWeatherEntity;
import br.com.amedigital.weather.api.entity.WeatherEntity;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class WavesWeatherRepository extends BaseRepository {

    public WavesWeatherRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Flux<WavesWeatherEntity> save(List<WavesWeatherEntity> entities) {

        List<WavesWeatherEntity> wavesWeatherEntities = new ArrayList<>();

        return async(() -> jdbi.inTransaction(handle -> {

            entities.stream().forEach(entity -> {
                entity.setId(UUID.randomUUID().toString());
                handle.createUpdate(sqlLocator.locate("sql.save-waves-weather"))
                        .bindBean(entity)
                        .execute();
                wavesWeatherEntities.add(entity);
            });

            return wavesWeatherEntities;
        })).flatMapIterable(e -> e);
    }
}
