UPDATE weather SET maximumTemperature = :maximumTemperature,
                   minimumTemperature = :minimumTemperature,
                   weather = :weather,
                   cityCode = :cityCode,
                   cityName = :cityName,
                   date = :date,
                   updatedAt = CURRENT_TIMESTAMP()
WHERE ID =:id;


