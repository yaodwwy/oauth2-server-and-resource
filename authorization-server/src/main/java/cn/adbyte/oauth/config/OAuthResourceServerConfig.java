package cn.adbyte.oauth.config;

import cn.adbyte.oauth.security.LoginSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * auth-server提供user信息，所以auth-server也是一个Resource Server
 */
@Configuration
@EnableResourceServer
public class OAuthResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String[] only = {
            "/user/**"
    };
    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.antMatcher("/user/**").authorizeRequests()
                .anyRequest().permitAll();
//                .antMatchers(only).permitAll()
//                    .and()
                //登录页面url 配置登录成功后调用的方法
//                .formLogin().loginPage("/login").permitAll().successHandler(loginSuccessHandler())
//                    .and()
//                .authorizeRequests().anyRequest().authenticated();
    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }
}
