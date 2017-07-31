package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 * 方法定义粒度，参数，返回类型（返回类型及异常）
 */
public interface SeckillService {
    /*查询所有秒杀记录*/
    List<Seckill> getSeckillList();

    /*查询单个秒杀记录*/
    Seckill getById(long seckillId);

    /*秒杀开始时输出接品地址
    * 否则输出系统时间和秒杀时间
    * @param seckillId
    */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException,RepeatKillException,SeckillCloseException;
}
