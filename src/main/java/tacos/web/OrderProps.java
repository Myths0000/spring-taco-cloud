package tacos.web;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
/*
 * @Component 使得Spring 组件扫描时将自动发现它并在 Spring 应用程序上下文中将其创建为bean
 * 这很重要，因为下一步是将 OrderProps bean 注入到 OrderController 中。
 */
@ConfigurationProperties(prefix="taco.orders")
@Data
public class OrderProps {
    @Min(value=5, message="must be between 5 and 25")
    @Max(value=25, message="must be between 5 and 25")
	private int pageSize = 20;
}
