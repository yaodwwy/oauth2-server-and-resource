package cn.adbyte.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .requestMatchers()
                .antMatchers("/**")
                .and()
                .authorizeRequests()
                .filterSecurityInterceptorOncePerRequest(true)
                .antMatchers("/api/**").permitAll()
                .and().headers().frameOptions().disable();
    }
}