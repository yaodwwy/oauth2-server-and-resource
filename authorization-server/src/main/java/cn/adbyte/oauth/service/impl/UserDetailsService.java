package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.ResourceEntity;
import cn.adbyte.oauth.entity.RoleEntity;
import cn.adbyte.oauth.entity.UserEntity;
import cn.adbyte.oauth.service.IPermissionService;
import cn.adbyte.oauth.service.IRoleService;
import cn.adbyte.oauth.service.IUserDetailsService;
import cn.adbyte.oauth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam Yao on 2018/5/17.
 */
@Service
public class UserDetailsService implements IUserDetailsService {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IPermissionService iPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = iUserService.findByUsername(username);
        if (user == null) {
            System.out.println("用户:" + username + ",不存在!");
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        boolean enabled = true; // 可用性 :true:可用 false:不可用
        boolean accountNonExpired = true; // 过期性 :true:没过期 false:过期
        boolean credentialsNonExpired = true; // 有效性 :true:凭证有效 false:凭证无效
        boolean accountNonLocked = true; // 锁定性 :true:未锁定 false:已锁定
        List<RoleEntity> roleValues = iRoleService.listUserRoles(user.getId());
        for (RoleEntity role : roleValues) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            List<ResourceEntity> permissionList = iPermissionService.getPermissionsByRoleId(role.getId());
            for (ResourceEntity menu : permissionList) {
                String url = menu.getUrl();
                if (menu.getParent() != null) {
                    url = menu.getParent().getUrl() + "/" + menu.getUrl();
                }
                GrantedAuthority authority = new SimpleGrantedAuthority(url);
                grantedAuthorities.add(authority);
            }
        }
        user = new UserEntity(user.getUsername(), user.getPassword(),
                enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
        return user;
    }
}
