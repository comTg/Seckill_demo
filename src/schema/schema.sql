-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill_demo;
-- 使用数据库
use seckill_demo;

-- 启用timestamp默认值
-- set @@session.explicit_defaults_for_timestamp=on;

CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT '商品库存ID',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` int NOT NULL COMMENT '库存数量',
  `start_time` TIMESTAMP  NOT NULL COMMENT '秒杀开始时间',
  `end_time`   TIMESTAMP   NOT NULL COMMENT '秒杀结束时间',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据
INSERT into seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iphone6',100,'2017-10-21 00:00:00','2017-10-23 00:00:00'),
  ('800元秒杀ipad',200,'2017-10-21 00:00:00','2017-10-23 00:00:00'),
  ('6600元秒杀mac book pro',300,'2017-10-21 00:00:00','2017-10-23 00:00:00'),
  ('7000元秒杀iMac',400,'2017-10-21 00:00:00','2017-10-23 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息(简化为手机号)
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀商品ID',
  `user_id` BIGINT NOT NULL COMMENT '用户id',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY(seckill_id,user_id),/*联合主键*/
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

-- 用户信息表
create table user(
  `user_id` BIGINT not null AUTO_INCREMENT COMMENT '用户id',
  `phone` BIGINT not null COMMENT '用户手机号码',
  `name` VARCHAR(20) COMMENT '用户名',
  `password` VARCHAR(20) COMMENT '用户密码',
  PRIMARY KEY(user_id),
  KEY idx_phone(phone)
)ENGINE=INNODB DEFAULT CHARSET=utf8 AUTO_INCREMENT=100 COMMENT='用户信息表';

-- SHOW CREATE TABLE seckill;#显示表的创建信息

-- 为什么手写DDL
-- 记录每次上线的DDL修改