package cn.adbyte.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()//确保我们应用中的所有请求都需要用户被认证
                .and()
                .requestMatchers().antMatchers("/oauth/**")
                .and()
                .formLogin()//允许用户进行基于表单的认证
                .loginPage( "/login")//指定了登录页面的位置
                .permitAll()//允许所有用户访问这个页面
                .and()
                .httpBasic();//允许用户使用HTTP基本验证进行认证
//        http.csrf().disable();
//        http.requestMatchers().antMatchers("/oauth/**")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/oauth/**").authenticated();
    }

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

}
