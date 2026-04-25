/**
 * Test class for Main.
 * Author: ${user.name}
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - main(): 1
 */
package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
    }

    @AfterEach
    void tearDown() {
        main = null;
    }

    /**
     * Test for Main instantiation.
     * Cyclomatic Complexity: 1
     */
    @Test
    @DisplayName("Main class should be instantiable without throwing exceptions")
    void testMainInstantiation() {
        assertNotNull(main,
                "Error: Main instance should not be null.");
    }

    /**
     * Test for main() method existence.
     * Note: main() calls Tasks.menu() which requires user input,
     * making full execution untestable in a standard unit test context.
     */
    @Test
    @DisplayName("Main class should have a public static main method")
    void testMainMethodExists() {
        assertDoesNotThrow(() ->
                        Main.class.getMethod("main", String[].class),
                "Error: Main class should have a public static main method.");
    }

    @Test
    @DisplayName("main() should execute without throwing exceptions when input is 'desisto'")
    void testMain() {
        // Redirecionar System.in para simular o comando 'desisto'
        String input = "desisto\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));

        // Redirecionar System.out para evitar output no ecrã
        java.io.PrintStream originalOut = System.out;
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            public void write(int b) {} // descartar output
        }));

        try {
            assertDoesNotThrow(() -> Main.main(new String[]{}),
                    "Error: main() should not throw exceptions when input is 'desisto'.");
        } finally {
            // Restaurar System.in e System.out originais
            System.setIn(System.in);
            System.setOut(originalOut);
        }
    }
}