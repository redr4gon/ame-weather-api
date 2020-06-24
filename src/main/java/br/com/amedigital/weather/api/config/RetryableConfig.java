package br.com.amedigital.weather.api.config;

public class RetryableConfig {

    public RetryableConfig() {
    }

    public RetryableConfig(Integer maxRetries, Integer minDuration, Integer maxDuration) {
        this.maxRetries = maxRetries;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
    }

    private Integer maxRetries;

    private Integer minDuration;

    private Integer maxDuration;

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }
}
