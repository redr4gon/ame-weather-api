UPDATE weather SET deletedAt = CURRENT_TIMESTAMP where id = :id;