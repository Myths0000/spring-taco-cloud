package tacos.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Ingredient {

	private final String id;
	private final String name;
	private final Type type;

	//enum：enumeration 枚举: contains a fixed set of constants.
	public static enum Type{
		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE

	}
}
