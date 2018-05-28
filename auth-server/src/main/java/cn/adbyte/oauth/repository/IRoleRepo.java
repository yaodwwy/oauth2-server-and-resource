package cn.adbyte.oauth.repository;

import cn.adbyte.oauth.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Adam Yao on 2018/5/18.
 */
public interface IRoleRepo extends JpaRepository<RoleEntity, Integer> {
}
