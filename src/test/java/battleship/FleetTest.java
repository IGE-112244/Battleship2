package battleship;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for Fleet.
 * Author: ${user.name}
 * Date: ${current_date}
 * Time: ${current_time}
 * Cyclomatic Complexity for each method:
 * - Constructor: 1
 * - addShip: 3
 * - getShips: 1
 * - getShipsLike: 2
 * - getFloatingShips: 2
 * - createRandom(): 2
 * - printShips(): 2
 * - printShipsByCategory(): 1
 * - printFloatingShips(): 1
 * - printAllShips(): 1
 * - getShipsLike() path 2: category not found
 * - getFloatingShips() path 2: no floating ships
 * - getSunkShips(): 2
 * - shipAt() path 2: no ship at position
 * - isInsideBoard: 3
 * - colisionRisk: 2
 */
	public class FleetTest {

		private Fleet fleet;

		@BeforeEach
		void setUp() {
			fleet = new Fleet();
		}

		@AfterEach
		void tearDown() {
			fleet = null;
		}

		/**
		 * Test for the Fleet constructor.
		 * Cyclomatic Complexity: 1
		 */
		@Test
		void testConstructor() {
			assertNotNull(fleet, "Error: Instance of Fleet should not be null.");
			assertTrue(fleet.getShips().isEmpty(), "Error: Fleet should be initialized with empty ships list.");
		}

		/**
		 * Test for the addShip method (all conditions true).
		 * Cyclomatic Complexity: 3
		 */
		@Test
		void testAddShip1() {
			IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
			assertTrue(fleet.addShip(ship), "Error: Valid ship should be added successfully.");
			assertEquals(1, fleet.getShips().size(), "Error: Fleet should contain one ship after addition.");
		}

		/**
		 * Test for the addShip method (fleet size limit reached).
		 */
		@Test
		void testAddShip2() {
			for (int i = 0; i < Fleet.FLEET_SIZE; i++) {
				fleet.addShip(new Barge(Compass.NORTH, new Position(i, 0)));
			}
			IShip anotherShip = new Barge(Compass.NORTH, new Position(10, 10));
			assertFalse(fleet.addShip(anotherShip), "Error: Should not add ship when fleet size limit is reached.");
		}

		/**
		 * Test for the addShip method (ship outside the board).
		 */
		@Test
		void testAddShip3() {
			IShip shipOutside = new Barge(Compass.NORTH, new Position(99, 99));
			assertFalse(fleet.addShip(shipOutside), "Error: Should not add ship outside the board.");
		}

		/**
		 * Test for the addShip method (collision risk).
		 */
		@Test
		void testAddShip4() {
			IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
			IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));  // Overlapping position
			fleet.addShip(ship1);
			assertFalse(fleet.addShip(ship2), "Error: Should not add ship with a collision risk.");
		}

		/**
		 * Test for the getShips method.
		 * Cyclomatic Complexity: 1
		 */
		@Test
		void testGetShips() {
			assertTrue(fleet.getShips().isEmpty(), "Error: Fleet's ships list should initially be empty.");
			IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
			fleet.addShip(ship);
			assertEquals(1, fleet.getShips().size(), "Error: Fleet should have size 1 after adding a ship.");
			assertEquals(ship, fleet.getShips().get(0), "Error: Fleet's first ship should match the added ship.");
		}

		/**
		 * Test for the getShipsLike method (ships of specific category).
		 * Cyclomatic Complexity: 2
		 */
		@Test
		void testGetShipsLike() {
			IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
			IShip ship2 = new Caravel(Compass.NORTH, new Position(2, 1));
			fleet.addShip(ship1);
			fleet.addShip(ship2);

			List<IShip> barges = fleet.getShipsLike("Barca");
			assertEquals(1, barges.size(), "Error: There should be exactly one ship of category 'Barca'.");
			assertEquals(ship1, barges.get(0), "Error: The ship of category 'Barca' does not match.");
		}

		/**
		 * Test for the getFloatingShips method.
		 * Cyclomatic Complexity: 2
		 */
		@Test
		void testGetFloatingShips() {
			IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
			IShip ship2 = new Caravel(Compass.NORTH, new Position(4, 4));
			fleet.addShip(ship1);
			fleet.addShip(ship2);

			List<IShip> floatingShips = fleet.getFloatingShips();
			assertEquals(2, floatingShips.size(), "Error: All ships should be floating initially.");

			ship1.getPositions().get(0).shoot();  // Sink ship1
			floatingShips = fleet.getFloatingShips();
			assertEquals(1, floatingShips.size(), "Error: Only one ship should be floating after sinking one.");
			assertEquals(ship2, floatingShips.get(0), "Error: The floating ship should match the expected result.");
		}

		/**
		 * Test for the shipAt method.
		 * Cyclomatic Complexity: 2
		 */
		@Test
		void testShipAt() {
			IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
			fleet.addShip(ship);

			assertEquals(ship, fleet.shipAt(new Position(1, 1)), "Error: Should return the correct ship at the position.");
			assertNull(fleet.shipAt(new Position(5, 5)), "Error: Should return null for empty positions in the fleet.");
		}

		/**
		 * Test for private method isInsideBoard.
		 * Cyclomatic Complexity: 3
		 */
		@Test
		void testIsInsideBoard() throws Exception {
			// Use reflection to access private methods
			var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
			method.setAccessible(true);

			IShip insideShip = new Barge(Compass.NORTH, new Position(1, 1));
			IShip outsideShip = new Barge(Compass.NORTH, new Position(99, 99));

			assertTrue((Boolean) method.invoke(fleet, insideShip), "Error: Ship inside the board should return true.");
			assertFalse((Boolean) method.invoke(fleet, outsideShip), "Error: Ship outside the board should return false.");
		}

		/**
		 * Test for private method colisionRisk.
		 * Cyclomatic Complexity: 2
		 */
		@Test
		void testColisionRisk() throws Exception {
			var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
			method.setAccessible(true);

			IShip ship1 = new Barge(Compass.NORTH, new Position(1, 1));
			IShip ship2 = new Barge(Compass.NORTH, new Position(1, 1));  // Overlapping position
			fleet.addShip(ship1);

			assertTrue((Boolean) method.invoke(fleet, ship2), "Error: Overlapping ships should be at collision risk.");
			assertFalse((Boolean) method.invoke(fleet, new Barge(Compass.NORTH, new Position(5, 5))),
					"Error: Ships at non-overlapping positions should not have a collision risk.");
		}

		/**
		 * Test for the printStatus method.
		 * Cyclomatic Complexity: 1
		 */
		@Test
		void testPrintStatus() {
			IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
			fleet.addShip(ship);
			assertDoesNotThrow(fleet::printStatus, "Error: printStatus should not throw any exceptions.");
		}
	// -----------------------------------------------------------------------
// createRandom()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("createRandom() should return a non-null fleet")
	void testCreateRandom1() {
		IFleet randomFleet = Fleet.createRandom();
		assertNotNull(randomFleet, "Error: createRandom() should return a non-null fleet.");
	}

	@Test
	@DisplayName("createRandom() should return a fleet with the correct number of ships")
	void testCreateRandom2() {
		IFleet randomFleet = Fleet.createRandom();
		assertEquals(Fleet.FLEET_SIZE, randomFleet.getShips().size(),
				"Error: createRandom() should return a fleet with " + Fleet.FLEET_SIZE + " ships.");
	}

// -----------------------------------------------------------------------
// printShips()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printShips() should not throw with non-empty list")
	void testPrintShips1() {
		List<IShip> ships = new ArrayList<>();
		ships.add(new Barge(Compass.NORTH, new Position(1, 1)));
		assertDoesNotThrow(() -> fleet.printShips(ships),
				"Error: printShips() should not throw with a non-empty list.");
	}

	@Test
	@DisplayName("printShips() should not throw with empty list")
	void testPrintShips2() {
		assertDoesNotThrow(() -> fleet.printShips(new ArrayList<>()),
				"Error: printShips() should not throw with an empty list.");
	}

// -----------------------------------------------------------------------
// printShipsByCategory()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printShipsByCategory() should not throw for existing category")
	void testPrintShipsByCategory() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertDoesNotThrow(() -> fleet.printShipsByCategory("Barca"),
				"Error: printShipsByCategory() should not throw for an existing category.");
	}

// -----------------------------------------------------------------------
// printFloatingShips()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printFloatingShips() should not throw")
	void testPrintFloatingShips() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertDoesNotThrow(() -> fleet.printFloatingShips(),
				"Error: printFloatingShips() should not throw.");
	}

// -----------------------------------------------------------------------
// printAllShips()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printAllShips() should not throw")
	void testPrintAllShips() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertDoesNotThrow(() -> fleet.printAllShips(),
				"Error: printAllShips() should not throw.");
	}

// -----------------------------------------------------------------------
// getShipsLike() - path sem resultados
// -----------------------------------------------------------------------

	@Test
	@DisplayName("getShipsLike() should return empty list for non-existing category")
	void testGetShipsLike2() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		List<IShip> result = fleet.getShipsLike("Galeao");
		assertTrue(result.isEmpty(),
				"Error: getShipsLike() should return empty list for non-existing category.");
	}

// -----------------------------------------------------------------------
// getSunkShips()
// -----------------------------------------------------------------------

	@Test
	@DisplayName("getSunkShips() should return empty list when no ships are sunk")
	void testGetSunkShips1() {
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertTrue(fleet.getSunkShips().isEmpty(),
				"Error: getSunkShips() should return empty list when no ships are sunk.");
	}

	@Test
	@DisplayName("getSunkShips() should return sunk ships")
	void testGetSunkShips2() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		ship.shoot(new Position(1, 1)); // afundar a barca
		assertEquals(1, fleet.getSunkShips().size(),
				"Error: getSunkShips() should return 1 sunk ship.");
	}

// -----------------------------------------------------------------------
// getFloatingShips() - path sem navios a flutuar
// -----------------------------------------------------------------------

	@Test
	@DisplayName("getFloatingShips() should return empty list when all ships are sunk")
	void testGetFloatingShips2() {
		IShip ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		ship.shoot(new Position(1, 1)); // afundar
		assertTrue(fleet.getFloatingShips().isEmpty(),
				"Error: getFloatingShips() should return empty when all ships are sunk.");
	}

// -----------------------------------------------------------------------
// shipAt() - path sem navio na posição
// -----------------------------------------------------------------------

	@Test
	@DisplayName("shipAt() should return null for empty fleet")
	void testShipAtEmpty() {
		assertNull(fleet.shipAt(new Position(1, 1)),
				"Error: shipAt() should return null for an empty fleet.");
	}

	// -----------------------------------------------------------------------
// addShip() - assert s != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("addShip() should throw AssertionError when ship is null")
	void testAddShipNull() {
		assertThrows(AssertionError.class, () -> fleet.addShip(null),
				"Error: addShip() should throw AssertionError when ship is null.");
	}

	// -----------------------------------------------------------------------
// getShipsLike() - assert category != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("getShipsLike() should throw AssertionError when category is null")
	void testGetShipsLikeNull() {
		assertThrows(AssertionError.class, () -> fleet.getShipsLike(null),
				"Error: getShipsLike() should throw AssertionError when category is null.");
	}

// -----------------------------------------------------------------------
// shipAt() - assert pos != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("shipAt() should throw AssertionError when position is null")
	void testShipAtNull() {
		assertThrows(AssertionError.class, () -> fleet.shipAt(null),
				"Error: shipAt() should throw AssertionError when position is null.");
	}

// -----------------------------------------------------------------------
// printShips() - assert ships != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printShips() should throw AssertionError when list is null")
	void testPrintShipsNull() {
		assertThrows(AssertionError.class, () -> fleet.printShips(null),
				"Error: printShips() should throw AssertionError when ships list is null.");
	}

// -----------------------------------------------------------------------
// printShipsByCategory() - assert category != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("printShipsByCategory() should throw AssertionError when category is null")
	void testPrintShipsByCategoryNull() {
		assertThrows(AssertionError.class, () -> fleet.printShipsByCategory(null),
				"Error: printShipsByCategory() should throw AssertionError when category is null.");
	}

	// -----------------------------------------------------------------------
// isInsideBoard() via addShip() - cada condição falsa individualmente
// -----------------------------------------------------------------------

	@Test
	@DisplayName("addShip() should fail when ship's leftmost position is negative")
	void testAddShipLeftOutside() throws Exception {
		var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
		method.setAccessible(true);
		IShip ship = new Barge(Compass.NORTH, new Position(0, -1));
		assertFalse((Boolean) method.invoke(fleet, ship),
				"Error: Ship with negative column should be outside board.");
	}

	@Test
	@DisplayName("addShip() should fail when ship's rightmost position exceeds board")
	void testAddShipRightOutside() throws Exception {
		var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
		method.setAccessible(true);
		IShip ship = new Barge(Compass.NORTH, new Position(0, Game.BOARD_SIZE));
		assertFalse((Boolean) method.invoke(fleet, ship),
				"Error: Ship with column exceeding board size should be outside board.");
	}

	@Test
	@DisplayName("addShip() should fail when ship's topmost position is negative")
	void testAddShipTopOutside() throws Exception {
		var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
		method.setAccessible(true);
		IShip ship = new Barge(Compass.NORTH, new Position(-1, 0));
		assertFalse((Boolean) method.invoke(fleet, ship),
				"Error: Ship with negative row should be outside board.");
	}

	@Test
	@DisplayName("addShip() should fail when ship's bottommost position exceeds board")
	void testAddShipBottomOutside() throws Exception {
		var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
		method.setAccessible(true);
		IShip ship = new Barge(Compass.NORTH, new Position(Game.BOARD_SIZE, 0));
		assertFalse((Boolean) method.invoke(fleet, ship),
				"Error: Ship with row exceeding board size should be outside board.");
	}

	@Test
	@DisplayName("createRandom() should always return a valid fleet with correct size")
	void testCreateRandomFleetSize() {
		for (int i = 0; i < 5; i++) { // correr várias vezes para maior confiança
			IFleet f = Fleet.createRandom();
			assertEquals(Fleet.FLEET_SIZE, f.getShips().size(),
					"Error: createRandom() should always produce a fleet of size " + Fleet.FLEET_SIZE);
		}
	}

	// -----------------------------------------------------------------------
// isInsideBoard() - assert s != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("isInsideBoard() should throw AssertionError when ship is null")
	void testIsInsideBoardNull() throws Exception {
		var method = Fleet.class.getDeclaredMethod("isInsideBoard", IShip.class);
		method.setAccessible(true);
		assertThrows(java.lang.reflect.InvocationTargetException.class,
				() -> method.invoke(fleet, (Object) null),
				"Error: isInsideBoard() should throw AssertionError when ship is null.");
	}

// -----------------------------------------------------------------------
// colisionRisk() - assert s != null
// -----------------------------------------------------------------------

	@Test
	@DisplayName("colisionRisk() should throw AssertionError when ship is null")
	void testColisionRiskNull() throws Exception {
		var method = Fleet.class.getDeclaredMethod("colisionRisk", IShip.class);
		method.setAccessible(true);
		assertThrows(java.lang.reflect.InvocationTargetException.class,
				() -> method.invoke(fleet, (Object) null),
				"Error: colisionRisk() should throw AssertionError when ship is null.");
	}

	@Test
	@DisplayName("addShip() should return false when fleet exceeds FLEET_SIZE")
	void testAddShipFleetFull() {
		Fleet fullFleet = (Fleet) Fleet.createRandom(); // 11 navios

		// Adicionar mais um navio manualmente para atingir o limite real (12)
		// Procurar uma posição livre sem colisão
		IShip extra = null;
		for (int r = 0; r < Game.BOARD_SIZE && extra == null; r++) {
			for (int c = 0; c < Game.BOARD_SIZE && extra == null; c++) {
				IShip candidate = new Barge(Compass.NORTH, new Position(r, c));
				if (fullFleet.addShip(candidate)) {
					extra = candidate; // foi adicionado — frota tem agora 12
				}
			}
		}

		// Agora a frota tem 12 navios — o próximo deve ser recusado
		assertEquals(Fleet.FLEET_SIZE + 1, fullFleet.getShips().size(),
				"Error: Fleet should have FLEET_SIZE + 1 ships before testing limit.");

		// Tentar adicionar o 13º navio — deve ser recusado pelo tamanho
		IShip oneMore = new Barge(Compass.NORTH, new Position(9, 9));
		assertFalse(fullFleet.addShip(oneMore),
				"Error: addShip() should return false when fleet exceeds FLEET_SIZE.");
	}

	@Test
	@DisplayName("createRandom() branch ship null is not reachable in normal execution")
	void testCreateRandomBranchNotReachable() {
		// O branch 'ship != null' quando false não é alcançável em execução normal
		// pois createRandom() usa apenas tipos de navio válidos.
		// Este teste confirma que createRandom() sempre produz uma frota válida.
		IFleet f = Fleet.createRandom();
		assertNotNull(f, "Error: createRandom() should never return null.");
		assertEquals(Fleet.FLEET_SIZE, f.getShips().size(),
				"Error: createRandom() should always produce a fleet of size " + Fleet.FLEET_SIZE);
	}

	}