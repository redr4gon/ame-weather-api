package br.com.amedigital.weather.api.repository;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.locator.ClasspathSqlLocator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class BaseRepository {

    public final Jdbi jdbi;
    public final ClasspathSqlLocator sqlLocator;
    private final Scheduler jdbcScheduler;

    public BaseRepository(Jdbi jdbi, Scheduler jdbcScheduler) {
        this.jdbi = jdbi;
        this.jdbcScheduler = jdbcScheduler;
        this.sqlLocator = ClasspathSqlLocator.create();
    }

    public <T> Mono<T> async(Callable<T> supplier) {
        return Mono.fromCallable(supplier)
                .subscribeOn(jdbcScheduler)
                .publishOn(Schedulers.parallel());
    }

    public <T> Flux<T> asyncFlux(Supplier<Stream<? extends T>> streamSupplier) {
        return Flux.fromStream(streamSupplier)
                .subscribeOn(jdbcScheduler)
                .publishOn(Schedulers.parallel());
    }

}
