package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/7/16.
 */
public interface SeckillDao {


    /**
     * 查询
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * 根据偏移量查询总的列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响多行，则返回值为n
     * 插入多行
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
}
