INSERT INTO weather (id, maximumTemperature, minimumTemperature, weather, cityCode, cityName, date, createdAt)
values (:id, :maximumTemperature, :minimumTemperature, :weather, :cityCode, :cityName, :date, CURRENT_TIMESTAMP());


