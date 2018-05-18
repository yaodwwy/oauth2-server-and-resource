package cn.adbyte.oauth.service;

import cn.adbyte.oauth.entity.UserEntity;

public interface IUserService {
    UserEntity findByUsernameFetchRoles(String username);
}
