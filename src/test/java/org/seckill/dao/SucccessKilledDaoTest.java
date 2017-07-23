package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SucccessKilledDaoTest {

    @Resource
    private SucccessKilledDao succcessKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id =1001L;
        long phone = 17718382025L;
        int insertCount = succcessKilledDao.insertSuccessKilled(id, phone);
        System.out.println("InsertCount=" + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id =1001L;
        long phone = 17718382025L;
        SuccessKilled successKilled = succcessKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }

}