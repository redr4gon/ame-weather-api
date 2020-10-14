UPDATE weather SET maximumTemperature = :maximumTemperature, minimumTemperature = :minimumTemperature,
 weather = :weather, cityCode= :cityCode, cityName = :cityName where id = :id;