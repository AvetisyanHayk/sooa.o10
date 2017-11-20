package be.howest.sooa.o10.domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

/**
 *
 * @author Hayk
 */
public class Trainer {

    public static final int INITIAL_BALLS = 3;

    private String name;
    private int pokeballs;
    private List<Pokemon> pokemons;

    public Trainer(String name) {
        this(name, INITIAL_BALLS);
    }

    public Trainer(String name, int pokeballs) {
        this.name = name;
        this.pokeballs = pokeballs;
        pokemons = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public int getPokeballs() {
        return pokeballs;
    }

    public void setPokeballs(int pokeballs) {
        this.pokeballs = pokeballs;
    }
    
    public boolean hasPokeballs() {
        return pokeballs > 0;
    }

    public List<Pokemon> getPokemons() {
        return Collections.unmodifiableList(pokemons);
    }
    
    public void setPokemons(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
    }
    
    public int getPokemonCount() {
        return pokemons.size();
    }

    public void earnBall() {
        pokeballs++;
    }

    public void useBall() {
        if (pokeballs > 0) {
            pokeballs--;
        }
    }

    public void addPokemon(Pokemon pokemon) {
        if (pokemon != null) {
            pokemons.add(pokemon);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name.toLowerCase(Locale.ENGLISH));
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
        final Trainer other = (Trainer) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
