update weather set
maximumTemperature = :maximumTemperature,
minimumTemperature = :minimumTemperature,
weather = :weather,
updated_at = now()
where id = :id;