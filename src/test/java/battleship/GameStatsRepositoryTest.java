/**
 * Test class for GameStatsRepository.
 * Author: ${user.name}
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - load(): 3 (file not exists, file exists and valid, file exists and invalid)
 * - save(): 2 (success, IOException)
 * - reset(): 1
 */
package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class GameStatsRepositoryTest {

    private static final String STATS_FILE = "stats.json";

    @BeforeEach
    void setUp() {
        // Garantir que não existe ficheiro de stats antes de cada teste
        File file = new File(STATS_FILE);
        if (file.exists()) file.delete();
    }

    @AfterEach
    void tearDown() {
        // Limpar o ficheiro após cada teste
        File file = new File(STATS_FILE);
        if (file.exists()) file.delete();
    }

    // -----------------------------------------------------------------------
    // load() - path 1: ficheiro não existe
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("load() should return new GameStats when file does not exist")
    void load1() {
        GameStats stats = GameStatsRepository.load();
        assertAll(
                () -> assertNotNull(stats,
                        "Error: load() should return a non-null GameStats when file does not exist."),
                () -> assertEquals(0, stats.getTotalJogos(),
                        "Error: totalJogos should be 0 when file does not exist."),
                () -> assertEquals(0, stats.getJogosGanhos(),
                        "Error: jogosGanhos should be 0 when file does not exist."),
                () -> assertEquals(0, stats.getTotalTiros(),
                        "Error: totalTiros should be 0 when file does not exist."),
                () -> assertEquals(0, stats.getTirosAcertados(),
                        "Error: tirosAcertados should be 0 when file does not exist.")
        );
    }

    // -----------------------------------------------------------------------
    // load() - path 2: ficheiro existe e é válido
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("load() should return correct GameStats when file exists and is valid")
    void load2() {
        // Guardar stats primeiro
        GameStats original = new GameStats();
        original.setTotalJogos(5);
        original.setJogosGanhos(3);
        original.setTotalTiros(100);
        original.setTirosAcertados(50);
        GameStatsRepository.save(original);

        // Carregar e verificar
        GameStats loaded = GameStatsRepository.load();
        assertAll(
                () -> assertNotNull(loaded,
                        "Error: load() should return a non-null GameStats when file is valid."),
                () -> assertEquals(5, loaded.getTotalJogos(),
                        "Error: totalJogos should be 5 after loading."),
                () -> assertEquals(3, loaded.getJogosGanhos(),
                        "Error: jogosGanhos should be 3 after loading."),
                () -> assertEquals(100, loaded.getTotalTiros(),
                        "Error: totalTiros should be 100 after loading."),
                () -> assertEquals(50, loaded.getTirosAcertados(),
                        "Error: tirosAcertados should be 50 after loading.")
        );
    }

    // -----------------------------------------------------------------------
    // load() - path 3: ficheiro existe mas é inválido (IOException)
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("load() should return new GameStats when file is corrupted")
    void load3() throws IOException {
        // Criar ficheiro com conteúdo inválido
        try (FileWriter writer = new FileWriter(STATS_FILE)) {
            writer.write("conteudo_invalido_nao_e_json");
        }

        GameStats stats = GameStatsRepository.load();
        assertAll(
                () -> assertNotNull(stats,
                        "Error: load() should return a non-null GameStats when file is corrupted."),
                () -> assertEquals(0, stats.getTotalJogos(),
                        "Error: totalJogos should be 0 when file is corrupted."),
                () -> assertEquals(0, stats.getJogosGanhos(),
                        "Error: jogosGanhos should be 0 when file is corrupted.")
        );
    }

    // -----------------------------------------------------------------------
    // save() - path 1: guardar com sucesso
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("save() should persist GameStats to file successfully")
    void save1() {
        GameStats stats = new GameStats();
        stats.setTotalJogos(10);
        stats.setJogosGanhos(7);
        stats.setTotalTiros(200);
        stats.setTirosAcertados(120);

        GameStatsRepository.save(stats);

        File file = new File(STATS_FILE);
        assertTrue(file.exists(),
                "Error: save() should create the stats.json file.");
        assertTrue(file.length() > 0,
                "Error: save() should write non-empty content to the file.");
    }

    // -----------------------------------------------------------------------
    // save() - path 2: save e load são consistentes
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("save() and load() should be consistent")
    void save2() {
        GameStats original = new GameStats();
        original.setTotalJogos(3);
        original.setJogosGanhos(2);
        original.setTotalTiros(60);
        original.setTirosAcertados(30);

        GameStatsRepository.save(original);
        GameStats loaded = GameStatsRepository.load();

        assertAll(
                () -> assertEquals(original.getTotalJogos(), loaded.getTotalJogos(),
                        "Error: totalJogos should match after save and load."),
                () -> assertEquals(original.getJogosGanhos(), loaded.getJogosGanhos(),
                        "Error: jogosGanhos should match after save and load."),
                () -> assertEquals(original.getTotalTiros(), loaded.getTotalTiros(),
                        "Error: totalTiros should match after save and load."),
                () -> assertEquals(original.getTirosAcertados(), loaded.getTirosAcertados(),
                        "Error: tirosAcertados should match after save and load.")
        );
    }

    // -----------------------------------------------------------------------
// save() - path 3: IOException ao guardar
// -----------------------------------------------------------------------

    @Test
    @DisplayName("save() should handle IOException gracefully when file is read-only")
    void save3() throws IOException {
        // Criar o ficheiro e torná-lo read-only para forçar IOException
        File file = new File(STATS_FILE);
        file.createNewFile();
        file.setReadOnly();

        GameStats stats = new GameStats();
        stats.setTotalJogos(5);

        // Não deve lançar exceção — deve ser tratada internamente
        assertDoesNotThrow(() -> GameStatsRepository.save(stats),
                "Error: save() should handle IOException gracefully.");

        // Restaurar permissões para o tearDown conseguir apagar
        file.setWritable(true);
    }

    // -----------------------------------------------------------------------
    // reset()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("reset() should overwrite stats with zeroed GameStats")
    void reset() {
        // Guardar stats com valores
        GameStats stats = new GameStats();
        stats.setTotalJogos(10);
        stats.setJogosGanhos(5);
        GameStatsRepository.save(stats);

        // Reset
        GameStatsRepository.reset();

        // Verificar que as stats foram apagadas
        GameStats resetStats = GameStatsRepository.load();
        assertAll(
                () -> assertEquals(0, resetStats.getTotalJogos(),
                        "Error: totalJogos should be 0 after reset."),
                () -> assertEquals(0, resetStats.getJogosGanhos(),
                        "Error: jogosGanhos should be 0 after reset."),
                () -> assertEquals(0, resetStats.getTotalTiros(),
                        "Error: totalTiros should be 0 after reset."),
                () -> assertEquals(0, resetStats.getTirosAcertados(),
                        "Error: tirosAcertados should be 0 after reset.")
        );
    }
}