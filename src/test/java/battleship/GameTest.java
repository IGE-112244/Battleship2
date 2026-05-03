package battleship;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Game.
 * Author: britoeabreu
 * Date: 2024-03-19
 * Time: 15:30
 * Cyclomatic Complexity for each method:
 * - Game (constructor): 1
 * - fire: 7
 * - getShots: 1
 * - getRepeatedShots: 1
 * - getInvalidShots: 1
 * - getHits: 1
 * - getSunkShips: 1
 * - getRemainingShips: 1
 * - validShot: 3
 * - repeatedShot: 2
 * - printBoard: 1
 * - printValidShots: 1
 * - printFleet: 1
 */
class GameTest {

	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(new Fleet()); // Assuming Fleet is a concrete implementation of IFleet
	}

	@AfterEach
	void tearDown() {
		game = null;
	}

	@Test
	void constructor() {
		assertNotNull(game, "Game instance should not be null after construction.");
		assertNotNull(game.getAlienMoves(), "Shots list should not be null after initialization.");
		assertTrue(game.getAlienMoves().isEmpty(), "Shots list should be empty upon initialization.");
		assertEquals(0, game.getInvalidShots(), "Invalid shots count should be zero upon initialization.");
		assertEquals(0, game.getRepeatedShots(), "Repeated shots count should be zero upon initialization.");
		assertEquals(0, game.getHits(), "Hits count should be zero upon initialization.");
		assertEquals(0, game.getSunkShips(), "Sunk ships count should be zero upon initialization.");
	}

	@Test
	void fire2() {
		Position invalidPosition = new Position(-1, 5);
		game.fireSingleShot(invalidPosition, false);
		assertEquals(1, game.getInvalidShots(), "Invalid shots counter should increase for an invalid shot.");
	}

	@Test
	void fire3() {
		Position position = new Position(2, 3);
		game.fireSingleShot(position, false);
		game.fireSingleShot(position, true);
		assertEquals(1, game.getRepeatedShots(), "Repeated shots counter should increase for a repeated shot.");
	}

	@Test
	void repeatedShot1() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		Position position = new Position(2, 3);
		assertTrue(game.repeatedShot(position), "Position (2,3) should be marked as repeated after firing.");
	}

	@Test
	void repeatedShot2() {
		Position position = new Position(2, 3);
		assertFalse(game.repeatedShot(position), "Position (2,3) should not be marked as repeated before firing.");
	}

	@Test
	void getAlienMoves() {
		List<IPosition> positions = List.of(new Position(2, 3), new Position(2, 4), new Position(2, 5));
		game.fireShots(positions);
		assertEquals(1, game.getAlienMoves().size(), "Shots list should contain one shot after firing once.");
	}

	@Test
	void getRemainingShips() {
		IFleet fleet = game.getMyFleet();
		Ship ship1 = new Barge(Compass.NORTH, new Position(1, 1));
		Ship ship2 = new Frigate(Compass.EAST, new Position(5, 5));

		fleet.addShip(ship1);
		assertEquals(1, game.getRemainingShips(), "Just one ship was created!");
		fleet.addShip(ship2);
		assertEquals(2, game.getRemainingShips(), "Two ships were created!");
		ship2.sink();
		assertEquals(1, game.getRemainingShips(), "Remaining ships count should be 1 after sinking one of two ships.");
	}



	// fire1 - tiro válido que acerta num navio
	@Test
	@DisplayName("fireSingleShot() should register a hit when shot hits a ship")
	void fire1() {
		Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
		game.getMyFleet().addShip(ship);
		Position position = new Position(1, 1);
		game.fireSingleShot(position, false);
		assertEquals(1, game.getHits(), "Hits counter should increase when shot hits a ship.");
	}

	// fire4 - tiro válido que afunda um navio
	@Test
	@DisplayName("fireSingleShot() should register a sunk ship when all positions are hit")
	void fire4() {
		Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
		game.getMyFleet().addShip(ship);
		game.fireSingleShot(new Position(1, 1), false);
		game.fireSingleShot(new Position(2, 1), false);
		game.fireSingleShot(new Position(3, 1), false);
		assertEquals(1, game.getSunkShips(), "Sunk ships counter should increase when all positions are hit.");
	}

	// fire5 - tiro válido que não acerta em nada
	@Test
	@DisplayName("fireSingleShot() should not register hit when shot misses all ships")
	void fire5() {
		Position position = new Position(5, 5);
		game.fireSingleShot(position, false);
		assertEquals(0, game.getHits(), "Hits counter should not increase when shot misses.");
	}

	// fireShots - lista com número errado de tiros
	@Test
	@DisplayName("fireShots() should throw IllegalArgumentException when shot count is not NUMBER_SHOTS")
	void fireShotsInvalidCount() {
		List<IPosition> shots = List.of(new Position(1, 1));
		assertThrows(IllegalArgumentException.class, () -> game.fireShots(shots),
				"Error: Should throw IllegalArgumentException for wrong number of shots.");
	}

	// validShot - tiro dentro dos limites
	@Test
	@DisplayName("fireSingleShot() should be valid when position is inside bounds")
	void validShot1() {
		Position position = new Position(0, 0);
		game.fireSingleShot(position, false);
		assertEquals(0, game.getInvalidShots(), "Error: Valid shot should not increase invalid shots counter.");
	}

	// printBoard - sem tiros, sem legenda
	@Test
	@DisplayName("printBoard() should not throw when called with empty fleet and no shots")
	void printBoard1() {
		assertDoesNotThrow(() -> Game.printBoard(new Fleet(), new ArrayList<>(), false, false),
				"Error: printBoard should not throw any exceptions.");
	}

	// printBoard - com tiros e com legenda
	@Test
	@DisplayName("printBoard() should not throw when called with shots and legend")
	void printBoard2() {
		assertDoesNotThrow(() -> Game.printBoard(new Fleet(), new ArrayList<>(), true, true),
				"Error: printBoard should not throw any exceptions with shots and legend.");
	}

	// jsonShots - lista válida
	@Test
	@DisplayName("jsonShots() should return a valid JSON string for a list of positions")
	void jsonShots1() {
		List<IPosition> shots = List.of(new Position(0, 0), new Position(1, 1));
		String json = Game.jsonShots(shots);
		assertNotNull(json, "Error: jsonShots should return a non-null JSON string.");
		assertTrue(json.contains("row"), "Error: JSON should contain 'row' key.");
		assertTrue(json.contains("column"), "Error: JSON should contain 'column' key.");
	}

	// over - não deve lançar exceção
	@Test
	@DisplayName("over() should not throw any exceptions")
	void over() {
		assertDoesNotThrow(() -> game.over(),
				"Error: over() should not throw any exceptions.");
	}

	// printMyBoard e printAlienBoard
	@Test
	@DisplayName("printMyBoard() should not throw any exceptions")
	void printMyBoard() {
		assertDoesNotThrow(() -> game.printMyBoard(false, false),
				"Error: printMyBoard should not throw any exceptions.");
	}

	@Test
	@DisplayName("printAlienBoard() should not throw any exceptions")
	void printAlienBoard() {
		assertDoesNotThrow(() -> game.printAlienBoard(false, false),
				"Error: printAlienBoard should not throw any exceptions.");
	}

	@Test
	@DisplayName("readEnemyFire() should process valid input correctly")
	void readEnemyFire1() {
		Scanner scanner = new Scanner("A1 B2 C3");
		assertDoesNotThrow(() -> game.readEnemyFire(scanner),
				"Error: readEnemyFire should not throw for valid input.");
	}

	@Test
	@DisplayName("readEnemyFire() should throw when insufficient positions provided")
	void readEnemyFire2() {
		Scanner scanner = new Scanner("A1 B2");
		assertThrows(IllegalArgumentException.class, () -> game.readEnemyFire(scanner),
				"Error: Should throw IllegalArgumentException for insufficient positions.");
	}

	@Test
	@DisplayName("readEnemyFire() should process input with separate column and row tokens")
	void readEnemyFire3() {
		Scanner scanner = new Scanner("A 1 B 2 C 3");
		assertDoesNotThrow(() -> game.readEnemyFire(scanner),
				"Error: readEnemyFire should handle separate column and row tokens.");
	}
	@Test
	@DisplayName("readEnemyFire() should throw when Scanner is null")
	void readEnemyFire5() {
		assertThrows(IllegalArgumentException.class, () -> game.readEnemyFire(null),
				"Error: Should throw IllegalArgumentException when Scanner is null.");
	}

	@Test
	@DisplayName("printBoard() should not throw with a fleet containing ships")
	void printBoard3() {
		Fleet fleet = new Fleet();
		fleet.addShip(new Barge(Compass.NORTH, new Position(1, 1)));
		assertDoesNotThrow(() -> Game.printBoard(fleet, new ArrayList<>(), true, false),
				"Error: printBoard should not throw with ships in fleet.");
	}

	@Test
	@DisplayName("randomEnemyFire() should return a valid JSON string")
	void randomEnemyFire() {
		assertDoesNotThrow(() -> game.randomEnemyFire(),
				"Error: randomEnemyFire should not throw any exceptions.");
	}

	@Test
	@DisplayName("readEnemyFire() should throw when column is not followed by a number")
	void readEnemyFire4() {
		Scanner scanner = new Scanner("A B C");
		assertThrows(IllegalArgumentException.class, () -> game.readEnemyFire(scanner),
				"Error: Should throw when column is not followed by a number.");
	}

	@Test
	@DisplayName("printBoard() should show adjacent positions when ship is sunk")
	void printBoard4() {
		Fleet fleet = new Fleet();
		Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		ship.sink();
		assertDoesNotThrow(() -> Game.printBoard(fleet, new ArrayList<>(), false, false),
				"Error: printBoard should not throw when ship is sunk.");
	}

	@Test
	@DisplayName("printBoard() should mark hit ship positions correctly")
	void printBoard5() {
		Fleet fleet = new Fleet();
		Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		List<IPosition> shotPositions = List.of(
				new Position(1, 1), new Position(1, 2), new Position(5, 5)
		);
		Move move = new Move(1, shotPositions, List.of(
				new IGame.ShotResult(true, false, ship, false),
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(true, false, null, false)
		));
		assertDoesNotThrow(() -> Game.printBoard(fleet, List.of(move), true, false),
				"Error: printBoard should not throw when marking hit positions.");
	}

	@Test
	@DisplayName("readEnemyFireFromJson() should process valid JSON input")
	void readEnemyFireFromJson1() {
		String json = "[{\"row\":\"A\",\"column\":1},{\"row\":\"B\",\"column\":2},{\"row\":\"C\",\"column\":3}]\n\n";
		Scanner scanner = new Scanner(json);
		assertDoesNotThrow(() -> game.readEnemyFireFromJson(scanner),
				"Error: readEnemyFireFromJson should not throw for valid JSON.");
	}

	@Test
	@DisplayName("readEnemyFireFromJson() should throw for invalid JSON count")
	void readEnemyFireFromJson2() {
		String json = "[{\"row\":\"A\",\"column\":1}]\n\n";
		Scanner scanner = new Scanner(json);
		assertThrows(IllegalArgumentException.class, () -> game.readEnemyFireFromJson(scanner),
				"Error: Should throw for wrong number of shots in JSON.");
	}

	@Test
	@DisplayName("getAlienFleet() should return a non-null fleet")
	void getAlienFleet() {
		assertNotNull(game.getAlienFleet(),
				"Error: getAlienFleet should not return null.");
	}

	@Test
	@DisplayName("getMyMoves() should return an empty list initially")
	void getMyMoves() {
		assertNotNull(game.getMyMoves(),
				"Error: getMyMoves should not return null.");
		assertTrue(game.getMyMoves().isEmpty(),
				"Error: getMyMoves should be empty initially.");
	}

	@Test
	@DisplayName("printBoard() should mark SHIP_ADJACENT_MARKER position as SHOT_WATER_MARKER")
	void printBoard6() {
		Fleet fleet = new Fleet();
		Ship ship = new Barge(Compass.NORTH, new Position(1, 1));
		fleet.addShip(ship);
		ship.sink();
		// tiro numa posição adjacente ao navio afundado
		List<IPosition> shotPositions = List.of(
				new Position(1, 0), new Position(0, 1), new Position(4, 1)
		);
		Move move = new Move(1, shotPositions, List.of(
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(true, false, null, false),
				new IGame.ShotResult(true, false, null, false)
		));
		assertDoesNotThrow(() -> Game.printBoard(fleet, List.of(move), true, false),
				"Error: printBoard should handle SHIP_ADJACENT_MARKER positions.");
	}


	@Test
	@DisplayName("readEnemyFireFromJson() should throw RuntimeException for malformed JSON")
	void readEnemyFireFromJson3() {
		Scanner scanner = new Scanner("isto não é json\n\n");
		assertThrows(RuntimeException.class, () -> game.readEnemyFireFromJson(scanner),
				"Should throw RuntimeException for malformed JSON.");
	}

	@Test
	@DisplayName("printBoard() should ignore shots outside the board")
	void printBoard7() {
		Fleet fleet = new Fleet();
		Move move = new Move(1,
				List.of(new Position(-1, -1)),
				List.of(new IGame.ShotResult(false, false, null, false))
		);

		assertDoesNotThrow(() -> Game.printBoard(fleet, List.of(move), true, false),
				"printBoard should ignore shots outside the board.");
	}



}