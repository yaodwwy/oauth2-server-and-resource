package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.UserEntity;
import cn.adbyte.oauth.repository.IUserRepo;
import cn.adbyte.oauth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepo iUserRepo;

    @Override
    public UserEntity findByUsernameFetchRoles(String username) {
        return iUserRepo.findByUsernameFetchRolesAndResource(username);
    }
}
