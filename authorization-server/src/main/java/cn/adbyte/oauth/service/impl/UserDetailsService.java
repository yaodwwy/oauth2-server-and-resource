package cn.adbyte.oauth.service.impl;

import cn.adbyte.oauth.entity.ResourceEntity;
import cn.adbyte.oauth.entity.RoleEntity;
import cn.adbyte.oauth.entity.UserEntity;
import cn.adbyte.oauth.service.IUserDetailsService;
import cn.adbyte.oauth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsService implements IUserDetailsService {

    @Autowired
    private IUserService iUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = iUserService.findByUsernameFetchRoles(username);
        if (user == null) {
            System.out.println("用户:" + username + ",不存在!");
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<RoleEntity> roleValues = user.getRole();
        for (RoleEntity role : roleValues) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            Set<ResourceEntity> permissionList = role.getResource();
            for (ResourceEntity menu : permissionList) {
                String url = menu.getUrl();
                if (menu.getParent() != null) {
                    url = menu.getParent().getUrl() + "/" + menu.getUrl();
                }
                GrantedAuthority authority = new SimpleGrantedAuthority(url);
                grantedAuthorities.add(authority);
            }
        }
        return user;
    }
}
