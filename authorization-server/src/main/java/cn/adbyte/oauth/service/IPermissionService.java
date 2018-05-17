package cn.adbyte.oauth.service;

import cn.adbyte.oauth.entity.ResourceEntity;

import java.util.List;

/**
 * Created by Adam Yao on 2018/5/17.
 */
public interface IPermissionService {
    List<ResourceEntity> getPermissionsByRoleId(Integer id);
}
