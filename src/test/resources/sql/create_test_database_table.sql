CREATE database `ismart_demo_test` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

use ismart_demo_test;

CREATE TABLE `customer` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `remark` text,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

