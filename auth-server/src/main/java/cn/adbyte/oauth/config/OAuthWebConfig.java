package cn.adbyte.oauth.config;

import cn.adbyte.oauth.security.LoginSuccessHandler;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

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

    private static final String[] Ignores = {
            "/oauth/**", "/login","/static/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().headers().disable()
                .authorizeRequests()
                .antMatchers(Ignores).permitAll()
                .anyRequest().authenticated()//除以上路径都需要验证
                    .and()
                //登录页面url 配置登录成功后调用的方法
                .formLogin().loginPage("/login")
                .permitAll().successHandler(loginSuccessHandler())
                    .and()
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                //注销登录  默认支持 销毁session并且清空配置的rememberMe()认证 跳转登录页 或配置的注销成功页面
                .logout().deleteCookies("remove").invalidateHttpSession(false).logoutUrl("/logout")
                .logoutSuccessUrl("/")
                    .and()
                //配置http认证
                .httpBasic()
                    .and()
                //当用户进行重复登录时  强制销毁前一个登录用户  配置此应用必须添加Listener  HttpSessionEventPublisher
                .sessionManagement().maximumSessions(1).expiredUrl("/login?expired");

    }

    private AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favor.ico");
    }

}
