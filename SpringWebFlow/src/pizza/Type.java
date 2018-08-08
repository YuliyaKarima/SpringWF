package pizza;

import static org.apache.commons.lang.WordUtils.capitalizeFully;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public enum Type implements Serializable{
	ROMEO, FLORENTINE, 
	VENETIAN, MARINARA, 
	SPECIAL, SORENTO, 
	ROMAN, HAWAIIAN, 
	SICILIAN, VESUVIUS,
	ITALIAN, MARGARITA;

	public static List<Type> asList() {
		Type[] all = Type.values();
		return Arrays.asList(all);
	}

	@Override
	public String toString() {
		return capitalizeFully(name().replace('_', ' '));
	}
}
