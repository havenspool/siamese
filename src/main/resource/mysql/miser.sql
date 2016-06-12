CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `passwd` varchar(32) NOT NULL,
  `registerTime` int(15) NOT NULL DEFAULT '0',
  `loginTime` int(15) NOT NULL DEFAULT '0',
  `channel` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户表' AUTO_INCREMENT=1001 ;
