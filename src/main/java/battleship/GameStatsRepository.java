package battleship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Gere a persistência das estatísticas do jogador em JSON.
 * Utiliza a biblioteca Jackson (já existente no projeto).
 * O ficheiro é guardado em "stats.json" na raiz do projeto.
 */
public class GameStatsRepository {

    private static final String STATS_FILE = "stats.json";
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private GameStatsRepository() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Carrega as estatísticas do ficheiro JSON.
     * Se o ficheiro não existir, devolve estatísticas a zero.
     *
     * @return as estatísticas carregadas ou novas se não existir ficheiro
     */
    public static GameStats load() {
        File file = new File(STATS_FILE);
        if (!file.exists()) return new GameStats();
        try {
            return mapper.readValue(file, GameStats.class);
        } catch (IOException e) {
            System.out.println("Erro ao carregar estatísticas, a iniciar a zero.");
            return new GameStats();
        }
    }

    /**
     * Guarda as estatísticas no ficheiro JSON.
     *
     * @param stats as estatísticas a guardar
     */
    public static void save(GameStats stats) {
        try {
            mapper.writeValue(new File(STATS_FILE), stats);
        } catch (IOException e) {
            System.out.println("Erro ao guardar estatísticas: " + e.getMessage());
        }
    }

    /**
     * Apaga todas as estatísticas acumuladas.
     */
    public static void reset() {
        save(new GameStats());
        System.out.println("Estatísticas apagadas com sucesso!");
    }
}
