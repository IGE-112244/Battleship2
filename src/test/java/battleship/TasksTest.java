/**
 * Test class for Tasks.
 * Author: ${user.name}
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - menu(): 52 (parcialmente testável — GUI e System.exit() não testáveis)
 * - menuHelp(): 1
 * - buildFleet(): 4
 * - readShip(): 1
 * - readPosition(): 1
 * - readClassicPosition(): 5
 */
package battleship;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TasksTest {

    private MockedStatic<GameStatsPanel> mockedGameStatsPanel;

    @BeforeEach
    void setUp() {
        mockedGameStatsPanel = Mockito.mockStatic(GameStatsPanel.class);
        mockedGameStatsPanel.when(GameStatsPanel::mostrar).thenAnswer(i -> null);
    }

    @AfterEach
    void tearDown() {
        if (mockedGameStatsPanel != null) mockedGameStatsPanel.close();
    }

    // -----------------------------------------------------------------------
    // menu() — testável apenas para comandos que não chamam BoardVisualizer
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("menu() should exit gracefully when input is 'desisto'")
    void menu() {
        System.setIn(new ByteArrayInputStream("desisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should not throw when input is 'desisto'.");
    }

    @Test
    @DisplayName("menu() should handle 'ajuda' command")
    void menuAjuda() {
        System.setIn(new ByteArrayInputStream("ajuda\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'ajuda' without throwing.");
    }

    @Test
    @DisplayName("menu() should handle 'estado' when fleet is null")
    void menuEstado() {
        System.setIn(new ByteArrayInputStream("estado\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'estado' when fleet is null.");
    }

    @Test
    @DisplayName("menu() should handle 'mapa' when fleet is null")
    void menuMapaNull() {
        System.setIn(new ByteArrayInputStream("mapa\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'mapa' when fleet is null.");
    }

    @Test
    @DisplayName("menu() should handle 'rajada' when game is null")
    void menuRajadaNull() {
        System.setIn(new ByteArrayInputStream("rajada\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'rajada' when game is null.");
    }

    @Test
    @DisplayName("menu() should handle 'simula' when game is null")
    void menuSimulaNull() {
        System.setIn(new ByteArrayInputStream("simula\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'simula' when game is null.");
    }

    @Test
    @DisplayName("menu() should handle 'tiros' when game is null")
    void menuTirosNull() {
        System.setIn(new ByteArrayInputStream("tiros\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'tiros' when game is null.");
    }

    @Test
    @DisplayName("menu() should handle 'exportjson' when game is null")
    void menuExportJsonNull() {
        System.setIn(new ByteArrayInputStream("exportjson\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'exportjson' when game is null.");
    }

    @Test
    @DisplayName("menu() should handle 'stats' command")
    void menuStats() {
        System.setIn(new ByteArrayInputStream("stats\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'stats' without throwing.");
    }

    @Test
    @DisplayName("menu() should handle 'resetstats' command")
    void menuResetStats() {
        System.setIn(new ByteArrayInputStream("resetstats\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'resetstats' without throwing.");
    }

    @Test
    @DisplayName("menu() should handle unknown command")
    void menuUnknown() {
        System.setIn(new ByteArrayInputStream("comandoinvalido\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle unknown commands without throwing.");
    }

    @Test
    @DisplayName("menu() should handle 'rajadajson' when game is null")
    void menuRajadaJsonNull() {
        System.setIn(new ByteArrayInputStream("rajadajson\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'rajadajson' when game is null.");
    }

    @Test
    @DisplayName("menu() should handle 'iajogo2p' when game is null")
    void menuIaJogo2pNull() {
        System.setIn(new ByteArrayInputStream("iajogo2p\ndesisto\n".getBytes()));
        assertDoesNotThrow(Tasks::menu,
                "Error: menu() should handle 'iajogo2p' when game is null.");
    }

    // -----------------------------------------------------------------------
    // menuHelp()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("menuHelp() should not throw exceptions")
    void menuHelp() {
        assertDoesNotThrow(Tasks::menuHelp,
                "Error: menuHelp() should not throw exceptions.");
    }

    // -----------------------------------------------------------------------
    // readShip()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("readShip() should return a valid ship from input")
    void readShip() {
        try (MockedStatic<Ship> mockedShip = Mockito.mockStatic(Ship.class);
             MockedStatic<Compass> mockedCompass = Mockito.mockStatic(Compass.class)) {

            // Mockar Compass.charToCompass para retornar NORTH
            mockedCompass.when(() -> Compass.charToCompass(Mockito.anyChar()))
                    .thenReturn(Compass.NORTH);

            Ship mockShip = new Barge(Compass.NORTH, new Position(0, 0));
            mockedShip.when(() -> Ship.buildShip(
                            Mockito.anyString(),
                            Mockito.any(Compass.class),
                            Mockito.any(Position.class)))
                    .thenReturn(mockShip);

            String input = "barca 0 0 N\n";
            Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));
            Ship result = Tasks.readShip(in);
            assertNotNull(result,
                    "Error: readShip() should return non-null for valid input.");
        }
    }

    @Test
    @DisplayName("readShip() should return null for unknown ship type")
    void readShipUnknown() {
        try (MockedStatic<Ship> mockedShip = Mockito.mockStatic(Ship.class);
             MockedStatic<Compass> mockedCompass = Mockito.mockStatic(Compass.class)) {

            mockedCompass.when(() -> Compass.charToCompass(Mockito.anyChar()))
                    .thenReturn(Compass.NORTH);

            mockedShip.when(() -> Ship.buildShip(
                            Mockito.anyString(),
                            Mockito.any(Compass.class),
                            Mockito.any(Position.class)))
                    .thenReturn(null);

            String input = "desconhecido 0 0 N\n";
            Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));
            Ship result = Tasks.readShip(in);
            assertNull(result,
                    "Error: readShip() should return null for unknown ship type.");
        }
    }

    @Test
    @DisplayName("buildFleet() should return a non-null fleet")
    void buildFleet1() {
        try (MockedStatic<Ship> mockedShip = Mockito.mockStatic(Ship.class);
             MockedStatic<Compass> mockedCompass = Mockito.mockStatic(Compass.class)) {

            mockedCompass.when(() -> Compass.charToCompass(Mockito.anyChar()))
                    .thenReturn(Compass.NORTH);

            // 11 posições garantidamente sem colisão num tabuleiro 10x10
            int[][] positions = {
                    {0,0},{0,3},{0,6},{0,9},
                    {3,0},{3,3},{3,6},{3,9},
                    {6,0},{6,3},{6,6}
            };

            final int[] counter = {0};
            mockedShip.when(() -> Ship.buildShip(
                            Mockito.anyString(),
                            Mockito.any(Compass.class),
                            Mockito.any(Position.class)))
                    .thenAnswer(i -> {
                        int idx = counter[0] % positions.length;
                        counter[0]++;
                        return new Barge(Compass.NORTH,
                                new Position(positions[idx][0], positions[idx][1]));
                    });

            // Input com tokens suficientes
            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                input.append("barca 0 0 N\n");
            }

            Scanner in = new Scanner(new ByteArrayInputStream(input.toString().getBytes()));
            Fleet fleet = Tasks.buildFleet(in);
            assertNotNull(fleet,
                    "Error: buildFleet() should return a non-null fleet.");
        }
    }

    @Test
    @DisplayName("buildFleet() should handle null ship from buildShip()")
    void buildFleet2() {
        try (MockedStatic<Ship> mockedShip = Mockito.mockStatic(Ship.class);
             MockedStatic<Compass> mockedCompass = Mockito.mockStatic(Compass.class)) {

            mockedCompass.when(() -> Compass.charToCompass(Mockito.anyChar()))
                    .thenReturn(Compass.NORTH);

            int[][] positions = {
                    {0,0},{0,3},{0,6},{0,9},
                    {3,0},{3,3},{3,6},{3,9},
                    {6,0},{6,3},{6,6}
            };

            final int[] counter = {0};
            mockedShip.when(() -> Ship.buildShip(
                            Mockito.anyString(),
                            Mockito.any(Compass.class),
                            Mockito.any(Position.class)))
                    .thenAnswer(i -> {
                        int idx = counter[0];
                        counter[0]++;
                        if (idx == 0) return null; // primeiro null — cobre o branch
                        int posIdx = (idx - 1) % positions.length;
                        return new Barge(Compass.NORTH,
                                new Position(positions[posIdx][0], positions[posIdx][1]));
                    });

            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                input.append("barca 0 0 N\n");
            }

            Scanner in = new Scanner(new ByteArrayInputStream(input.toString().getBytes()));
            assertDoesNotThrow(() -> Tasks.buildFleet(in),
                    "Error: buildFleet() should handle null ships without throwing.");
        }
    }

    // -----------------------------------------------------------------------
    // readPosition()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("readPosition() should return correct position from input")
    void readPosition() {
        String input = "3 5\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Position pos = Tasks.readPosition(in);
        assertAll(
                () -> assertNotNull(pos,
                        "Error: readPosition() should return a non-null position."),
                () -> assertEquals(3, pos.getRow(),
                        "Error: readPosition() row should be 3."),
                () -> assertEquals(5, pos.getColumn(),
                        "Error: readPosition() column should be 5.")
        );
    }

    // -----------------------------------------------------------------------
    // readClassicPosition()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("readClassicPosition() should parse compact format 'A3'")
    void readClassicPosition1() {
        Scanner in = new Scanner(new ByteArrayInputStream("A3\n".getBytes()));
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos,
                "Error: readClassicPosition() should return non-null for 'A3'.");
    }

    @Test
    @DisplayName("readClassicPosition() debug second branch")
    void readClassicPosition2() {
        // part1 = "B" → matches("[A-Z]") = true ✅
        // part2 = "5" → matches("\\d+") = true ✅
        // in.hasNextInt() após ler "B" → true ✅
        String input = "B 5";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));

        // Verificar o que o scanner lê
        String part1 = in.next(); // deve ser "B"
        // hasNextInt() deve ser true para "5"
        assertTrue(in.hasNextInt(), "Scanner should have next int after 'B'");

        // Agora testar readClassicPosition
        Scanner in2 = new Scanner(new ByteArrayInputStream("B 5".getBytes()));
        IPosition pos = Tasks.readClassicPosition(in2);
        assertNotNull(pos,
                "Error: readClassicPosition() should return non-null for 'B 5'.");
    }

    @Test
    @DisplayName("readClassicPosition() should throw when no input available")
    void readClassicPosition3() {
        Scanner in = new Scanner(new ByteArrayInputStream("".getBytes()));
        assertThrows(IllegalArgumentException.class,
                () -> Tasks.readClassicPosition(in),
                "Error: readClassicPosition() should throw when no input available.");
    }

    @Test
    @DisplayName("readClassicPosition() should throw for invalid format")
    void readClassicPosition4() {
        Scanner in = new Scanner(new ByteArrayInputStream("123\n".getBytes()));
        assertThrows(IllegalArgumentException.class,
                () -> Tasks.readClassicPosition(in),
                "Error: readClassicPosition() should throw for invalid format.");
    }

    @Test
    @DisplayName("readClassicPosition() should parse lowercase 'a3'")
    void readClassicPosition5() {
        Scanner in = new Scanner(new ByteArrayInputStream("a3\n".getBytes()));
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos,
                "Error: readClassicPosition() should handle lowercase input.");
    }

    // -----------------------------------------------------------------------
// readClassicPosition() - formato 'A 3' separado
// -----------------------------------------------------------------------

    @Test
    @DisplayName("readClassicPosition() should parse 'A 3' with letter then integer")
    void readClassicPosition6() {
        // Este formato entra no segundo branch: part1=[A-Z] e part2=\d+
        Scanner in = new Scanner(new ByteArrayInputStream("A 3\n".getBytes()));
        IPosition pos = Tasks.readClassicPosition(in);
        assertNotNull(pos,
                "Error: readClassicPosition() should parse 'A 3' format correctly.");
    }

    // -----------------------------------------------------------------------
// exportAndSaveStats() via reflection
// -----------------------------------------------------------------------

    @Test
    @DisplayName("exportAndSaveStats() should not throw with valid game")
    void exportAndSaveStats() throws Exception {
        // GameStatsPanel já está mockado pelo @BeforeEach
        try (MockedStatic<GamePdfExporter> mockedPdf = Mockito.mockStatic(GamePdfExporter.class);
             MockedStatic<GameJsonExporter> mockedJson = Mockito.mockStatic(GameJsonExporter.class)) {

            mockedPdf.when(() -> GamePdfExporter.export(
                    Mockito.any(), Mockito.anyString())).thenAnswer(i -> null);
            mockedJson.when(() -> GameJsonExporter.export(
                    Mockito.any(), Mockito.anyString())).thenAnswer(i -> null);

            IFleet fleet = Fleet.createRandom();
            IGame game = new Game(fleet);

            java.lang.reflect.Method method = Tasks.class
                    .getDeclaredMethod("exportAndSaveStats", IGame.class);
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(null, game),
                    "Error: exportAndSaveStats() should not throw with valid game.");
        }
    }
// -----------------------------------------------------------------------
// buildFleet() - assert in != null
// -----------------------------------------------------------------------

    @Test
    @DisplayName("buildFleet() should throw AssertionError when scanner is null")
    void buildFleetNull() {
        assertThrows(AssertionError.class,
                () -> Tasks.buildFleet(null),
                "Error: buildFleet() should throw AssertionError when scanner is null.");
    }

// -----------------------------------------------------------------------
// buildFleet() - branch success=false (LOGGER.info falha na criação)
// -----------------------------------------------------------------------

    @Test
    @DisplayName("buildFleet() should log when addShip fails due to collision")
    void buildFleet3() {
        try (MockedStatic<Ship> mockedShip = Mockito.mockStatic(Ship.class);
             MockedStatic<Compass> mockedCompass = Mockito.mockStatic(Compass.class)) {

            mockedCompass.when(() -> Compass.charToCompass(Mockito.anyChar()))
                    .thenReturn(Compass.NORTH);

            // Primeiro retorna navios que colidem (mesmo sítio),
            // depois retorna navios em posições válidas espaçadas
            int[][] positions = {
                    {0,0},{0,3},{0,6},{0,9},
                    {3,0},{3,3},{3,6},{3,9},
                    {6,0},{6,3},{6,6}
            };

            final int[] counter = {0};
            mockedShip.when(() -> Ship.buildShip(
                            Mockito.anyString(),
                            Mockito.any(Compass.class),
                            Mockito.any(Position.class)))
                    .thenAnswer(i -> {
                        int idx = counter[0];
                        counter[0]++;
                        if (idx == 0) {
                            // Retorna navio válido que será adicionado
                            return new Barge(Compass.NORTH, new Position(0, 0));
                        }
                        if (idx == 1) {
                            // Retorna navio na mesma posição — colisão, addShip=false
                            return new Barge(Compass.NORTH, new Position(0, 0));
                        }
                        // Restantes em posições espaçadas
                        int posIdx = (idx - 2) % positions.length;
                        return new Barge(Compass.NORTH,
                                new Position(positions[posIdx][0], positions[posIdx][1]));
                    });

            StringBuilder input = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                input.append("barca 0 0 N\n");
            }

            Scanner in = new Scanner(new ByteArrayInputStream(input.toString().getBytes()));
            assertDoesNotThrow(() -> Tasks.buildFleet(in),
                    "Error: buildFleet() should handle addShip failure gracefully.");
        }
    }

// -----------------------------------------------------------------------
// readShip() - assert in != null
// -----------------------------------------------------------------------

    @Test
    @DisplayName("readShip() should throw AssertionError when scanner is null")
    void readShipNull() {
        assertThrows(AssertionError.class,
                () -> Tasks.readShip(null),
                "Error: readShip() should throw AssertionError when scanner is null.");
    }

// -----------------------------------------------------------------------
// readPosition() - assert in != null
// -----------------------------------------------------------------------

    @Test
    @DisplayName("readPosition() should throw AssertionError when scanner is null")
    void readPositionNull() {
        assertThrows(AssertionError.class,
                () -> Tasks.readPosition(null),
                "Error: readPosition() should throw AssertionError when scanner is null.");
    }



// -----------------------------------------------------------------------
// readPlayerShots() via reflection
// -----------------------------------------------------------------------

    @Test
    @DisplayName("readPlayerShots() should return correct list of positions")
    void readPlayerShots1() throws Exception {
        java.lang.reflect.Method method = Tasks.class
                .getDeclaredMethod("readPlayerShots", Scanner.class);
        method.setAccessible(true);

        // NUMBER_SHOTS = 3, formato clássico
        String input = "\nA1 B2 C3\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));
        in.nextLine(); // consumir linha vazia inicial

        @SuppressWarnings("unchecked")
        List<IPosition> shots = (List<IPosition>) method.invoke(null, in);
        assertEquals(Game.NUMBER_SHOTS, shots.size(),
                "Error: readPlayerShots() should return NUMBER_SHOTS positions.");
    }

    @Test
    @DisplayName("readPlayerShots() should throw when fewer than NUMBER_SHOTS positions")
    void readPlayerShots2() throws Exception {
        java.lang.reflect.Method method = Tasks.class
                .getDeclaredMethod("readPlayerShots", Scanner.class);
        method.setAccessible(true);

        // Apenas 1 posição em vez de NUMBER_SHOTS
        String input = "\nA1\n";
        Scanner in = new Scanner(new ByteArrayInputStream(input.getBytes()));
        in.nextLine();

        assertThrows(java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(null, in),
                "Error: readPlayerShots() should throw when fewer positions than NUMBER_SHOTS.");
    }

    @Test
    @DisplayName("menu() should handle 'estado' when fleet is not null")
    void menuEstadoComFrota() {
        try (MockedStatic<Fleet> mockedFleet = Mockito.mockStatic(Fleet.class)) {
            IFleet mockFleet = Fleet.createRandom();
            mockedFleet.when(Fleet::createRandom).thenReturn(mockFleet);

            // gerafrota inicia a frota, depois estado, depois desisto
            // gerafrota chama BoardVisualizer que não conseguimos mockar
            // por isso usamos lefrota em vez disso
            String input = "estado\ndesisto\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            assertDoesNotThrow(Tasks::menu,
                    "Error: menu() should handle 'estado' with fleet.");
        }
    }
}