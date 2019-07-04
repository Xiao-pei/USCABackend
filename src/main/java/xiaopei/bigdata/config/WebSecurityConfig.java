package xiaopei.bigdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("detailService")
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register", "/search", "api/check").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                /**Login success*/
                .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.setContentType("application/json;charset=utf-8");
                    PrintWriter out = httpServletResponse.getWriter();
                    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("error", 0);
                    node.put("message", "login success");
                    node.put("username", user.getUsername());
                    out.write(objectMapper.writeValueAsString(node));
                    out.flush();
                    out.close();
                })
                /**Login failed*/
                .failureHandler((httpServletRequest, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("error", 1);
                    node.put("message", "login failed");
                    WriteString(resp.getWriter(), objectMapper.writeValueAsString(node));
                })
                .and()
                .exceptionHandling()
                .accessDeniedHandler((HttpServletRequest httpServletRequest,
                                      HttpServletResponse resp, AccessDeniedException e) -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("error", 1);
                    node.put("message", e.getMessage());
                    WriteString(resp.getWriter(), objectMapper.writeValueAsString(node));
                })
                /**Login required*/
                .authenticationEntryPoint((HttpServletRequest httpServletRequest,
                                           HttpServletResponse resp,
                                           AuthenticationException e) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    ObjectNode node = objectMapper.createObjectNode();
                    node.put("error", 1);
                    node.put("message", e.getMessage());
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    WriteString(resp.getWriter(), objectMapper.writeValueAsString(node));
                })
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    ObjectMapper om = new ObjectMapper();
                    ObjectNode node = om.createObjectNode();
                    node.put("error", 0);
                    node.put("message", "logout success");
                    WriteString(resp.getWriter(), om.writeValueAsString(node));
                })
                .permitAll()
                .and()
                .csrf().disable();

    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    private void WriteString(PrintWriter out, String node) {
        out.write(node);
        out.flush();
        out.close();
    }
}
