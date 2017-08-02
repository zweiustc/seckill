package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SucccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;


/**
 * Created by Administrator on 2017/7/25.
 */
//@Components @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //注入service
    @Autowired //ReSource, @Inject
    private SeckillDao seckillDao;

    @Autowired
    private SucccessKilledDao succcessKilledDao;

    //用于加密url
    private final String slat = "dfdsafdasfdsgHJKL&*";

    public List<Seckill> getSeckillList() {
        return this.seckillDao.queryAll(1,4);
    }

    public Seckill getById(long seckillId) {
        return this.seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = this.seckillDao.queryById(seckillId);
        if (null == seckill) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(),
                    endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + '/' + this.slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /*
    * 使用注解控制事务方法的优点
    * 1、开发团队达成一致约定，明确标注事务方法的编程风格
    * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作 RPC、HTTP请求 或者 剥离到事务方法外
    * 3、不是所有的方法都需要事务，如只有一条修改操作，或者只读操作，不需要事务控制（参考行级锁）*/
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if (null == md5 || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite, fail md5 check");
        }
//        秒杀逻辑：减库存加 记录购买行为
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is closed");
            } else {
                int insertCount = succcessKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeat");
                } else {
                    SuccessKilled successKilled = this.succcessKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            this.logger.error(e.getMessage(), e);
            //所有编译期异常，转化为运行期异常
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }
}

