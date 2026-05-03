package battleship;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Position.
 * Author: IGE-122989
 * Date: 2026-04-29 15:23
 *
 * Cyclomatic Complexity (CC):
 * - randomPosition(): 1
 * - constructor(char,int): 1
 * - constructor(int,int): 1
 * - getRow(): 1
 * - getColumn(): 1
 * - getClassicRow(): 1
 * - getClassicColumn(): 1
 * - isInside(): 3
 * - isAdjacentTo(): 3
 * - adjacentPositions(): 2
 * - isOccupied(): 1
 * - isHit(): 1
 * - occupy(): 1
 * - shoot(): 1
 * - equals(): 4
 * - hashCode(): 1
 * - toString(): 1
 */
 class PositionTest {
	private Position position;

	@BeforeEach
	void setUp() {
		position = new Position(2, 3);
	//	position = new Position('C', 4);
	}

	@AfterEach
	void tearDown() {
		position = null;
	}

	@Test
	@DisplayName("constructor(): deve criar posição (1,1) com isOccupied=false e isHit=false")
	void constructor() {
		Position pos = new Position(1, 1);
		assertAll(
				() -> assertNotNull(pos, "Error: object should not be null"),
				() -> assertEquals(1, pos.getRow(), "Error: row should be 1"),
				() -> assertEquals(1, pos.getColumn(), "Error: column should be 1"),
				() -> assertFalse(pos.isOccupied(), "Error: new position should not be occupied"),
				() -> assertFalse(pos.isHit(), "Error: new position should not be hit")
		);
	}

	@Test
	@DisplayName("constructorCharInt(): 'C',4 deve mapear para row=2, column=3")
	void constructorCharInt() {
		// 'C' → toUpperCase('C') - 'A' = 2   ;   4 - 1 = 3
		Position pos = new Position('C', 4);
		assertAll(
				() -> assertNotNull(pos, "Error: char-constructor object should not be null"),
				() -> assertEquals(2, pos.getRow(), "Error: 'C' should map to row 2"),
				() -> assertEquals(3, pos.getColumn(), "Error: column 4 should map to internal column 3"),
				() -> assertFalse(pos.isOccupied(), "Error: new position should not be occupied"),
				() -> assertFalse(pos.isHit(), "Error: new position should not be hit")
		);
	}

	@Test
	@DisplayName("constructorCharInt2(): letra minúscula 'a',1 deve ser normalizada para row=0, column=0")
	void constructorCharInt2() {
		// toUpperCase('a') = 'A' → row 0 ; 1-1 = 0
		Position pos = new Position('a', 1);
		assertAll(
				() -> assertEquals(0, pos.getRow(), "Error: 'a' (lowercase) should map to row 0 after toUpperCase"),
				() -> assertEquals(0, pos.getColumn(), "Error: column 1 should map to internal column 0")
		);
	}

	@Test
	void getRow() {
		assertEquals(2, position.getRow(), "Failed to get row: expected 2 but got " + position.getRow());
	}

	@Test
	void getColumn() {
		assertEquals(3, position.getColumn(), "Failed to get column: expected 3 but got " + position.getColumn());
	}

	@Test
	void getClassicRow() {
		assertEquals('C', position.getClassicRow(), "Failed to get row: expected 2 but got " + position.getRow());
	}

	@Test
	@DisplayName("getClassicColumn(): column 3 deve devolver 4 (1-indexed)")
	void getClassicColumn() {
		// column is stored as 3 (0-indexed), getClassicColumn() returns column+1 = 4
		assertEquals(4, position.getClassicColumn(),
				"Error: getClassicColumn() should return column+1 = 4, not the raw column");
	}

	@Test
	void isValid1() {
		position = new Position(0, 0);
		assertTrue(position.isInside(), "Position (0,0) should be valid");
	}

	@Test
	void isValid2() {
		position = new Position(-1, 5);
		assertFalse(position.isInside(), "Position with negative row should be invalid");
	}

	@Test
	void isValid3() {
		position = new Position(5, -1);
		assertFalse(position.isInside(), "Position with negative column should be invalid");
	}

	@Test
	void isValid4() {
		position = new Position(Game.BOARD_SIZE, 5);
		assertFalse(position.isInside(), "Position with row >= BOARD_SIZE should be invalid");
	}

	@Test
	void isValid5() {
		position = new Position(5, Game.BOARD_SIZE);
		assertFalse(position.isInside(), "Position with column >= BOARD_SIZE should be invalid");
	}

	@Test
	void isOccupied() {
		assertFalse(position.isOccupied(), "New position should not be occupied");
		position.occupy();
		assertTrue(position.isOccupied(), "Position should be occupied after occupy()");
	}

	@Test
	void isHit() {
		assertFalse(position.isHit(), "New position should not be hit");
		position.shoot();
		assertTrue(position.isHit(), "Position should be hit after shoot()");
	}

	@Test
	void equals1() {
		Position same = new Position(2, 3);
		assertTrue(position.equals(same), "Equal positions not identified as equal");
	}

	@Test
	void equals2() {
		assertFalse(position.equals(null), "Position should not equal null");
	}

	@Test
	void equals3() {
		Object other = new Object();
		assertFalse(position.equals(other), "Position should not equal non-Position object");
	}

	@Test
	void equals4() {
		Position other = new Position(2, 4);
		assertFalse(position.equals(other), "Positions with the same row but different column should not be equal");
	}

	@Test
	void equals5() {
		assertTrue(position.equals(position), "A position should be equal to itself");
	}

	@Test
	void hashCodeConsistency() {
		Position same = new Position(2, 3);
		assertEquals(position.hashCode(), same.hashCode(),
				"Hash codes not consistent for equal positions");
	}

	@Test
	void toStringFormat() {
//		String expected = "Row = C, Column = 4";
		String expected = "C4";
		assertEquals(expected, position.toString(),
				"Incorrect string representation: expected '" + expected +
						"' but got '" + position.toString() + "'");
	}

	@Test
	@DisplayName("randomPosition(): deve devolver posição não nula dentro do tabuleiro")
	void randomPosition() {
		Position p = Position.randomPosition();
		assertAll(
				() -> assertNotNull(p, "Error: randomPosition() must not return null"),
				() -> assertTrue(p.isInside(),
						"Error: randomPosition() must always generate a valid in-bounds position"),
				() -> assertTrue(p.getRow() >= 0 && p.getRow() < Game.BOARD_SIZE,
						"Error: random row must be in [0, BOARD_SIZE)"),
				() -> assertTrue(p.getColumn() >= 0 && p.getColumn() < Game.BOARD_SIZE,
						"Error: random column must be in [0, BOARD_SIZE)")
		);
	}

	@Test
	@DisplayName("hashCode1(): posições com mesmas coordenadas devem ter igual hashCode")
	void hashCode1() {
		assertEquals(new Position(2, 3).hashCode(), position.hashCode(),
				"Error: hash codes must be consistent for equal positions");
	}

	@Test
	@DisplayName("hashCode2(): posições diferentes devem ter hashCodes diferentes")
	void hashCode2() {
		assertNotEquals(new Position(1, 1).hashCode(), new Position(2, 2).hashCode(),
				"Error: different positions should have different hash codes.");
	}

	@Test
	@DisplayName("toString1(): (2,3) deve devolver 'C4'")
	void toString1() {
		assertEquals("C4", position.toString(),
				"Error: expected 'C4' for position (2,3) but got '" + position.toString() + "'");
	}

	@Test
	@DisplayName("toString2(): (0,0) deve devolver 'A1'")
	void toString2() {
		assertEquals("A1", new Position(0, 0).toString(),
				"Error: expected 'A1' for position (0,0)");
	}

	@Test
	@DisplayName("adjacentPositions1(): posição no canto deve ter 3 adjacentes")
	void adjacentPositions1() {
		Position corner = new Position(0, 0);
		List<IPosition> adj = corner.adjacentPositions();
		assertEquals(3, adj.size(), "Error: corner position should have exactly 3 adjacent positions.");
	}

	@Test
	@DisplayName("adjacentPositions2(): posição na borda deve ter 5 adjacentes")
	void adjacentPositions2() {
		Position edge = new Position(0, 5);
		List<IPosition> adj = edge.adjacentPositions();
		assertEquals(5, adj.size(), "Error: edge position should have exactly 5 adjacent positions.");
	}

	@Test
	@DisplayName("isAdjacentTo1(): posição horizontal adjacente deve retornar true")
	void isAdjacentTo1() {
		assertTrue(position.isAdjacentTo(new Position(2, 4)),
				"Error: horizontally adjacent position should be adjacent");
	}

	@Test
	@DisplayName("isAdjacentTo2(): posição distante deve retornar false")
	void isAdjacentTo2() {
		assertFalse(position.isAdjacentTo(new Position(4, 5)),
				"Error: non-adjacent position should not be adjacent (Δrow=2 fails first condition)");
	}

	@Test
	@DisplayName("isAdjacentTo3(): Δcol > 1 com Δrow == 0 deve retornar false")
	void isAdjacentTo3() {
		// Δrow = 0 (passes first condition), Δcol = 2 (fails second condition)
		assertFalse(position.isAdjacentTo(new Position(2, 5)),
				"Error: position with Δcol=2 should not be adjacent (second condition false)");
	}

	@Test
	@DisplayName("shoot(): deve marcar posição como atingida")
	void shoot() {
		position.shoot();
		assertTrue(position.isHit(),
				"Error: isHit() should be true after shoot()");
	}

	@Test
	@DisplayName("equals6(): mesma coluna, diferente row deve ser false")
	void equals6() {
        assertNotEquals(position, new Position(1, 3), "Error: positions with different row should not be equal");
	}

}