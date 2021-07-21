package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.entity.Ingredient;

//<String> 表示实体ID属性的类型
public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
