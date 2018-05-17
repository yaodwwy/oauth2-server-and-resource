package cn.adbyte.oauth.common;

import cn.adbyte.oauth.common.utils.SecurityUserUtils;
import cn.adbyte.oauth.entity.UserEntity;
import cn.gomro.core.bizes.member.entity.UserEntity;
import cn.gomro.core.commons.constants.SystemConstants;
import cn.gomro.core.commons.exceptions.BaseException;
import cn.gomro.core.commons.exceptions.ErrorCode;
import cn.gomro.core.commons.logger.entity.SysLogEntity;
import cn.gomro.core.commons.utils.SecurityUserUtils;
import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Adam.yao on 2017/11/2.
 */
public class BaseRepositoryImpl<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final Class<T> domainClass;
    private EntityManager em;

    @SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection"})
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.em = entityManager;
    }

    @Override
    public boolean support(String modelType) {
        return domainClass.getName().equals(modelType);
    }

    @Override
    @Transactional
    public <S extends T> S save(S entity) {
        if (null == entity.getDel()) {
            entity.setDel(false);
        }
        if (null == entity.getOperator()) {
            Integer operatorId = -1;
            UserEntity securityMember = null;
            try {
                securityMember = SecurityUserUtils.getSecurityMember();
            } catch (Exception ignored) {
            }
            if (securityMember != null) {
                operatorId = securityMember.getId();
            }
            entity.setOperator(operatorId);
        }
        if (null == entity.getTime()) {
            entity.setTime(Date.from(Instant.now()));
        }
        S save = super.save(entity);
        this.saveLog(LogLevel.INFO, "save()", "审计日志", entity);
        return save;
    }

    @Override
    @Transactional
    public void saveLog(LogLevel Level, String method, String description, T entity) {
        try {
            Integer operator;
            try {
                operator = SecurityUserUtils.getCurrentMemberID();
            } catch (Exception e) {
                operator = -1;
            }
            if (description == null) {
                description = "";
            }
            SysLogEntity saveLog = new SysLogEntity(false, null, Date.from(Instant.now()), operator,
                    description, method, Level.toString(), entity.toString());
            em.merge(saveLog);
            em.flush();
        } catch (Exception e){
            System.out.println("---------WARN-------->>>" + e.getMessage() + "<<<---------WARN--------");
        }

    }

    @Override
    @Transactional
    public List<T> saveAll(List<T> entities) {
        return super.saveAll(entities);
    }


    @Transactional
    public T update(T entity) {
        entity.setLast(new Date());
        T t = super.saveAndFlush(entity);
        this.saveLog(LogLevel.INFO, "update()", "审计日志", entity);
        return t;
    }

    @Override
    public void deleteById(ID id) {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Override
    public void delete(T entity) {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Override
    public void deleteAll() {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Override
    public void deleteAllInBatch() {
        throw new BaseException(ErrorCode.RealDelException);
    }

    @Transactional
    public boolean remove(ID id) {
        T one = findOne(id);
        if (null != one) {
            one.setDel(true);
            this.update(one);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean removeAll(List<ID> id) {
        for (ID index : id) {
            if (!remove(index)) {
                throw new BaseException(ErrorCode.NO_DATA);
            }
        }
        return true;
    }


    public boolean exists(ID id) {
        return super.existsById(id);
    }

    @Override
    public T findOne(ID id) {
        if (id == null) {
            throw new BaseException(ErrorCode.paraNotRight);
        }
        Optional<T> optionalT = super.findById(id);
        if (optionalT.isPresent()) {
            T t = optionalT.get();
            if (t.getDel() != null && !t.getDel()) {
                return t;
            }
        }
        return null;
    }

    public long count(Example example) {
        return super.count(example);
    }

    public long count(Specification spec) {
        return super.count(spec);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        List<T> all = super.findAllById(ids);
        List<T> result = new ArrayList<>();
        for (T t : all) {
            if (!t.getDel()) {
                result.add(t);
            }
        }
        return result;
    }

    public List<T> findAll(Example example) throws BaseException {
        List<T> all = super.findAll(example);
        if (all.size() > SystemConstants.MAX_SIZE) {
            all.clear();
            throw new BaseException(ErrorCode.ReturnSizeTooLargeException);
        }
        return all;
    }

    public List<T> findAll(Specification spec) {
        List<T> all = super.findAll(spec);
        if (all.size() > SystemConstants.MAX_SIZE) {
            all.clear();
            throw new BaseException(ErrorCode.ReturnSizeTooLargeException);
        }
        return all;
    }

    public List<T> findAll(Specification spec, Sort sort) {
        List<T> all = super.findAll(spec, sort);
        if (all.size() > SystemConstants.MAX_SIZE) {
            all.clear();
            throw new BaseException(ErrorCode.ReturnSizeTooLargeException);
        }
        return all;
    }

    public Page<T> findAll(Example example, Pageable pageable) {
        if (pageable.getPageSize() > SystemConstants.MAX_SIZE) {
            throw new BaseException(ErrorCode.ReturnSizeTooLargeException);
        }
        return super.findAll(example, pageable);
    }


    public Page<T> findAll(Specification spec, Pageable pageable) {
        if (pageable.getPageSize() > SystemConstants.MAX_SIZE) {
            throw new BaseException(ErrorCode.ReturnSizeTooLargeException);
        }
        return super.findAll(spec, pageable);
    }
}
