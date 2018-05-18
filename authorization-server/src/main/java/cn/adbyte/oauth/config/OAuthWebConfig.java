package cn.adbyte.oauth.config;

import cn.adbyte.oauth.service.IUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class OAuthWebConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IUserDetailsService iUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(iUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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

//        http.oauth2Login().requestMatchers()
//                .antMatchers("/api/**","/oauth/**")
//                .and()
//            .authorizeRequests()
//                .antMatchers("/**")
//                .httpBasic();

//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().and()
//                .csrf().disable()
//                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ico");
    }

}
