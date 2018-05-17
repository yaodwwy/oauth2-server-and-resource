package cn.adbyte.oauth.common.utils;

import cn.adbyte.oauth.common.exception.ErrorCode;
import cn.adbyte.oauth.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Adam.yao on 2017/11/10.
 */
public class SecurityUserUtils {
    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserEntity getSecurityMember() throws Exception {
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
        throw new Exception("");
    }

    /**
     * 获取当前登录用户ID
     *
     * @return id
     */
    public static Integer getCurrentMemberID() {
        return getSecurityMember().getId();
    }

    /**
     * 获取当前登录用户的组织ID
     *
     * @return id
     */
    public static Integer getCurrentOrgID() {
        return getSecurityMember().getOrgID();
    }

    /**
     * 获取当前登录用户的部门ID
     *
     * @return id
     */
    public static Integer getCurrentDeptID() {
        return getSecurityMember().getDeptID();
    }

}
