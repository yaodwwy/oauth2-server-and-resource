package cn.adbyte.oauth.service;

import cn.adbyte.oauth.entity.ResourceEntity;

import java.util.List;

public interface IPermissionService {
    List<ResourceEntity> getPermissionsByRoleId(Integer id);
}
