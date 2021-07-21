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
// 为了能够把多个taco存在一个order中，需要保存在session中才能跨请求使用
// 在用户完成操作并提交Order表单之前，Order对象会一直保存在session中，而不是保存到数据库中
@SessionAttributes("order")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	
	private TacoRepository designRepo;

	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, TacoRepository designRepo) {
		this.ingredientRepo = ingredientRepo;
		this.designRepo = designRepo;
	}
	
	/**
	 * 创建唯一的 Order 对象
	 * @SesssionAttributes 可以指定模型对象并保存在session中，
	   然后可以跨请求使用
	 * @return
	 */
	@ModelAttribute(name = "order")
	public Order order() {
	    return new Order();
	}
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	// Model 以参数的方式传递过来
	@GetMapping
	public String showDesignForm(Model model) {
		
		// 获取数据
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		// values() 是枚举enum的方法
		Type[] types = Ingredient.Type.values();
		
		// 把过滤后的配料列表 作为 属性添加到Model对象上
		for (Type type: types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
		
		// 不能被删除，用来给taco命名并被thymeleaf识别 (th:object)
		model.addAttribute("designTaco", new Taco()); 
		
		// 视图的逻辑名称, 将模型渲染到视图上 (html)
		return "design";
	}

	/**
	 * Order参数带有@ModelAttribute注解
	   表明它的值应该是来自模型的
	   SpringMVC不会尝试将请求参数绑定到它上面 （？）
	 * @param design
	 * @param errors
	 * @param order
	 * @return
	 */
	@PostMapping
	public String processDesign(@Valid @ModelAttribute("designTaco") Taco design, Errors errors,
			@ModelAttribute Order order) {
		
		if(errors.hasErrors()) {
			return "design";
		}
		
	    Taco saved = designRepo.save(design);
	    order.addDesign(saved);
	    
		log.info("Processing design: " + design);
		
		// 返回的String 代表了一个要展现给用户的视图
		return "redirect:/orders/current"; 
	}
	
	// 筛选相同 type 的 ingredient
	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		return ingredients
				.stream()
				.filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
