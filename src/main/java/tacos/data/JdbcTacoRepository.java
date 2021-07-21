package tacos.data;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.entity.Ingredient;
import tacos.entity.Taco;

@Repository
public class JdbcTacoRepository implements TacoRepository {
	
	private JdbcTemplate jdbc;
	
	// 为什么选择构造器来利用注解，而不是直接在变量
	@Autowired
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}	
	
	/**
	 * 保存必要的 taco设计细节 （储存design视图返回的 用户创建的taco信息）
	 * @return
	 */
	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for (Ingredient ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}
		
		return taco;
	}

	/**
	 * 生成ID，并于用户design的taco的名字，创建时间一并 存入数据库
	 * @param taco
	 * @return
	 */
	private long saveTacoInfo(Taco taco) {
		
		// 生成创建时间
		taco.setCreatedAt(new Date());
		
		// 无法获取到 KeyHolder， 以至没有主键id无法更新数据， 所以拆分
		PreparedStatementCreatorFactory pscEdit =
				new PreparedStatementCreatorFactory(
					   "insert into Taco (name, createdAt) values (?, ?)",
					   Types.VARCHAR, Types.TIMESTAMP
					   );
		pscEdit.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = pscEdit.newPreparedStatementCreator(
				Arrays.asList(
						taco.getName(),
						new Timestamp(taco.getCreatedAt().getTime())));
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);

	    return keyHolder.getKey().longValue();	
	}

	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		jdbc.update(
				"insert into Taco_Ingredients (taco, ingredient) values (?,?)",
				tacoId, ingredient.getId());
	}
	
}
