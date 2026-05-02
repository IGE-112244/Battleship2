package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for Ship.
 * Author: IGE-122989
 * Date: 2026-04-29
 * Time: 14:50
 * Cyclomatic Complexity for each method:
 * - buildShip: 6  (switch: 5 cases + default)
 * - Constructor: 1
 * - getCategory: 1
 * - getSize: 1
 * - getBearing: 1
 * - getPosition: 1
 * - getPositions: 1
 * - getAdjacentPositions: 3
 * - stillFloating: 2
 * - shoot: 2
 * - sink: 1
 * - occupies: 2
 * - tooCloseTo (IShip): 2
 * - tooCloseTo (IPosition): 2
 * - getTopMostPos: 2
 * - getBottomMostPos: 2
 * - getLeftMostPos: 2
 * - getRightMostPos: 2
 * - toString: 1
 */
public class ShipTest {

    private Ship ship;

    static class TestShip extends Ship {
        public TestShip(String category, Compass bearing, IPosition pos, int size) {
            super(category, bearing, pos, size);
            for (int i = 0; i < size; i++) {
                positions.add(new Position(pos.getRow() + i, pos.getColumn()));
            }
        }
    }

    @BeforeEach
    void setUp() {
        // Since Ship is abstract, instantiate it with a concrete subclass (e.g., Barge)
        ship = new Barge(Compass.NORTH, new Position(5, 5));
    }

    @AfterEach
    void tearDown() {
        ship = null;
    }

    /**
     * Test for the constructor.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testConstructor() {
        assertNotNull(ship, "Error: Ship instance should not be null.");
        assertEquals("Barca", ship.getCategory(), "Error: Ship category is incorrect.");
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing is incorrect.");
        assertEquals(1, ship.getSize(), "Error: Ship size is incorrect.");
        assertFalse(ship.getPositions().isEmpty(), "Error: Ship positions should not be empty.");
    }

    /**
     * Test for the getCategory method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetCategory() {
        assertEquals("Barca", ship.getCategory(), "Error: Ship category should be 'Barca'.");
    }

    /**
     * Test for the getSize method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetSize() {
        assertEquals(1, ship.getSize(), "Error: Ship size should be 1.");
    }

    /**
     * Test for the getBearing method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetBearing() {
        assertEquals(Compass.NORTH, ship.getBearing(), "Error: Ship bearing should be NORTH.");
    }

    /**
     * Test for the getPositions method.
     * Cyclomatic Complexity: 1
     */
    @Test
    void testGetPositions() {
        List<IPosition> positions = ship.getPositions();
        assertNotNull(positions, "Error: Ship positions should not be null.");
        assertEquals(1, positions.size(), "Error: Ship should have exactly one position.");
        assertEquals(5, positions.get(0).getRow(), "Error: Position's row should be 5.");
        assertEquals(5, positions.get(0).getColumn(), "Error: Position's column should be 5.");
    }

    /**
     * Test for the stillFloating method (all positions intact).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testStillFloating1() {
        assertTrue(ship.stillFloating(), "Error: Ship should still be floating.");
    }

    /**
     * Test for the stillFloating method (all positions hit).
     */
    @Test
    void testStillFloating2() {
        ship.getPositions().get(0).shoot();
        assertFalse(ship.stillFloating(), "Error: Ship should no longer be floating after being hit.");
    }

    /**
     * Test for the shoot method (valid position).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testShoot1() {
        Position target = new Position(5, 5);
        ship.shoot(target);
        assertTrue(ship.getPositions().get(0).isHit(), "Error: Position should be marked as hit.");
    }

    /**
     * Test for the shoot method (invalid position).
     */
    @Test
    void testShoot2() {
        Position target = new Position(0, 0);
        ship.shoot(target); // No exception expected
        assertFalse(ship.getPositions().get(0).isHit(), "Error: Position should not be marked as hit for an invalid target.");
    }

    /**
     * Test for the occupies method (position occupied).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testOccupies1() {
        Position pos = new Position(5, 5);
        assertTrue(ship.occupies(pos), "Error: Ship should occupy position (5, 5).");
    }

    /**
     * Test for the occupies method (position not occupied).
     */
    @Test
    void testOccupies2() {
        Position pos = new Position(1, 1);
        assertFalse(ship.occupies(pos), "Error: Ship should not occupy position (1, 1).");
    }

    /**
     * Test for the tooCloseTo method with another IShip (ships too close).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testTooCloseToShip1() {
        Ship nearbyShip = new Barge(Compass.NORTH, new Position(5, 6));
        assertTrue(ship.tooCloseTo(nearbyShip), "Error: Ships should be too close.");
    }

    /**
     * Test for the tooCloseTo method with another IShip (ships not close).
     */
    @Test
    void testTooCloseToShip2() {
        Ship farShip = new Barge(Compass.NORTH, new Position(10, 10));
        assertFalse(ship.tooCloseTo(farShip), "Error: Ships should not be too close.");
    }

    /**
     * Test for the tooCloseTo method with an IPosition (positions adjacent).
     * Cyclomatic Complexity: 2
     */
    @Test
    void testTooCloseToPosition1() {
        Position pos = new Position(5, 6); // Adjacent position
        assertTrue(ship.tooCloseTo(pos), "Error: Ship should be too close to the given position.");
    }

    /**
     * Test for the tooCloseTo method with an IPosition (positions not adjacent).
     */
    @Test
    void testTooCloseToPosition2() {
        Position pos = new Position(7, 7); // Non-adjacent position
        assertFalse(ship.tooCloseTo(pos), "Error: Ship should not be too close to the given position.");
    }

    /**
     * Test for the getTopMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetTopMostPos() {
        assertEquals(5, ship.getTopMostPos(), "Error: The topmost position should be 5.");
    }

    /**
     * Test for the getBottomMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetBottomMostPos() {
        assertEquals(5, ship.getBottomMostPos(), "Error: The bottommost position should be 5.");
    }

    /**
     * Test for the getLeftMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetLeftMostPos() {
        assertEquals(5, ship.getLeftMostPos(), "Error: The leftmost position should be 5.");
    }

    /**
     * Test for the getRightMostPos method.
     * Cyclomatic Complexity: 2
     */
    @Test
    void testGetRightMostPos() {
        assertEquals(5, ship.getRightMostPos(), "Error: The rightmost position should be 5.");
    }

    @Test
    @DisplayName("sink(): todas as posições devem ficar atingidas")
    void sink() {
        ship.sink();
        for (IPosition p : ship.getPositions()) {
            assertTrue(p.isHit(), "Error: All ship positions should be hit after sink().");
        }
    }

    @Test
    @DisplayName("occupies(null): deve lançar AssertionError")
    void occupies() {
        assertThrows(AssertionError.class, () -> ship.occupies(null),
                "Error: occupies(null) should throw AssertionError.");
    }

    @Test
    @DisplayName("tooCloseTo(IShip=null): deve lançar AssertionError")
    void tooCloseTo1() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IShip) null),
                "Error: tooCloseTo(null) should throw AssertionError.");
    }

    @Test
    @DisplayName("tooCloseTo(IPosition=null): deve lançar AssertionError")
    void tooCloseTo2() {
        assertThrows(AssertionError.class, () -> ship.tooCloseTo((IPosition) null),
                "Error: tooCloseTo(null) should throw AssertionError.");
    }

    @Test
    @DisplayName("shoot(null): deve lançar AssertionError")
    void shoot3() {
        assertThrows(AssertionError.class, () -> ship.shoot(null),
                "Error: shoot(null) should throw AssertionError.");
    }

    @Test
    @DisplayName("shoot4(): posição fora do board deve lançar AssertionError")
    void shoot4() {
        assertThrows(AssertionError.class,
                () -> ship.shoot(new Position(-1, -1)),
                "Error: shoot() with position outside board should throw AssertionError.");
    }

    @Test
    @DisplayName("toString(): deve conter categoria, orientação e posição")
    void toString1() {
        String str = ship.toString();
        assertAll(
                () -> assertTrue(str.contains(ship.getCategory()), "Error: toString() should contain the ship category."),
                () -> assertTrue(str.contains(ship.getBearing().toString()), "Error: toString() should contain the ship bearing."),
                () -> assertTrue(str.contains(ship.getPosition().toString()), "Error: toString() should contain the ship position.")
        );
    }

    @Test
    @DisplayName("getTopMostPos2(): navio grande deve devolver a linha mais alta")
    void getTopMostPos2() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 5), 3);
        assertEquals(5, s.getTopMostPos(),
                "Error: topmost position should be 5.");
    }

    @Test
    @DisplayName("getTopMostPos3(): posição posterior com row menor → branch if=true")
    void getTopMostPos3() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(7, 5), 2);
        // TestShip adiciona (7,5) e (8,5) — substitui a segunda por (5,5)
        s.positions.set(1, new Position(5, 5));
        assertEquals(5, s.getTopMostPos(),
                "Error: topmost row should be 5 when later position has smaller row.");
    }

    @Test
    @DisplayName("getBottomMostPos2(): navio grande deve devolver a linha mais baixa")
    void getBottomMostPos2() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 5), 3);
        assertEquals(7, s.getBottomMostPos(),
                "Error: bottommost position should be 7.");
    }

    @Test
    @DisplayName("getBottomMostPos3(): posição posterior com row menor → branch if=false")
    void getBottomMostPos3() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(7, 5), 2);
        s.positions.set(1, new Position(5, 5));
        assertEquals(7, s.getBottomMostPos(),
                "Error: bottommost row should remain 7.");
    }

    @Test
    @DisplayName("getLeftMostPos2(): navio vertical deve devolver coluna correta")
    void getLeftMostPos2() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 5), 3);
        assertEquals(5, s.getLeftMostPos(),
                "Error: leftmost column should be 5.");
    }

    @Test
    @DisplayName("getLeftMostPos3(): posição posterior com coluna menor → branch if=true")
    void getLeftMostPos3() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 7), 2);
        s.positions.set(1, new Position(5, 3));
        assertEquals(3, s.getLeftMostPos(),
                "Error: leftmost column should be 3 when later position has smaller column.");
    }

    @Test
    @DisplayName("getRightMostPos2(): navio vertical deve devolver coluna correta")
    void getRightMostPos2() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 5), 3);
        assertEquals(5, s.getRightMostPos(),
                "Error: rightmost column should be 5.");
    }

    @Test
    @DisplayName("getRightMostPos3(): posição posterior com coluna maior → branch if=true")
    void getRightMostPos3() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5, 3), 2);
        s.positions.set(1, new Position(5, 7));
        assertEquals(7, s.getRightMostPos(),
                "Error: rightmost column should be 7 when later position has larger column.");
    }

    @Test
    @DisplayName("getAdjacentPositions(): navio grande deve ter adjacentes adicionais")
    void getAdjacentPositions() {
        Ship s = new TestShip("X", Compass.NORTH, new Position(5,5), 3);
        List<IPosition> adj = s.getAdjacentPositions();
        assertTrue(adj.size() > 4, "Error: large ship should have more adjacent positions.");
    }

    @Test
    @DisplayName("buildShip1(): 'barca' deve criar instância de Barge")
    void buildShip1() {
        Ship s = Ship.buildShip("barca", Compass.NORTH, new Position(3, 3));
        assertNotNull(s, "Error: buildShip('barca') should not return null.");
        assertInstanceOf(Barge.class, s, "Error: buildShip('barca') should return a Barge.");
    }

    @Test
    @DisplayName("buildShip2(): 'caravela' deve criar instância de Caravel")
    void buildShip2() {
        Ship s = Ship.buildShip("caravela", Compass.NORTH, new Position(3, 3));
        assertNotNull(s, "Error: buildShip('caravela') should not return null.");
        assertInstanceOf(Caravel.class, s, "Error: buildShip('caravela') should return a Caravel.");
    }

    @Test
    @DisplayName("buildShip3(): 'nau' deve criar instância de Carrack")
    void buildShip3() {
        Ship s = Ship.buildShip("nau", Compass.NORTH, new Position(3, 3));
        assertNotNull(s, "Error: buildShip('nau') should not return null.");
        assertInstanceOf(Carrack.class, s, "Error: buildShip('nau') should return a Carrack.");
    }

    @Test
    @DisplayName("buildShip4(): 'fragata' deve criar instância de Frigate")
    void buildShip4() {
        Ship s = Ship.buildShip("fragata", Compass.NORTH, new Position(3, 3));
        assertNotNull(s, "Error: buildShip('fragata') should not return null.");
        assertInstanceOf(Frigate.class, s, "Error: buildShip('fragata') should return a Frigate.");
    }

    @Test
    @DisplayName("buildShip5(): 'galeao' deve criar instância de Galleon")
    void buildShip5() {
        Ship s = Ship.buildShip("galeao", Compass.NORTH, new Position(3, 3));
        assertNotNull(s, "Error: buildShip('galeao') should not return null.");
        assertInstanceOf(Galleon.class, s, "Error: buildShip('galeao') should return a Galleon.");
    }

    @Test
    @DisplayName("buildShip6(): tipo desconhecido deve devolver null")
    void buildShip6() {
        Ship s = Ship.buildShip("unknown", Compass.NORTH, new Position(3, 3));
        assertNull(s, "Error: buildShip with unknown kind should return null.");
    }

    @Test
    @DisplayName("buildShip7(): shipKind null deve lançar AssertionError")
    void buildShip7() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip(null, Compass.NORTH, new Position(3, 3)),
                "Error: buildShip(null, ...) should throw AssertionError.");
    }

    @Test
    @DisplayName("buildShip8(): bearing null deve lançar AssertionError")
    void buildShip8() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip("barca", null, new Position(3, 3)),
                "Error: buildShip(..., null, ...) should throw AssertionError.");
    }

    @Test
    @DisplayName("buildShip9(): pos null deve lançar AssertionError")
    void buildShip9() {
        assertThrows(AssertionError.class,
                () -> Ship.buildShip("barca", Compass.NORTH, null),
                "Error: buildShip(..., null) should throw AssertionError.");
    }

}