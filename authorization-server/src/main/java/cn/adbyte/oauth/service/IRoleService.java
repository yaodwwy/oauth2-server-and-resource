package cn.adbyte.oauth.service;

import cn.adbyte.oauth.entity.RoleEntity;

import java.util.List;

/**
 * Created by Adam Yao on 2018/5/17.
 */
public interface IRoleService {
    List<RoleEntity> listUserRoles(Integer id);
}
