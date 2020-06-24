CREATE TABLE `weather` (
  `id` varchar(45) NOT NULL,
  `maximumTemperature` int(11) DEFAULT NULL,
  `minimumTemperature` int(11) DEFAULT NULL,
  `weather` varchar(45) DEFAULT NULL,
  `cityCode` int(11) DEFAULT NULL,
  `cityName` varchar(255) DEFAULT NULL,
  `date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


