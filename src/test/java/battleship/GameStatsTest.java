package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for GameStats.
 * Author: 122989
 * Date: 2026-04-29 13:43
 *
 * Cyclomatic Complexity:
 * - constructor: 1
 * - update(): 4
 * - getAccuracy(): 2
 * - reset(): 1
 * - getters/setters: 1 each
 */
class GameStatsTest {

    private GameStats stats;

    // Mock para IMove
    static class MockMove implements IMove {

        @Override
        public int getNumber() {
            return 0;
        }

        @Override
        public List<IPosition> getShots() {
            return Collections.emptyList();
        }

        @Override
        public List<IGame.ShotResult> getShotResults() {
            return Collections.emptyList();
        }

        @Override
        public String processEnemyFire(boolean verbose) {
            return "mock";
        }

        @Override
        public String toString() {
            return "MockMove";
        }
    }

    static class MockGame implements IGame {

        private final int hits;
        private final int invalidShots;
        private final int repeatedShots;
        private final int alienMovesSize;

        MockGame(int hits, int invalidShots, int repeatedShots, int alienMovesSize) {
            this.hits = hits;
            this.invalidShots = invalidShots;
            this.repeatedShots = repeatedShots;
            this.alienMovesSize = alienMovesSize;
        }

        @Override
        public String randomEnemyFire() { return "mock"; }

        @Override
        public String readEnemyFire(Scanner in) { return "mock"; }

        @Override
        public void fireShots(List<IPosition> shots) {}

        @Override
        public ShotResult fireSingleShot(IPosition pos, boolean isRepeated) {
            return new ShotResult(true, false, null, false);
        }

        @Override
        public IFleet getMyFleet() { return null; }

        @Override
        public List<IMove> getAlienMoves() {
            return Collections.nCopies(alienMovesSize, new MockMove());
        }

        @Override
        public IFleet getAlienFleet() { return null; }

        @Override
        public List<IMove> getMyMoves() { return Collections.emptyList(); }

        @Override
        public int getRepeatedShots() { return repeatedShots; }

        @Override
        public int getInvalidShots() { return invalidShots; }

        @Override
        public int getHits() { return hits; }

        @Override
        public int getSunkShips() { return 0; }

        @Override
        public int getRemainingShips() { return 0; }

        @Override
        public void printMyBoard(boolean show_shots, boolean show_legend) {}

        @Override
        public void printAlienBoard(boolean show_shots, boolean show_legend) {}

        @Override
        public void over() {}
    }

    @BeforeEach
    void setUp() {
        stats = new GameStats();
    }

    @AfterEach
    void tearDown() {
        stats = null;
    }

    // ---------------------------------------------------------
    // update() — CC = 4 → 4 testes
    // ---------------------------------------------------------

    @Test
    @DisplayName("update1(): jogador ganhou")
    void update1() {
        MockGame game = new MockGame(5, 1, 1, 3);

        stats.update(game, true);

        int expectedTiros = 3 * Game.NUMBER_SHOTS - 1 - 1;

        assertAll(
                () -> assertEquals(1, stats.getTotalJogos(), "Erro: totalJogos deveria ser 1"),
                () -> assertEquals(1, stats.getJogosGanhos(), "Erro: jogosGanhos deveria ser 1"),
                () -> assertEquals(expectedTiros, stats.getTotalTiros(), "Erro no cálculo de tiros"),
                () -> assertEquals(5, stats.getTirosAcertados(), "Erro nos tiros acertados")
        );
    }

    @Test
    @DisplayName("update2(): jogador perdeu")
    void update2() {
        MockGame game = new MockGame(2, 0, 0, 2);

        stats.update(game, false);

        int expectedTiros = 2 * Game.NUMBER_SHOTS;

        assertAll(
                () -> assertEquals(1, stats.getTotalJogos(), "Erro: totalJogos deveria ser 1"),
                () -> assertEquals(0, stats.getJogosGanhos(), "Erro: jogosGanhos deveria ser 0"),
                () -> assertEquals(expectedTiros, stats.getTotalTiros(), "Erro no cálculo de tiros"),
                () -> assertEquals(2, stats.getTirosAcertados(), "Erro nos tiros acertados")
        );
    }

    @Test
    @DisplayName("update3(): tiros inválidos e repetidos máximos")
    void update3() {
        MockGame game = new MockGame(1, 5, 5, 5);

        stats.update(game, false);

        int expectedTiros = 5 * Game.NUMBER_SHOTS - 5 - 5;

        assertEquals(expectedTiros, stats.getTotalTiros(),
                "Erro: cálculo incorreto com muitos tiros inválidos/repetidos");
    }

    @Test
    @DisplayName("update4(): nenhum movimento do inimigo")
    void update4() {
        MockGame game = new MockGame(0, 0, 0, 0);

        stats.update(game, false);

        assertEquals(0, stats.getTotalTiros(),
                "Erro: sem movimentos, totalTiros deveria ser 0");
    }

    // ---------------------------------------------------------
    // getAccuracy() — CC = 2 → 2 testes
    // ---------------------------------------------------------

    @Test
    @DisplayName("getAccuracy1(): sem tiros → precisão 0")
    void getAccuracy1() {
        assertEquals(0.0, stats.getAccuracy(),
                "Erro: precisão deveria ser 0 quando totalTiros = 0");
    }

    @Test
    @DisplayName("getAccuracy2(): precisão normal")
    void getAccuracy2() {
        stats.setTotalTiros(10);
        stats.setTirosAcertados(4);

        assertEquals(40.0, stats.getAccuracy(),
                "Erro: precisão deveria ser 40%");
    }

    // ---------------------------------------------------------
    // reset() — CC = 1 → 1 teste
    // ---------------------------------------------------------

    @Test
    @DisplayName("reset(): repõe tudo a zero")
    void reset() {
        stats.setTotalJogos(5);
        stats.setJogosGanhos(3);
        stats.setTotalTiros(20);
        stats.setTirosAcertados(10);

        stats.reset();

        assertAll(
                () -> assertEquals(0, stats.getTotalJogos(), "Erro: totalJogos deveria ser 0"),
                () -> assertEquals(0, stats.getJogosGanhos(), "Erro: jogosGanhos deveria ser 0"),
                () -> assertEquals(0, stats.getTotalTiros(), "Erro: totalTiros deveria ser 0"),
                () -> assertEquals(0, stats.getTirosAcertados(), "Erro: tirosAcertados deveria ser 0")
        );
    }

    // ---------------------------------------------------------
    // Getters e Setters — CC = 1 cada
    // ---------------------------------------------------------

    @Test
    @DisplayName("gettersAndSetters(): valida todos os getters e setters")
    void gettersAndSetters() {
        stats.setTotalJogos(7);
        stats.setJogosGanhos(4);
        stats.setTotalTiros(30);
        stats.setTirosAcertados(12);

        assertAll(
                () -> assertEquals(7, stats.getTotalJogos(), "Erro no getter totalJogos"),
                () -> assertEquals(4, stats.getJogosGanhos(), "Erro no getter jogosGanhos"),
                () -> assertEquals(30, stats.getTotalTiros(), "Erro no getter totalTiros"),
                () -> assertEquals(12, stats.getTirosAcertados(), "Erro no getter tirosAcertados")
        );
    }
}