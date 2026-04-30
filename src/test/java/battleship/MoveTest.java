package battleship;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Test class for Move.
 * Author: 122989
 * Date: 2026-04-29 14:06
 *
 * Cyclomatic Complexity:
 * - constructor: 1
 * - toString(): 1
 * - getNumber(): 1
 * - getShots(): 1
 * - getShotResults(): 1
 * - processEnemyFire(): 10
 */

class MoveTest {
    private Move move;
    private List<IPosition> shots;
    private List<IGame.ShotResult> results;


    // Mock simples de IShip
    static class MockShip implements IShip {

        private final String category;

        MockShip(String category) {
            this.category = category;
        }

        @Override
        public String getCategory() {
            return category;
        }

        @Override
        public Integer getSize() {
            return 3;
        }

        @Override
        public List<IPosition> getPositions() {
            return new ArrayList<>();
        }

        @Override
        public List<IPosition> getAdjacentPositions() {
            return new ArrayList<>();
        }

        @Override
        public IPosition getPosition() {
            return new Position(0, 0);
        }

        @Override
        public Compass getBearing() {
            return Compass.NORTH;
        }

        @Override
        public boolean stillFloating() {
            return true;
        }

        @Override
        public int getTopMostPos() {
            return 0;
        }

        @Override
        public int getBottomMostPos() {
            return 0;
        }

        @Override
        public int getLeftMostPos() {
            return 0;
        }

        @Override
        public int getRightMostPos() {
            return 0;
        }

        @Override
        public boolean occupies(IPosition pos) {
            return false;
        }

        @Override
        public boolean tooCloseTo(IShip other) {
            return false;
        }

        @Override
        public boolean tooCloseTo(IPosition pos) {
            return false;
        }

        @Override
        public void shoot(IPosition pos) {}

        @Override
        public void sink() {}
    }

    private IGame.ShotResult shot(boolean valid, boolean repeated, IShip ship, boolean sunk) {
        return new IGame.ShotResult(valid, repeated, ship, sunk);
    }

    @BeforeEach
    void setUp() {
        shots = new ArrayList<>();
        results = new ArrayList<>();
        move = new Move(1, shots, results);
    }

    @AfterEach
    void tearDown() {
        move = null;
        shots = null;
        results = null;
    }

    // ---------------------------------------------------------
    // constructor — CC = 1
    // ---------------------------------------------------------
    @Test
    @DisplayName("constructor(): inicializa campos corretamente")
    void constructor() {
        List<IPosition> s = new ArrayList<>();
        List<IGame.ShotResult> r = new ArrayList<>();
        Move m = new Move(3, s, r);
        assertAll(
                () -> assertNotNull(m,            "Erro: Move não deveria ser null"),
                () -> assertEquals(3, m.getNumber(), "Erro: número deveria ser 3"),
                () -> assertSame(s, m.getShots(),      "Erro: lista de shots não corresponde"),
                () -> assertSame(r, m.getShotResults(), "Erro: lista de results não corresponde")
        );
    }

    // ---------------------------------------------------------
    // toString(), getNumber(), getShots(), getShotResults() — CC = 1 cada
    // ---------------------------------------------------------
    @Test
    @DisplayName("toString(): formato contém número e tamanhos")
    void toStringTest() {
        String str = move.toString();
        assertAll(
                () -> assertTrue(str.contains("number=1"),  "Erro: toString deveria conter 'number=1'"),
                () -> assertTrue(str.contains("shots=0"),   "Erro: toString deveria indicar 0 shots"),
                () -> assertTrue(str.contains("results=0"), "Erro: toString deveria indicar 0 results")
        );
    }

    @Test
    @DisplayName("getNumber(): devolve o número correto")
    void getNumber() {
        assertEquals(1, move.getNumber(), "Erro: getNumber deveria devolver 1");
    }

    @Test
    @DisplayName("getShots(): devolve a lista de tiros")
    void getShots() {
        assertSame(shots, move.getShots(), "Erro: getShots deveria devolver a mesma lista");
    }

    @Test
    @DisplayName("getShotResults(): devolve a lista de resultados")
    void getShotResults() {
        assertSame(results, move.getShotResults(), "Erro: getShotResults deveria devolver a mesma lista");
    }

    // ---------------------------------------------------------
    // processEnemyFire() — CC alta → vários testes independentes
    // ---------------------------------------------------------

    @Test
    @DisplayName("processEnemyFire1(): sem resultados → tudo zero, outsideShots=NUMBER_SHOTS")
    void processEnemyFire1() {
        // Branch E=false; loop never entered
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 0"),
                        "Erro: validShots deveria ser 0"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 0"),
                        "Erro: repeatedShots deveria ser 0"),
                () -> assertTrue(json.contains("\"missedShots\" : 0"),
                        "Erro: missedShots deveria ser 0"),
                () -> assertTrue(json.contains("\"outsideShots\" : " + Game.NUMBER_SHOTS),
                        "Erro: outsideShots deveria ser NUMBER_SHOTS")
        );
    }

    @Test
    @DisplayName("processEnemyFire2(): tiros inválidos (A=false) são ignorados")
    void processEnemyFire2() {
        // Branch A=false — the 'continue' path
        results.add(shot(false, false, null, false));
        results.add(shot(false, true,  null, false));
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 0"),
                        "Erro: tiros inválidos não devem contar como válidos"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 0"),
                        "Erro: tiros inválidos não devem contar como repetidos"),
                () -> assertTrue(json.contains("\"missedShots\" : 0"),
                        "Erro: tiros inválidos não devem contar como misses")
        );
    }

    @Test
    @DisplayName("processEnemyFire3(): tiros repetidos (B=true) incrementam repeatedShots")
    void processEnemyFire3() {
        // Branch B=true
        results.add(shot(true, true, null, false));
        results.add(shot(true, true, null, false));
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 0"),
                        "Erro: tiros repetidos não contam como válidos"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 2"),
                        "Erro: repeatedShots deveria ser 2")
        );
    }

    @Test
    @DisplayName("processEnemyFire4(): tiros na água (B=false, C=true) contam em missedShots")
    void processEnemyFire4() {
        // Branch B=false, C=true
        results.add(shot(true, false, null, false));
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 2"),
                        "Erro: validShots deveria ser 2"),
                () -> assertTrue(json.contains("\"missedShots\" : 2"),
                        "Erro: missedShots deveria ser 2"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 0"),
                        "Erro: repeatedShots deveria ser 0")
        );
    }

    @Test
    @DisplayName("processEnemyFire5(): hits sem afundar (B=false, C=false, D=false)")
    void processEnemyFire5() {
        // Branch C=false, D=false
        IShip ship = new MockShip("Destroyer");
        results.add(shot(true, false, ship, false));
        results.add(shot(true, false, ship, false));
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 2"),
                        "Erro: validShots deveria ser 2"),
                () -> assertTrue(json.contains("\"sunkBoats\" : [ ]"),
                        "Erro: não deveria haver barcos afundados"),
                () -> assertTrue(json.contains("\"hitsOnBoats\""),
                        "Erro: deveria haver hitsOnBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire6(): navio afundado (D=true) aparece em sunkBoats")
    void processEnemyFire6() {
        // Branch D=true
        IShip ship = new MockShip("Cruiser");
        results.add(shot(true, false, ship, true));
        String json = move.processEnemyFire(false);
        assertAll(
                () -> assertTrue(json.contains("\"type\" : \"Cruiser\""),
                        "Erro: tipo de navio afundado deveria ser Cruiser"),
                () -> assertTrue(json.contains("\"sunkBoats\""),
                        "Erro: JSON deveria conter sunkBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire7(): null em shotResults lança NullPointerException")
    void processEnemyFire7() {
        Move m = new Move(2, new ArrayList<>(), null);
        assertThrows(NullPointerException.class, () -> m.processEnemyFire(false),
                "Erro: deveria lançar NullPointerException quando shotResults é null");
    }

    @Test
    @DisplayName("processEnemyFire8(v): F=true, 1 repetido → singular 'tiro repetido'")
    void processEnemyFire8() {
        // Branch F=true, V=false (repeatedShots==1, not >1)
        results.add(shot(true, true, null, false));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"repeatedShots\" : 1"),
                        "Erro: repeatedShots deveria ser 1"),
                () -> assertTrue(json.contains("\"validShots\" : 0"),
                        "Erro: validShots deveria ser 0")
        );
    }

    @Test
    @DisplayName("processEnemyFire9(v): F=true, 2 repetidos → plural 'tiros repetidos'")
    void processEnemyFire9() {
        // Branch F=true, V=true (repeatedShots>1)
        results.add(shot(true, true, null, false));
        results.add(shot(true, true, null, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"repeatedShots\" : 2"),
                "Erro: repeatedShots deveria ser 2");
    }

    @Test
    @DisplayName("processEnemyFire10(v): G=true, U=false — 1 tiro válido (singular)")
    void processEnemyFire10() {
        // Branch G=true, U=false (validShots==1)
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"),
                        "Erro: validShots deveria ser 1"),
                () -> assertTrue(json.contains("\"missedShots\" : 1"),
                        "Erro: missedShots deveria ser 1")
        );
    }

    @Test
    @DisplayName("processEnemyFire11(v): G=true, U=true — 2 tiros válidos (plural)")
    void processEnemyFire11() {
        // Branch G=true, U=true (validShots>1)
        results.add(shot(true, false, null, false));
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"validShots\" : 2"),
                "Erro: validShots deveria ser 2");
    }

    @Test
    @DisplayName("processEnemyFire12(v): G=false, F=false — sem válidos e sem repetidos (output vazio antes de outsideShots)")
    void processEnemyFire12() {
        // Branch G=false AND F=false: only invalid shots → validShots=0, repeatedShots=0
        // outsideShots>0 with output.isEmpty()=true → branch S=false
        results.add(shot(false, false, null, false)); // invalid, ignored
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 0"),
                        "Erro: validShots deveria ser 0"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 0"),
                        "Erro: repeatedShots deveria ser 0")
        );
    }

    @Test
    @DisplayName("processEnemyFire13(v): H=true, I=false — 1 navio afundado (singular 'ao fundo')")
    void processEnemyFire13() {
        // Branch H=true, I=false (count==1)
        IShip ship = new MockShip("Fragata");
        results.add(shot(true, false, ship, true));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"),
                        "Erro: validShots deveria ser 1"),
                () -> assertTrue(json.contains("\"type\" : \"Fragata\""),
                        "Erro: tipo Fragata deveria aparecer em sunkBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire14(v): H=true, I=true — 2 do mesmo tipo afundados (plural 'ao fundo')")
    void processEnemyFire14() {
        // Branch H=true, I=true (count>1 for same type)
        IShip ship = new MockShip("Barca");
        results.add(shot(true, false, ship, true));
        results.add(shot(true, false, ship, true));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"count\" : 2"),
                        "Erro: count deveria ser 2 para Barca"),
                () -> assertTrue(json.contains("\"type\" : \"Barca\""),
                        "Erro: tipo Barca deveria aparecer em sunkBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire15(v): H=false — nenhum navio afundado (sunkBoats vazio)")
    void processEnemyFire15() {
        // Branch H=false: hit but not sunk
        IShip ship = new MockShip("Nau");
        results.add(shot(true, false, ship, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"sunkBoats\" : [ ]"),
                "Erro: sunkBoats deveria estar vazio");
    }

    @Test
    @DisplayName("processEnemyFire16(v): J=true, K=true, L=false — 1 hit em navio não afundado (singular)")
    void processEnemyFire16() {
        // Branch J=true, K=true (boat not sunk), L=false (hits==1)
        IShip ship = new MockShip("Caravela");
        results.add(shot(true, false, ship, false));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"hitsOnBoats\""),
                        "Erro: deveria haver hitsOnBoats para Caravela"),
                () -> assertTrue(json.contains("\"type\" : \"Caravela\""),
                        "Erro: tipo Caravela deveria aparecer em hitsOnBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire17(v): J=true, K=true, L=true — 2 hits em navio não afundado (plural)")
    void processEnemyFire17() {
        // Branch J=true, K=true, L=true (hits>1)
        IShip ship = new MockShip("Galeão");
        results.add(shot(true, false, ship, false));
        results.add(shot(true, false, ship, false));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"hits\" : 2"),
                        "Erro: hits deveria ser 2 para Galeão"),
                () -> assertTrue(json.contains("\"type\" : \"Galeão\""),
                        "Erro: tipo Galeão deveria aparecer em hitsOnBoats")
        );
    }

    @Test
    @DisplayName("processEnemyFire18(v): J=true, K=false — hit em navio afundado não aparece em hitsOnBoats")
    void processEnemyFire18() {
        // Branch K=false: boat is sunk → entry skipped from hitsPerBoat display
        IShip ship = new MockShip("Fragata");
        results.add(shot(true, false, ship, true));
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"sunkBoats\""),
                        "Erro: Fragata afundada deveria aparecer em sunkBoats"),
                () -> assertTrue(json.contains("\"hitsOnBoats\" : [ ]"),
                        "Erro: hitsOnBoats deveria estar vazio quando navio foi afundado")
        );
    }

    @Test
    @DisplayName("processEnemyFire19(v): J=false — sem hits em navios")
    void processEnemyFire19() {
        // Branch J=false: only water misses
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"hitsOnBoats\" : [ ]"),
                "Erro: hitsOnBoats deveria estar vazio quando só houve tiros na água");
    }

    @Test
    @DisplayName("processEnemyFire20(v): M=true, W=false — 1 tiro na água (singular)")
    void processEnemyFire20() {
        // Branch M=true, W=false (missedShots==1)
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"missedShots\" : 1"),
                "Erro: missedShots deveria ser 1");
    }

    @Test
    @DisplayName("processEnemyFire21(v): M=true, W=true — 2 tiros na água (plural)")
    void processEnemyFire21() {
        // Branch M=true, W=true (missedShots>1)
        results.add(shot(true, false, null, false));
        results.add(shot(true, false, null, false));
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"missedShots\" : 2"),
                "Erro: missedShots deveria ser 2");
    }

    @Test
    @DisplayName("processEnemyFire22(v): O=true, P=true — repetidos com válidos → vírgula de separação")
    void processEnemyFire22() {
        // Branch O=true, P=true (validShots>0 AND repeatedShots>0)
        IShip ship = new MockShip("Nau");
        results.add(shot(true, false, ship, false)); // valid hit
        results.add(shot(true, true,  null, false)); // repeated
        String json = move.processEnemyFire(true);
        assertAll(
                () -> assertTrue(json.contains("\"validShots\" : 1"),
                        "Erro: validShots deveria ser 1"),
                () -> assertTrue(json.contains("\"repeatedShots\" : 1"),
                        "Erro: repeatedShots deveria ser 1")
        );
    }

    @Test
    @DisplayName("processEnemyFire23(v): O=true, Q=true — 2 tiros repetidos (plural) com válidos presentes")
    void processEnemyFire23() {
        // Branch O=true, Q=true (repeatedShots>1)
        results.add(shot(true, false, null, false)); // valid miss
        results.add(shot(true, true,  null, false)); // repeated
        results.add(shot(true, true,  null, false)); // repeated
        String json = move.processEnemyFire(true);
        assertTrue(json.contains("\"repeatedShots\" : 2"),
                "Erro: repeatedShots deveria ser 2");
    }

}