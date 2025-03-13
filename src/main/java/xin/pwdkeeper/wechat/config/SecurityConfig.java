package xin.pwdkeeper.wechat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xin.pwdkeeper.wechat.util.JwtTokenUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 禁用CSRF防护，适用于无状态的API场景。
     * 配置请求授权规则：允许对"/"和"/verifyCode/generateVerifyCode、/verifyCode/verificationCode"路径的匿名访问，其他所有请求需认证。
     * 自定义异常处理逻辑：当未认证用户访问受保护资源时，返回JSON格式的401 Unauthorized响应。
     * 在Spring Security过滤器链中添加自定义JWT认证过滤器，优先于用户名密码认证过滤器执行。
     * 设置会话管理策略为无状态，不创建HTTP会话。
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                //未授权允许被访问的路径，默认情况下，Spring Security会阻止所有未授权访问
                .antMatchers("/", "/verifyCode/generateVerifyCode","/verifyCode/verificationCode").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"code\":1,\"data\":null,\"msg\":\"你未获得授权，请先拿到授权\"}");
                    writer.flush();
                })
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(userDetailsService,jwtTokenUtil), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}