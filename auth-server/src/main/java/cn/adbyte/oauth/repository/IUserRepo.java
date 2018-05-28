package cn.adbyte.oauth.repository;

import cn.adbyte.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepo extends JpaRepository<UserEntity, Integer> {

    @Query("select u from UserEntity u left join fetch u.role r left join fetch r.resource where u.username =?1")
    UserEntity findByUsernameFetchRolesAndResource(String username);
}
