package br.com.amedigital.weather.api.repository;

import br.com.amedigital.weather.api.entity.WaveEntity;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class WaveRepository extends BaseRepository {

    public WaveRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        super(jdbi, jdbcScheduler);
    }

    public Flux<WaveEntity> save(List<WaveEntity> entities) {
        List<WaveEntity> waveEntities = new ArrayList<>();

        return async(() -> jdbi.inTransaction(handle -> {
            entities.stream().forEach(entity -> {
                entity.setId(UUID.randomUUID().toString());

                handle.createUpdate(sqlLocator.locate("sql.save-wave"))
                        .bindBean(entity)
                        .execute();

                waveEntities.add(entity);
            });

            return waveEntities;
        })).flatMapIterable(e -> e);
    }

}
