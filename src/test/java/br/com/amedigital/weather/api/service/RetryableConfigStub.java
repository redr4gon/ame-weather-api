package br.com.amedigital.weather.api.service;

import br.com.amedigital.weather.api.config.RetryableConfig;

public class RetryableConfigStub extends RetryableConfig {

    public RetryableConfigStub() {
        this.setMaxRetries(1);
        this.setMinDuration(5);
        this.setMaxDuration(10);
    }
}
