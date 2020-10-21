SELECT *
  FROM weather w
 WHERE w.cityCode = :cityCode
   and date >= CURRENT_DATE()
   and isdelete <> 1
 ORDER BY date
LIMIT :qtDays;