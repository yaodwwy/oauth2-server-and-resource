package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.ResourceEntity;
import cn.adbyte.oauth.service.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Adam Yao on 2018/5/17.
 */
@Service
public class PermissionService implements IPermissionService {
    @Override
    public List<ResourceEntity> getPermissionsByRoleId(Integer id) {
        return null;
    }
}
