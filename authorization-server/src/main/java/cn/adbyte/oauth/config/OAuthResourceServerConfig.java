package cn.adbyte.oauth.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Created by Adam Yao on 2018/5/17.
 */
public class OAuthResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
