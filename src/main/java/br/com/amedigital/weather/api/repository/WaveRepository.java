package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.entity.WaveEntity;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.UUID;

@Repository
public class WaveRepository extends BaseRepository {

    public WaveRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Mono<WaveEntity> save(WaveEntity entities) {

        WaveEntity waveEntities = new WaveEntity();

        return async(() -> jdbi.inTransaction(handle -> {

        entities.setId(UUID.randomUUID().toString());
        handle.createUpdate(sqlLocator.locate("sql.save-wave"))
                .bindBean(entities)
                .execute();
        waveEntities.setId(entities.getId());
        waveEntities.setCityName(entities.getCityName());
        waveEntities.setUf(entities.getUf());
        waveEntities.setCityCode(entities.getCityCode());
        waveEntities.setDate(entities.getDate());
            entities.getPeriods().stream().forEach(period -> {
                period.setId(UUID.randomUUID().toString());
                period.setIdWave(entities.getId());
                handle.createUpdate(sqlLocator.locate("sql.save-wavePeriod"))
                        .bindBean(period)
                        .execute();
                waveEntities.addPeriods(period);
            });

            return waveEntities;
        }));
    }
}
