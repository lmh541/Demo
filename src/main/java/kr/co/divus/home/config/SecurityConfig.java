package kr.co.divus.home.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Autowired
    private DataSource dataSource;
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
            //.cors().disable()
            .csrf().disable()
            .formLogin().disable()
            .headers().frameOptions().disable()
        .and()
            .authorizeRequests()
            .antMatchers("/admin/*", "/company/*")
            .authenticated()
            .and()
        .formLogin()
              .loginPage("/admin/login")
              .loginProcessingUrl("/admin/doLogin")
            .usernameParameter("id")
            .passwordParameter("pw")
            .permitAll()
            .defaultSuccessUrl("/admin/")
            .and()
        .logout()
            .logoutUrl("/admin/logout")
            .logoutSuccessUrl("/admin/")
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .permitAll()
            .and() .httpBasic();
    }

    /* // DB 초기화시에만 활성화
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("admin").password(passwordEncoder().encode("divus405")).roles("USER","ADMIN");
    }
    */
    @Autowired
    public void configureInMemoryAuthentication(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication()
        .withUser("admin")
        .password(passwordEncoder().encode("divus405"))
        .roles("USER","ADMIN");
    }

    @Autowired
    public void configureJdbcAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select id, pw , TRUE from tbl_user_info where id=? and is_delete = 0 and user_perm = 0")
            .authoritiesByUsernameQuery("select id, 'ROLE_ADMIN' from tbl_user_info where id=? and is_delete = 0 and user_perm = 0")
            .passwordEncoder(passwordEncoder());
    }
}
