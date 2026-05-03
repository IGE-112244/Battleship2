package battleship;

import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GamePdfExporter.
 * Author: ${user.name}
 * Date: ${current_date}
 * Time: ${current_time}
 * Cyclomatic Complexity:
 * - export(): 2
 * - addMovesTable(): 4
 * - getShotColor(): 5
 * - buildResultText(): 10
 */
 class GamePdfExporterTest {

    private Game game;
    private String testFilePath;

    @BeforeEach
    void setUp() {
        game = new Game(new Fleet());
        testFilePath = "test_export.pdf";
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
    @DisplayName("export() should create a PDF file at the specified path")
    void export1() {
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for a valid game and file path.");
        assertTrue(new File(testFilePath).exists(),
                "Error: PDF file should exist after export.");
    }

    @Test
    @DisplayName("export() should throw RuntimeException for invalid file path")
    void export2() {
        assertThrows(RuntimeException.class,
                () -> GamePdfExporter.export(game, "/invalid_path/test.pdf"),
                "Error: export() should throw RuntimeException for an invalid file path.");
    }

    // -----------------------------------------------------------------------
    // addMovesTable() - tested indirectly via export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("export() should handle empty alien moves list")
    void addMovesTable1() {
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw when moves list is empty.");
    }

    @Test
    @DisplayName("export() should handle non-empty alien moves list")
    void addMovesTable2() {
        game.fireShots(List.of(
                new Position(1, 1),
                new Position(2, 2),
                new Position(3, 3)
        ));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw when alien moves list is non-empty.");
    }

    @Test
    @DisplayName("export() should handle moves with alternating row colors")
    void addMovesTable3() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        game.fireShots(List.of(new Position(4, 4), new Position(5, 5), new Position(6, 6)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for multiple moves with alternating row colors.");
    }

    @Test
    @DisplayName("export() should handle moves with a sunk ship")
    void addMovesTable4() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw when a ship is sunk.");
    }

    // -----------------------------------------------------------------------
    // getShotColor() - tested indirectly via export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("getShotColor() should handle invalid shot")
    void getShotColor1() {
        game.fireSingleShot(new Position(-1, -1), false);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for invalid shots.");
    }

    @Test
    @DisplayName("getShotColor() should handle repeated shot")
    void getShotColor2() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        game.fireShots(List.of(new Position(1, 1), new Position(4, 4), new Position(5, 5)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for repeated shots.");
    }

    @Test
    @DisplayName("getShotColor() should handle hit shot")
    void getShotColor3() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(5, 5), new Position(6, 6)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for hit shots.");
    }

    @Test
    @DisplayName("getShotColor() should handle miss shot")
    void getShotColor4() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for miss shots.");
    }

    // -----------------------------------------------------------------------
    // buildResultText() - tested indirectly via export()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("buildResultText() should handle all misses")
    void buildResultText1() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for all-miss results.");
    }

    @Test
    @DisplayName("buildResultText() should handle hits with sunk ship")
    void buildResultText2() {
        Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
        game.getMyFleet().addShip(ship);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw when ship is sunk.");
    }

    @Test
    @DisplayName("buildResultText() should handle repeated and invalid shots combined")
    void buildResultText3() {
        game.fireShots(List.of(new Position(1, 1), new Position(2, 2), new Position(3, 3)));
        game.fireShots(List.of(new Position(1, 1), new Position(4, 4), new Position(5, 5)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw for repeated and invalid shots.");
    }

    @Test
    @DisplayName("buildResultText() should handle multiple sunk ships")
    void buildResultText4() {
        Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
        Ship ship2 = new Barge(Compass.NORTH, new Position(5, 5));
        game.getMyFleet().addShip(ship1);
        game.getMyFleet().addShip(ship2);
        game.fireShots(List.of(new Position(1, 1), new Position(2, 1), new Position(3, 1)));
        game.fireShots(List.of(new Position(5, 5), new Position(6, 5), new Position(7, 5)));
        assertDoesNotThrow(() -> GamePdfExporter.export(game, testFilePath),
                "Error: export() should not throw when multiple ships are sunk.");
    }
}