CREATE TABLE `wave` (
  `id` varchar(45) NOT NULL,
  `cityCode` int(11) DEFAULT NULL,
  `cityName` varchar(255) DEFAULT NULL,
  `updatedAt` timestamp NOT NULL DEFAULT NOW(),
  `date` varchar(20) NULL DEFAULT NULL,
  `agitation` ENUM('fr','mo','fo'),
  `height` double DEFAULT NULL,
  `direction` char(2) DEFAULT NULL,
  `drift` double DEFAULT NULL,
  `driftDir` varchar(10) DEFAULT NULL,
  `period` ENUM('M','T','N'),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;