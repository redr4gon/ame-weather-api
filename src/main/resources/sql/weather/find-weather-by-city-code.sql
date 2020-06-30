select id, weather, maximumTemperature, minimumTemperature, cityName, cityCode, date from weather
where cityCode = :cityCode
and date between  DATE_SUB(now(), INTERVAL 1 DAY) and DATE_ADD(now(), INTERVAL :plusDays DAY)
and deleted_at is null
order by date, cityName, minimumTemperature, maximumTemperature;