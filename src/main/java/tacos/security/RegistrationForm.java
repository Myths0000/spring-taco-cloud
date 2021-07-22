package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import tacos.entity.User;

@Data
public class RegistrationForm {
	
	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;
	
	/**
	 * 在 SecurityConfig 里的 PasswordEncoder bean 把
	 * encode（） 方法定义了
	 * @param passwordEncoder
	 * @return
	 */
	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(
				username, passwordEncoder.encode(password), 
				fullname, street, city, state, zip, phone);
	}
	
}
