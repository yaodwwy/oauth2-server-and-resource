package cn.adbyte.oauth.common;

import org.springframework.boot.logging.LogLevel;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adam.yao on 2017/10/27.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    boolean support(String modelType);

    <S extends T> S save(S entity);

    void saveLog(LogLevel Level, String method, String description, T entity);

    List<T> saveAll(List<T> entitys);

    T update(T entity);

    boolean remove(ID id);

    boolean removeAll(List<ID> id);

    T findOne(ID id);

    <S extends T> long count(Example<S> example);

    <S extends T> long count(Specification<S> spec);

    List<T> findAll(Iterable<ID> ids);

    <S extends T> List<S> findAll(Example<S> example);

    <S extends T> List<S> findAll(Specification<S> spec);

    <S extends T> List<S> findAll(Example<S> example, Sort sort);

    <S extends T> List<S> findAll(Specification<S> spec, Sort sort);

    <S extends T> Page<S> findAll(Example<S> example, Pageable pageable);

    <S extends T> Page<S> findAll(Specification<S> spec, Pageable pageable);

}