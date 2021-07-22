package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.entity.User;

/**
 * 用户存储库接口
 * @author WH
 */
public interface UserRepository extends CrudRepository<User, String> {
	
	/**
	 * 在用户详细信息服务中使用该方法根据用户名查找 User
	 */
	User findByUsername(String username);

}
