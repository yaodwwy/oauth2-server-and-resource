package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.RoleEntity;
import cn.adbyte.oauth.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Adam Yao on 2018/5/17.
 */
@Service
public class RoleService implements IRoleService {
    @Override
    public List<RoleEntity> listUserRoles(Integer id) {
        return null;
    }
}
