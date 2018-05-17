package cn.adbyte.oauth.service;

import cn.adbyte.oauth.entity.UserEntity;

/**
 * Created by Adam Yao on 2018/5/17.
 */
public interface IUserService {
    UserEntity findByUsername(String username);
}
