package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.UserEntity;
import cn.adbyte.oauth.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * Created by Adam Yao on 2018/5/17.
 */
@Service
public class UserService implements IUserService {

    @Override
    public UserEntity findByUsername(String username) {
        return null;
    }
}
