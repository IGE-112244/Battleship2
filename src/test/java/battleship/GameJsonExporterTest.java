package battleship;

import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameJsonExporter.
 * Author: ${user.name}
 * Date: ${current_date}
 * Time: ${current_time}
 * Cyclomatic Complexity:
 * - export(): 2
 * - buildMovesList(): 4
 * - buildResultText(): 10
 */
public class GameJsonExporterTest {

    private Game game;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        game = new Game(new Fleet());
        testFilePath = "test_export.json";
    }

    @AfterEach
    void tearDown() {
        game = null;
        File file = new File(testFilePath);
        if (file.exists()) file.delete();
        testFilePath = null;
    }

    // -----------------------------------------------------------------------
    // export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("export() should create a JSON file at the specified path")
    void export1() {
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw for a valid game and file path.");
        assertTrue(new File(testFilePath).exists(),
                "Error: JSON file should exist after export.");
    }

    @Test
    @DisplayName("export() should throw RuntimeException for invalid file path")
    void export2() {
        assertThrows(RuntimeException.class,
                () -> GameJsonExporter.export(game, "/invalid_path/test.json"),
                "Error: export() should throw RuntimeException for an invalid file path.");
    }

    // -----------------------------------------------------------------------
    // buildMovesList() - tested indirectly via export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("export() should handle empty moves list")
    void buildMovesList1() {
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when moves list is empty.");
    }

    @Test
    @DisplayName("export() should handle non-empty alien moves list")
    void buildMovesList2() {
        List<IPosition> shots = List.of(
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3)
        );
        game.fireShots(shots);
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when alien moves list is non-empty.");
    }

    @Test
    @DisplayName("export() should handle moves with hit results")
    void buildMovesList3() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        List<IPosition> shots = List.of(
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3)
        );
        game.fireShots(shots);
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when moves contain hits.");
    }

    @Test
    @DisplayName("export() should handle moves that sink a ship")
    void buildMovesList4() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when a ship is sunk.");
    }

    // -----------------------------------------------------------------------
    // buildResultText() - tested indirectly via export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("buildResultText() should handle all misses")
    void buildResultText1() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw for all-miss results.");
    }

    @Test
    @DisplayName("buildResultText() should handle repeated shots")
    void buildResultText2() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        game.fireShots(List.of(new Position(1, 1), new Position(4, 4), new Position(5, 5)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw for repeated shots.");
    }

    @Test
    @DisplayName("buildResultText() should handle invalid shots")
    void buildResultText3() {
        game.fireSingleShot(new Position(-1, -1), false);
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw for invalid shots.");
    }

    @Test
    @DisplayName("buildResultText() should handle a hit without sinking")
    void buildResultText4() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(5, 5), new Position(6, 6)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw for a hit without sinking.");
    }

    @Test
    @DisplayName("buildResultText() should handle a sunk ship with name")
    void buildResultText5() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when a ship is sunk and named.");
    }

    @Test
    @DisplayName("buildResultText() should handle multiple sunk ships")
    void buildResultText6() {
        Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        Ship ship2 = new Barge(Compass.NORTH, new Position(5, 5));
        game.getMyFleet().addShip(ship1);
        game.getMyFleet().addShip(ship2);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        game.fireShots(List.of(new Position(5, 5), new Position(6, 5), new Position(7, 5)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should not throw when multiple ships are sunk.");
    }

    @Test
    @DisplayName("buildResultText() should handle hits with misses combined")
    void buildResultText7() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(5, 5), new Position(6, 6)));
        game.fireShots(List.of(new Position(1, 1), new Position(7, 7), new Position(8, 8)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should handle hits combined with misses and repeated shots.");
    }

    @Test
    @DisplayName("buildResultText() should handle invalid shots combined with other results")
    void buildResultText8() {
        game.fireSingleShot(new Position(-1, -1), false);
        game.fireSingleShot(new Position(1, 1), false);
        game.fireShots(List.of(new Position(2, 2), new Position(3, 3), new Position(4, 4)));
        assertDoesNotThrow(() -> GameJsonExporter.export(game, testFilePath),
                "Error: export() should handle invalid shots combined with valid shots.");
    }
}