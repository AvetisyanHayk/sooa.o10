package be.howest.sooa.o10.data;

import be.howest.sooa.o10.domain.Pokemon;
import be.howest.sooa.o10.ex.DBException;
import be.howest.sooa.o10.gui.PokemonImagePanel;
import be.howest.sooa.o10.gui.ImageType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hayk
 */
public class PokemonRepository extends AbstractRepository {

    private static final String SQL = "SELECT * FROM pokemon";
    private static final String SQL_READ = SQL + " WHERE id = ?";
    private static final String SQL_FIND_ALL
            = SQL + " ORDER BY is_default DESC, `order`, identifier";
    private static final String SQL_FIND_ALL_BY_DEFAULT
            = SQL + " WHERE is_default = ?"
            + " ORDER BY `order`, identifier";
    private static final String SQL_PAGE = SQL_FIND_ALL
            + " LIMIT ?, ?";

    public Pokemon read(long id) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_READ)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return build(resultSet);
                }
            }
            return null;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public Pokemon readWithImagePath(long id, ImageType imageType)
            throws DBException {
        Pokemon pokemon = read(id);
        if (pokemon != null) {
            pokemon.setImagePath(getImagePathFor(pokemon, imageType));
        }
        return pokemon;
    }

    public List<Pokemon> findAll() {
        List<Pokemon> entities = new ArrayList<>();
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL)) {
            while (resultSet.next()) {
                entities.add(build(resultSet));
            }
            return entities;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new DBException(ex);
        }
    }

    public List<Pokemon> findAllWithImagePath(ImageType imageType) throws DBException {
        return getPokemonsWithImagePath(findAll(), imageType);
    }

    public List<Pokemon> findAllByDefault(boolean _default) {
        List<Pokemon> entities = new ArrayList<>();
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_DEFAULT)) {
            statement.setBoolean(1, _default);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(build(resultSet));
                }
            }
            return entities;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private Pokemon build(ResultSet resultSet) throws SQLException {
        return new Pokemon(
                resultSet.getLong("id"),
                resultSet.getString("identifier"),
                resultSet.getInt("species_id"),
                resultSet.getInt("height"),
                resultSet.getInt("weight"),
                resultSet.getInt("base_experience"),
                resultSet.getInt("order"),
                resultSet.getBoolean("is_default")
        );
    }

    private static String getImagePathFor(Pokemon pokemon, ImageType imageType) {
        return PokemonImagePanel.getImagePathFor(pokemon, imageType);
    }

    public static List<Pokemon> getPokemonsWithImagePath(List<Pokemon> pokemons,
            ImageType imageType) {
        List<Pokemon> pokemonsWithImagePath = new ArrayList<>();
        if (pokemons != null) {
            pokemons.forEach((pokemon) -> {
                pokemon.setImagePath(getImagePathFor(pokemon, imageType));
                pokemonsWithImagePath.add(pokemon);
            });
        }
        return pokemonsWithImagePath;
    }
}
