package br.com.amedigital.weather.api.controller.response;

public class WaveResponse {

    private Integer code;
    private String name;
    private String state;
    private String updatedAt;
    private Wave morning;
    private Wave afternoon;
    private Wave night;

    public static final class Wave {

        private String date;
        private String agitation;
        private Double height;
        private String direction;
        private Double drift;
        private String driftDirection;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAgitation() {
            return agitation;
        }

        public void setAgitation(String agitation) {
            this.agitation = agitation;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }

        public Double getDrift() {
            return drift;
        }

        public void setDrift(Double drift) {
            this.drift = drift;
        }

        public String getDriftDirection() {
            return driftDirection;
        }

        public void setDriftDirection(String driftDirection) {
            this.driftDirection = driftDirection;
        }

        @Override
        public String toString() {
            return "Wave{" +
                    "date='" + date + '\'' +
                    ", agitation='" + agitation + '\'' +
                    ", height=" + height +
                    ", direction='" + direction + '\'' +
                    ", drift=" + drift +
                    ", driftDirection='" + driftDirection + '\'' +
                    '}';
        }
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Wave getMorning() {
        return morning;
    }

    public void setMorning(Wave morning) {
        this.morning = morning;
    }

    public Wave getAfternoon() {
        return afternoon;
    }

    public void setAfternoon(Wave afternoon) {
        this.afternoon = afternoon;
    }

    public Wave getNight() {
        return night;
    }

    public void setNight(Wave night) {
        this.night = night;
    }

    @Override
    public String toString() {
        return "WaveResponse{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", morning=" + morning +
                ", afternoon=" + afternoon +
                ", night=" + night +
                '}';
    }
}
