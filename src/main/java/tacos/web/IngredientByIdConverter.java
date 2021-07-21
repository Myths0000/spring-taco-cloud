package tacos.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import tacos.entity.Ingredient;
import tacos.data.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, Ingredient> {

	@Autowired
	private IngredientRepository ingredientRepo;

    @Override
    public Ingredient convert(String id) {
    	Optional<Ingredient> optionalIngredient = ingredientRepo.findById(id);
    	if (optionalIngredient != null) {
    		return optionalIngredient.get();
    	} else {
    		return null;
    	}
//    	return optionalIngredient.isPresent() ? optionalIngredient.get() : null;
    }
}	
