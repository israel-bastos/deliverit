package br.com.deliverit.security;

//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@EnableWebSecurity
public class SecurityConfiguration { //extends WebSecurityConfigurerAdapter {
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//		http.csrf().disable()
//				.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }
//	
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        
//        auth.inMemoryAuthentication()
//                .withUser("deliverit")
//                .password(passwordEncoder.encode("deliverit"))
//                .roles("USER");
//    }
}