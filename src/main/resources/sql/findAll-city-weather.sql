SELECT *
  FROM weather w
 WHERE w.cityCode = :cityCode
   and date >= CURRENT_DATE()
 ORDER BY date
LIMIT :qtDays;