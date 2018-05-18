package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.RoleEntity;
import cn.adbyte.oauth.repository.IRoleRepo;
import cn.adbyte.oauth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepo iRoleRepo;
}
