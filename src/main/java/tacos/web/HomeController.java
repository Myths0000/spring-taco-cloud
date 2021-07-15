package tacos.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/delete")
	public String home() {
		return ""; //返回视图名，对应相应的html文件？
	}

}
