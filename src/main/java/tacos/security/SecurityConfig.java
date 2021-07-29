package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * 提供了一个免费的普通登录页面
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * 定义了统一的加密方式？
	 * encoder() 方法是用 @Bean 注释的
	 * 所以它将被用于在 Spring 应用程序上下文中声明一个 PasswordEncoder bean
	 * 然后拦截对 encoder() 的任何调用
	 * 以从应用程序上下文中返回 bean 实例
	 */
	@Bean
	public PasswordEncoder encoder() {
		return new StandardPasswordEncoder("53cr3t");
	}
	
	/**
	 * 身份验证
	 * 重写 WebSecurityConfigurerAdapter 配置基类中定义的 configure() 方法
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// 定制用户详细信息服务
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
		
		/**
		// 基于内存的用户储存
		auth.inMemoryAuthentication()
			.withUser("buzz").password("{noop}infinity").authorities("ROLE_USER")
			.and()
			.withUser("woody").password("{noop}bullseye").authorities("ROLE_USER");
		
		// 基于JDBC的用户储存
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery(
					"select username, password, enabled from Users "
					+ "where username=?")
			.authoritiesByUsernameQuery(
					"select username, authority from UserAuthorities "
					+ "where username=?")
			.passwordEncoder(new StandardPasswordEncoder("53cr3t"));
		
		// 基于LDAP支持的用户存储
		auth.ldapAuthentication()
			.userSearchBase("ou=people").userSearchFilter("(uid={0})")
			.groupSearchBase("ou=groups").groupSearchFilter("member={0}")
				.passwordCompare()
				.passwordEncoder(new BCryptPasswordEncoder())
				.passwordAttribute("passcode");
//				.contextSource()
//					.root("dc=tacocloud,dc=com")
//					.ldif("classpath:users.ldif");
		 */
	}

	/**
	 * 保护请求
	 */
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 *  这些规则的顺序很重要。首先声明的安全规则优先于较低级别声明的安全规则
		 *  安全表达式扩展 
		 *  Spring Security 扩展了 SpEL
		 */
		http
			.authorizeRequests()
				.antMatchers("/design", "/orders/**").access("hasRole('ROLE_USER')")
				.antMatchers("/","/**").access("permitAll")
		
			// 指定用户登录页面
			.and()
				.formLogin()
				.loginPage("/login")
//				.loginProcessingUrl(null)
		
			// 用户登出
			.and()
				.logout().logoutSuccessUrl("/login")
			
			/*
			 * Make H2-Console non-secured;
			 * for debug purposes
			 */
			.and()
				.csrf().ignoringAntMatchers("/h2-console/**")
			
			/*
			 *  Allow pages to be loaded in frames from the same origin; 
			 *  needed for H2-Console
			 */
			.and()
				.headers().frameOptions().sameOrigin();
	}
	
}
