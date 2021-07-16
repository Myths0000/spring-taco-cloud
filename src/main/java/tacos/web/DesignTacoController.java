package tacos.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;
import tacos.entity.Ingredient;
import tacos.entity.Ingredient.Type;
import tacos.entity.Order;
import tacos.entity.Taco;

@Slf4j
@Controller
@RequestMapping("/design")
//为了能够把多个taco存在一个order中，需要保存在session中才能跨请求使用
@SessionAttributes("order")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	
	private TacoRepository designRepo;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}
	
	@ModelAttribute(name = "order")
	public Order order() {
	    return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		// values() 是枚举enum的方法
		Type[] types = Ingredient.Type.values();
		for (Type type: types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		
		// 为什么被注释？用来起什么作用
		model.addAttribute("design", new Taco()); //不能被删除，用来绑定view 和 model 的关键code
		return "design";//视图的逻辑名称, 将模型渲染到视图上
	}

	@PostMapping
	public String processDesign(@Valid Taco design, Errors errors,
			@ModelAttribute Order order) {
		
		if(errors.hasErrors()) {
//			return "design";
			return "redirect:/design";
		}
		
	    Taco saved = designRepo.save(design);
	    order.addDesign(saved);
	    
		log.info("Processing design: " + design);
		return "redirect:/orders/current"; //代表了一个要展现给用户的视图??
	}
	
	
	// 筛选相同 type 的 ingredient
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
