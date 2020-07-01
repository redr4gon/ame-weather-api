select weather, maximumTemperature, minimumTemperature, cityName, cityCode, date
from weather
where deleted_at is null and :conditionals
order by date, cityName, minimumTemperature, maximumTemperature;