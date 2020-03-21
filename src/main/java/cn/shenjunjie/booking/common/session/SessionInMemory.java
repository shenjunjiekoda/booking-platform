package cn.shenjunjie.booking.common.session;

import org.apache.shiro.session.Session;

import java.util.Date;

/**
 * @author Junjie.Shen
 * @version 1.0
 * @date 2020/3/9 22:05
 */
public class SessionInMemory {
    private Session session;
    private Date createTime;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

