CREATE TABLE `waves_weather` (
  `id` varchar(45) NOT NULL,
  `agitation` varchar(45) DEFAULT NULL,
  `hight` DOUBLE DEFAULT NULL,
  `direction` varchar(45) DEFAULT NULL,
  `wind` DOUBLE DEFAULT NULL,
  `directionWind` varchar(45) DEFAULT NULL,
  `cityCode` int(11) DEFAULT NULL,
  `cityName` varchar(255) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;