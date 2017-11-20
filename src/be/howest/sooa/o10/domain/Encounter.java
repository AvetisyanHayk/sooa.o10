package be.howest.sooa.o10.domain;

import java.util.Objects;

/**
 *
 * @author Hayk
 */
public class Encounter implements Comparable<Encounter> {

    private final long id;
    private final Pokemon pokemon;
    private final Location location;

    public Encounter(Pokemon pokemon, int x, int y) {
        this(0L, pokemon, x, y);
    }

    public Encounter(long id, Pokemon pokemon, int x, int y) {
        this(id, pokemon, new Location(x, y));
    }

    public Encounter(Pokemon pokemon, Location location) {
        this(0L, pokemon, location);
    }

    public Encounter(long id, Pokemon pokemon, Location location) {
        if (location == null) {
            location = new Location();
        }
        this.pokemon = pokemon;
        this.location = location;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public Location getLocation() {
        return location;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.pokemon);
        hash = 17 * hash + Objects.hashCode(this.location);
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
        final Encounter other = (Encounter) obj;
        if (!Objects.equals(this.pokemon, other.pokemon)) {
            return false;
        }
        return location.equals(other.location);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(pokemon.getCapitalizedName()).append(" ").append(location)
                .toString();
    }

    @Override
    public int compareTo(Encounter other) {
        return location.compareTo(other.location);
    }
}
