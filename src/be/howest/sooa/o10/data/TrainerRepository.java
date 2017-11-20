package be.howest.sooa.o10.data;

import be.howest.sooa.o10.domain.Pokemon;
import be.howest.sooa.o10.domain.Trainer;
import be.howest.sooa.o10.ex.TrainerIOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author hayk
 */
public class TrainerRepository {

    private static final String TRAINERS_PATH = "trainers";
    private static final String FMT_TRAINER_PATH = "trainers/%s";
    private static final File TRAINERS_DIR = new File(TRAINERS_PATH);
    private static final String FMT_POKEBALLS_PATH = TRAINERS_PATH + "/%s/pokeballs";
    private static final String FMT_POKEMONS_PATH = TRAINERS_PATH + "/%s/pokemons";

    private final PokemonRepository pokemonRepo = new PokemonRepository();

    public TrainerRepository() {
        checkDirectory();
    }

    final boolean checkDirectory() {
        if (!TRAINERS_DIR.exists()) {
            return TRAINERS_DIR.mkdir();
        }
        return true;
    }

    public Trainer findByName(String name) {
        checkDirectory();
        return build(name);
    }

    public List<Trainer> findAll() throws TrainerIOException {
        checkDirectory();
        File trainersDirectory = new File(TRAINERS_PATH);
        List<Trainer> trainers = new ArrayList<>();
        String[] trainerDirectories = trainersDirectory
                .list((File current, String name)
                        -> new File(current, name).isDirectory());
        for (String name : trainerDirectories) {
            trainers.add(build(name));
        }
        return trainers;
    }

    public boolean save(Trainer trainer) {
        boolean trainersDirectoryExists = checkDirectory();
        File trainerDirectory
                = new File(String.format(FMT_TRAINER_PATH, trainer.getName()));
        boolean trainerDirectoryExists = trainerDirectory.exists();
        if (!trainerDirectoryExists) {
            trainerDirectoryExists = trainerDirectory.mkdir();
        }
        savePokeballsFor(trainer);
        savePokemonsFor(trainer);
        return trainersDirectoryExists && trainerDirectoryExists;
    }

    public boolean updateTrainerName(Trainer trainer, String oldTrainerName) throws TrainerIOException {
        boolean trainersDirectoryExists = checkDirectory();
        String newTrainerName = trainer.getName();
        File newTrainerDirectory
                = new File(String.format(FMT_TRAINER_PATH, newTrainerName));
        if (newTrainerDirectory.exists()) {
            throw new TrainerIOException("Trainer " + trainer.getName() + " already exists.");
        } else {

            File oldTrainerDirectory
                    = new File(String.format(FMT_TRAINER_PATH, oldTrainerName));
            if (!oldTrainerDirectory.exists()) {
                return save(trainer);
            }
            return trainersDirectoryExists
                    && oldTrainerDirectory.renameTo(newTrainerDirectory);
        }
    }

    public boolean remove(Trainer trainer) {
        File trainerDirectory
                = new File(String.format(FMT_TRAINER_PATH, trainer.getName()));
        if (trainerDirectory.exists()) {
            return deleteDir(trainerDirectory);
        }
        return false;
    }

    private boolean deleteDir(File directory) {
        File[] contents = directory.listFiles();
        if (contents != null) {
            for (File dir : contents) {
                deleteDir(dir);
            }
        }
        return directory.delete();
    }

    private Trainer build(String name) throws TrainerIOException {
        if (name != null && !"".equals(name = name.trim())) {
            File trainerDirectory
                    = new File(String.format(FMT_TRAINER_PATH, name));
            if (trainerDirectory.exists()) {
                Trainer trainer = new Trainer(name);
                trainer.setPokeballs(getPokeballsFor(trainer));
                trainer.setPokemons(getPokemonsFor(trainer));
                return trainer;
            }
        }
        return null;
    }

    private int getPokeballsFor(Trainer trainer) throws TrainerIOException {
        File pokeballsFile = new File(String.format(FMT_POKEBALLS_PATH, trainer.getName()));
        try (BufferedReader bufferedReader
                = Files.newBufferedReader(pokeballsFile.toPath(), StandardCharsets.UTF_8)) {
            StringBuilder sb = new StringBuilder();
            for (int eenByte; (eenByte = bufferedReader.read()) != -1;) {
                sb.append((char) eenByte);
            }
            return Integer.parseInt(sb.toString().trim());
        } catch (IOException | NumberFormatException ex) {
            throw new TrainerIOException(ex);
        }
    }

    private List<Pokemon> getPokemonsFor(Trainer trainer) throws TrainerIOException {
        File pokemonsFile = new File(String.format(FMT_POKEMONS_PATH, trainer.getName()));
        List<Pokemon> pokemons = new ArrayList<>();
        try (BufferedReader bufferedReader
                = Files.newBufferedReader(pokemonsFile.toPath(), StandardCharsets.UTF_8)) {
            Stream<String> lines = bufferedReader.lines();
            lines.map(String::trim).forEach((line) -> {
                pokemons.add(readPokemon(line));
            });
        } catch (IOException ex) {
            throw new TrainerIOException(ex);
        }
        return pokemons;
    }

    private Pokemon readPokemon(String pokemonId) throws TrainerIOException {
        if (pokemonId != null && !"".equals(pokemonId)) {
            try {
                long id = Long.parseLong(pokemonId);
                return pokemonRepo.read(id);
            } catch (NumberFormatException ex) {
                throw new TrainerIOException("Pokemon-data corrupted.", ex);
            }
        }
        return null;
    }

    private void savePokeballsFor(Trainer trainer) throws TrainerIOException {
        File pokeballsFile = new File(String.format(FMT_POKEBALLS_PATH, trainer.getName()));
        try (BufferedWriter bufferedWriter
                = Files.newBufferedWriter(pokeballsFile.toPath(), StandardCharsets.UTF_8)) {
            String pokeballs = String.valueOf(trainer.getPokeballs());
            bufferedWriter.write(pokeballs);
        } catch (IOException | NumberFormatException ex) {
            throw new TrainerIOException("Pokeball-data corrupted.", ex);
        }
    }

    private void savePokemonsFor(Trainer trainer) throws TrainerIOException {
        File pokemonsFile = new File(String.format(FMT_POKEMONS_PATH, trainer.getName()));
        try (BufferedWriter bufferedWriter
                = Files.newBufferedWriter(pokemonsFile.toPath(), StandardCharsets.UTF_8)) {
            trainer.getPokemons().forEach((pokemon) -> {
                try {
                    String pokemonId = String.valueOf(pokemon.getId());
                    bufferedWriter.write(pokemonId);
                    bufferedWriter.newLine();
                } catch (IOException ex) {
                    throw new TrainerIOException("Pokemon-data corrupted.", ex);
                }
            });
        } catch (IOException ex) {
            throw new TrainerIOException(ex);
        }
    }
}
