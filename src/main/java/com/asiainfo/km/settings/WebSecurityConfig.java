package com.asiainfo.km.settings;

import com.asiainfo.km.controller.Vcs$RepoAuthImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Vcs$RepoAuthImp provider;//自定义验证

    public WebSecurityConfig(Vcs$RepoAuthImp provider) {
        this.provider = provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/druid/**")
                .and()
                .authorizeRequests()
//                  .antMatchers("/druid/**").hasAuthority("USER")//user角色访问权限
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successForwardUrl("/")
                .and()
                    .logout()
                    .permitAll();
    }

    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers( "/**/*.css","/**/*.js","/**/*.jpg","/**/*.png","/**/*.map");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }
}
