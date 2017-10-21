package top.vetoer.entity;

import java.util.Date;

public class SuccessKilled {
    private long seckillId;

    private long userId;

    private short state;

    private Date createTime;

    //变通,
    //多对一,
    private Seckill seckill;

    public SuccessKilled() {
    }

    public SuccessKilled(long seckillId, long userId, short state, Date createTime) {

        this.seckillId = seckillId;
        this.userId = userId;
        this.state = state;
        this.createTime = createTime;
    }

    public long getSeckillId() {

        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userId=" + userId +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}
