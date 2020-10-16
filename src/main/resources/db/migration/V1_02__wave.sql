CREATE TABLE `wave` (
  `id` varchar(45) NOT NULL,
  `cityName` varchar(255) DEFAULT NULL,
  `uf` varchar(2) DEFAULT NULL,
  `cityCode` int(11) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;