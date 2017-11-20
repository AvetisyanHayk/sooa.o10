package be.howest.sooa.o10.data;

import be.howest.sooa.o10.domain.Pokemon;
import be.howest.sooa.o10.domain.Trainer;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Hayk
 */
public class TrainerRepositoryTest {

    private TrainerRepository trainerRepo;
    private PokemonRepository pokemonRepo;
    private Pokemon pokemon1;
    private Pokemon pokemon2;

    public TrainerRepositoryTest() {
        try {
            AbstractRepository.connect("mysql", "localhost", "3306",
                    "sooa_o8", "student", "student", "false");
            pokemonRepo = new PokemonRepository();
            trainerRepo = new TrainerRepository();
            pokemon1 = pokemonRepo.read(1L);
            pokemon2 = pokemonRepo.read(2L);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    public void trainers_directory_exists() {
        assertTrue(trainerRepo.checkDirectory());
    }

    @Test
    public void saving_new_trainer_works_correctly() {
        String name = "__JUnit__";
        Trainer trainer = new Trainer(name);
        trainerRepo.remove(trainer);
        trainer.addPokemon(pokemon1);
        assertTrue(trainerRepo.save(trainer));
        Trainer readTrainer = trainerRepo.findByName(name);
        assertNotNull(readTrainer);
        assertEquals(name, readTrainer.getName());
        assertEquals(Trainer.INITIAL_BALLS, readTrainer.getPokeballs());
        assertNotNull(readTrainer.getPokemons());
        assertEquals(1, readTrainer.getPokemons().size());
        assertEquals(readTrainer.getPokemons().get(0), pokemon1);
        assertEquals(trainer, readTrainer);
        assertTrue(trainerRepo.remove(trainer));
    }

    @Test
    public void updating_existing_trainer_works_correctly() {
        String name = "__JUnit__";
        int pokeballs = 53;
        Trainer trainer = new Trainer(name);
        trainerRepo.remove(trainer);
        trainer.addPokemon(pokemon1);
        assertTrue(trainerRepo.save(trainer));
        Trainer readTrainer = trainerRepo.findByName(name);
        assertNotNull(readTrainer);
        assertEquals(1, readTrainer.getPokemons().size());
        assertEquals(Trainer.INITIAL_BALLS, readTrainer.getPokeballs());
        readTrainer.addPokemon(pokemon2);
        readTrainer.setPokeballs(pokeballs);
        trainerRepo.save(readTrainer);
        Trainer updatedTrainer = trainerRepo.findByName(name);
        assertNotNull(updatedTrainer);
        assertEquals(name, updatedTrainer.getName());
        assertEquals(pokeballs, updatedTrainer.getPokeballs());
        assertNotNull(updatedTrainer.getPokemons());
        assertEquals(2, updatedTrainer.getPokemons().size());
        assertEquals(updatedTrainer.getPokemons().get(0), pokemon1);
        assertEquals(updatedTrainer.getPokemons().get(1), pokemon2);
        assertEquals(readTrainer, updatedTrainer);
        assertTrue(trainerRepo.remove(trainer));
    }

    @Test
    public void updating_existing_trainer_name_works_correctly() {
        String oldName = "__JUnit__";
        String newName = oldName + "1";
        Trainer trainer = new Trainer(oldName);
        trainerRepo.remove(trainer);
        assertTrue(trainerRepo.save(trainer));
        Trainer readTrainer = trainerRepo.findByName(oldName);
        assertNotNull(readTrainer);
        assertEquals(oldName, readTrainer.getName());
        readTrainer.setName(newName);
        trainerRepo.updateTrainerName(readTrainer, oldName);
        Trainer updatedTrainer = trainerRepo.findByName(newName);
        assertNotNull(updatedTrainer);
        assertNotEquals(oldName, updatedTrainer.getName());
        assertEquals(newName, updatedTrainer.getName());
        assertEquals(readTrainer, updatedTrainer);
        assertFalse(trainerRepo.remove(trainer));
        assertTrue(trainerRepo.remove(updatedTrainer));
    }

    @Test
    public void removing_trainer_works_correctly() {
        String name = "__JUnit__";
        String notExistingName = name + "not_existing";
        Trainer trainer = new Trainer(name);
        Trainer notExistingTrainer = new Trainer(notExistingName);
        trainerRepo.remove(notExistingTrainer);
        assertFalse(trainerRepo.remove(notExistingTrainer));
        assertTrue(trainerRepo.save(trainer));
        assertTrue(trainerRepo.remove(trainer));
    }
}
