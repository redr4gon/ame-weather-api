update weather
set deleted_at = now()
where id = :id;
