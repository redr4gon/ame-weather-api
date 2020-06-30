select weather, maximumTemperature, minimumTemperature, cityName, cityCode, date
from weather
where id = :id
and deleted_at is null;