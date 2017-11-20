package be.howest.sooa.o10.data;

import static be.howest.sooa.o10.data.AbstractRepository.getConnection;
import be.howest.sooa.o10.domain.Encounter;
import be.howest.sooa.o10.domain.Pokemon;
import be.howest.sooa.o10.ex.DBException;
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
public class EncounterRepository extends AbstractRepository {

    private static final String SQL
            = "SELECT encounter.id, encounter.x, encounter.y,"
            + " pokemon.id AS pokemon_id, pokemon.identifier AS name,"
            + " pokemon.species_id, pokemon.height, pokemon.weight,"
            + " pokemon.base_experience, pokemon.order, pokemon.is_default"
            + " FROM encounter"
            + " JOIN pokemon ON encounter.pokemon_id = pokemon.id";
    private static final String SQL_FIND_ALL = SQL
            + " ORDER BY x, y";
    private static final String SQL_INSERT
            = "INSERT INTO encounter(pokemon_id, x, y)"
            + " VALUES(?, ?, ?)";

    public List<Encounter> findAll() {
        List<Encounter> entities = new ArrayList<>();
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL_FIND_ALL)) {
            while (resultSet.next()) {
                entities.add(build(resultSet));
            }
            return entities;
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public void save(Encounter encounter) {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
            statement.setLong(1, encounter.getPokemon().getId());
            statement.setInt(2, encounter.getX());
            statement.setInt(3, encounter.getY());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    private Encounter build(ResultSet resultSet) throws SQLException {
        Pokemon pokemon = buildPokemon(resultSet);
        Encounter encounter = new Encounter(
                resultSet.getLong("id"),
                pokemon,
                resultSet.getInt("x"),
                resultSet.getInt("y"));
        return encounter;
    }

    private Pokemon buildPokemon(ResultSet resultSet) throws SQLException {
        return new Pokemon(
                resultSet.getLong("pokemon_id"),
                resultSet.getString("name"),
                resultSet.getInt("species_id"),
                resultSet.getInt("height"),
                resultSet.getInt("weight"),
                resultSet.getInt("base_experience"),
                resultSet.getInt("order"),
                resultSet.getBoolean("is_default")
        );
    }
}
