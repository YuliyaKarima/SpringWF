package pizza;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum Sizes implements Serializable {

	GINORMOUS, LARGE, MEDIUM, SMALL;

	private String[] sizes = new String[] { "45 sm", "31 sm", "26 sm", "18 sm" };

	public static List<Sizes> asList() {
		Sizes[] all = Sizes.values();
		return Arrays.asList(all);
	}

	public String toString() {
		return this.name().toLowerCase() + ", " + sizes[this.ordinal()];
	}
}
