package tacos.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.NaturalId;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/*
  第一部分 第三章 3.2
  注解以实现 JPA 持久化
 */

// 会自动添加一个有参构造器
@Data
/*
   目的：  针对标有 @NonNull 注解的变量和 final 变量进行参数的构造方法
	   @Data 隐式地添加了一个必需的有参构造函数，但是当使用 @NoArgsConstructor 时，
	   该构造函数将被删除。显式的 @RequiredArgsConstructor 确保除了私有无参数构造函数外，
	   仍然有一个必需有参构造函数。
 */
@RequiredArgsConstructor
// 不想直接使用 （private）  ；  final属性默认必须设置为null（force）
@NoArgsConstructor(access=AccessLevel.PRIVATE, force=true)
// 将其声明为JPA实体
@Entity
// 最后结果为： 1+1-1  有一个private 的无参构造器， 和一个有参构造器
public class Ingredient {
	
	@Id
	@NaturalId(mutable=false)
	private final String id;
	private final String name;
	private final Type type;

	//enum：enumeration 枚举: contains a fixed set of constants.
	public static enum Type {
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
	}
}
