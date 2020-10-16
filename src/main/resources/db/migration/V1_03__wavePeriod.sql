CREATE TABLE `waveperiod` (
  `id` varchar(45) NOT NULL,
  `period` varchar(20) DEFAULT NULL,
  `agitation` varchar(45) DEFAULT NULL,
  `height` decimal(11,2) DEFAULT NULL,
  `direction` varchar(3) DEFAULT NULL,
  `wind` decimal(11,2) DEFAULT NULL,
  `windDirection` varchar(3) DEFAULT NULL,
  `idwave` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;