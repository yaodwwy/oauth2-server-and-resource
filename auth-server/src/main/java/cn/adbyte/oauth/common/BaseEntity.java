package cn.adbyte.oauth.common;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    /**
     * 是否删除
     */
    protected Boolean del = false;
    /**
     * 最后修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)

    protected Date last;
    /**
     * 创建时间
     */

    @Temporal(TemporalType.TIMESTAMP)
    protected Date time;
    /**
     * 操作人
     */
    protected Integer operator;

    public BaseEntity() {
    }

    public BaseEntity(Boolean del, Date last, Date time, Integer operator) {
        this.del = del;
        this.last = last;
        this.time = time;
        this.operator = operator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }

    public Date getLast() {
        return last;
    }

    public void setLast(Date last) {
        this.last = last;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }
}
