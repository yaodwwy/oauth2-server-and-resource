package cn.adbyte.oauth.common.utils;

import cn.adbyte.oauth.common.exception.BaseException;
import cn.adbyte.oauth.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUserUtils {
    /**
     * 获取当前登录用户
     */
    public static UserEntity getSecurityMember() throws BaseException {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context != null) {
            Authentication authentication = context.getAuthentication();
            if (authentication != null) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserEntity) {
                    return (UserEntity) principal;
                }
            }
        }
        throw new BaseException("未能获取到安全上下文中的用户信息");
    }

    /**
     * 获取当前登录用户ID
     */
    public static Integer getCurrentMemberID() throws BaseException {
        return getSecurityMember().getId();
    }

    /**
     * 获取当前登录用户的组织ID
     */
    public static Integer getCurrentOrgID() {
        return getSecurityMember().getOrgID();
    }

    /**
     * 获取当前登录用户的部门ID
     */
    public static Integer getCurrentDeptID() {
        return getSecurityMember().getDeptID();
    }

}
