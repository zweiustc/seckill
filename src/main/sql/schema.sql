CREATE DATABASE seckill;
use seckill;

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
`name` VARCHAR(120) NOT NUll COMMENT '商品名称',
`number` int(11) NOT NULL COMMENT '库存数量',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`start_time` TIMESTAMP NOT NULL COMMENT '开启秒杀时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',

PRIMARY KEY(seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

---初始化数据
insert into seckill(name,number,start_time, end_time)
values
  ('1000元秒杀iphone7', 3000, '2016-10-01 00:00:00', '2016-10-2 00:00:00'),
  ('2000元秒杀ipad2', 2000, '2016-10-01 00:00:00', '2016-10-2 00:00:00'),
  ('1000元秒杀miphone', 1000, '2016-10-01 00:00:00', '2016-10-2 00:00:00');

CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT  '秒杀商品ID',
`user_phone` bigint NOT NULL COMMENT '用户手机号码',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标识 -1：无效， 0成功， 1：已付款',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id, user_phone),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

mysql -uzhangwei -pzhangwei -h47.93.234.248;