package club.lzlbog.chatting.config.security;

import club.lzlbog.chatting.config.handler.LoginFailureHandler;
import club.lzlbog.chatting.config.handler.LoginSuccessfulHandler;
import club.lzlbog.chatting.config.handler.LogoutProcessHandler;
import club.lzlbog.chatting.config.handler.LogoutSuccessfulHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author li
 * @version 1.0
 * @date 2019-03-17 11:36
 **/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private static final String LOGIN_PROCESSING_URL = "/auth/login";
    private final LoginSuccessfulHandler successfulHandler;
    private final LoginFailureHandler failureHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final LogoutProcessHandler logoutProcessHandler;
    private final LogoutSuccessfulHandler logoutSuccessfulHandler;
    private final AccessDeniedHandler restAccessDeniedHandler;

    private static final String[] WHITE_LIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/auth/**",
            "/", "/signin", "/signup", "/resetPassword",
            "/actuator/**"
    };

    @Autowired
    public SecurityConfig(LoginSuccessfulHandler successfulHandler, LoginFailureHandler failureHandler, UserDetailsServiceImpl userDetailsService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, LogoutSuccessfulHandler logoutSuccessfulHandler, AccessDeniedHandler restAccessDeniedHandler, LogoutProcessHandler logoutProcessHandler) {
        this.successfulHandler = successfulHandler;
        this.failureHandler = failureHandler;
        this.userDetailsService = userDetailsService;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.logoutSuccessfulHandler = logoutSuccessfulHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.logoutProcessHandler = logoutProcessHandler;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/static/**",
                "/webjars/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //白名单
        http.authorizeRequests()
                .antMatchers(WHITE_LIST)
                .permitAll();

        //允许跨域请求
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers(HttpMethod.HEAD)
                .permitAll();

        //登录
        http.formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler(successfulHandler)
                .failureHandler(failureHandler);
        //注销登录
        http.logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutProcessHandler)
                .logoutSuccessHandler(logoutSuccessfulHandler)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        http.authorizeRequests()
                .and()
                .rememberMe()
                .userDetailsService(userDetailsService)
                .useSecureCookie(true);


        //覆盖UsernamePasswordAuthenticationFilter过滤器对ajax登录和表单登录分别处理
        http.addFilterAt(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //未登录用户访问没有相应权限的页面的处理
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        //指定无权限请求处理器（适用于已登录认证的用户）
        http.exceptionHandling().accessDeniedHandler(restAccessDeniedHandler);
        super.configure(http);
    }

    /**
     * 自定义用户信息获取来源和加密算法
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 选择加密算法（BCryptPasswordEncoder BCrypt）并注入IOC容器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 重用父类的AuthenticationManager
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        try {
            return super.authenticationManagerBean();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }


    /**
     * 注入自定义UsernamePasswordAuthenticationFilter过滤器
     *
     * @return Bean
     */
    @Bean
    public MyUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        //重用WebSecurityConfigurerAdapter配置的AuthenticationManager，重点！！！！
        filter.setAuthenticationManager(authenticationManagerBean());
        //设置拦截路径为登录路径
        filter.setFilterProcessesUrl(LOGIN_PROCESSING_URL);
        //设置认证完成后的处理器
        filter.setAuthenticationSuccessHandler(successfulHandler);
        filter.setAuthenticationFailureHandler(failureHandler);

        return filter;
    }

}
