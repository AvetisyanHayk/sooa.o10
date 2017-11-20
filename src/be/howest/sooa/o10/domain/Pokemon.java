package be.howest.sooa.o10.domain;

import java.util.Locale;
import java.util.Objects;

/**
 *
 * @author Hayk
 */
public class Pokemon implements Comparable<Pokemon> {

    private final long id;
    private final String name;
    private final int speciesId;
    private final int height;
    private final int weight;
    private final int baseExperience;
    private final int order;
    private final boolean _default;
    private String imagePath;

    public Pokemon(long id, String name, int speciesId, int height, int weight,
            int baseExperience, int order, boolean _default) {
        this.id = id;
        this.name = name;
        this.speciesId = speciesId;
        this.height = height;
        this.weight = weight;
        this.baseExperience = baseExperience;
        this.order = order;
        this._default = _default;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String getCapitalizedName() {
        if (name != null && !"".equals(name)) {
            StringBuilder sb = new StringBuilder();
            sb.append(name.toUpperCase(Locale.ENGLISH).charAt(0));
            if (name.length() == 1) {
                return sb.toString();
            }
            sb.append(name.toLowerCase(Locale.ENGLISH).substring(1));
            return sb.toString();
        }
        return "";
    }

    public int getSpeciesId() {
        return speciesId;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getOrder() {
        return order;
    }

    public boolean isDefault() {
        return _default;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name.toLowerCase(Locale.ENGLISH));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pokemon other = (Pokemon) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCapitalizedName());
        if (!isDefault()) {
            sb.append(" (not default)");
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Pokemon other) {
        String pokemon = String.format("%d%s", order,
                name.toLowerCase(Locale.ENGLISH));
        String otherPokemon = String.format("%d%s", other.order,
                other.name.toLowerCase(Locale.ENGLISH));
        return pokemon.compareTo(otherPokemon);
    }
}
