package battleship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

/**
 * The type Tasks.
 */
public class Tasks {
	/**
	 * The constant LOGGER.
	 */
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * The constant GOODBYE_MESSAGE.
	 */
	private static final String GOODBYE_MESSAGE = "Bons ventos!";

	/**
	 * Strings to be used by the user
	 */
	private static final String AJUDA = "ajuda";
	private static final String GERAFROTA = "gerafrota";
	private static final String LEFROTA = "lefrota";
	private static final String DESISTIR = "desisto";
	private static final String RAJADA = "rajada";
	private static final String TIROS = "tiros";
	private static final String MAPA = "mapa";
	private static final String STATUS = "estado";
	private static final String SIMULA = "simula";
	private static final String RAJADAJSON = "rajadajson";
	private static final String EXPORTJSON = "exportjson";
	private static final String IAJOGO   = "iajogo";
	private static final String IAJOGO2P = "iajogo2p";
	private static final String STATS = "stats";
	private static final String RESETSTATS = "resetstats";

	/**
	 * This task also tests the fighting element of a round of three shots
	 */
	public static void menu() {

		IFleet myFleet = null;
		IGame game = null;
		menuHelp();

		System.out.print("> ");
		Scanner in = new Scanner(System.in);
		String command = in.next();
		while (!command.equals(DESISTIR)) {

			switch (command) {
				case GERAFROTA:
					myFleet = Fleet.createRandom();
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					BoardVisualizer.iniciar();                                           // abre a janela
					BoardVisualizer.atualizar(myFleet, game.getAlienMoves(), false);    // mostra tabuleiro inicial
					break;
				case LEFROTA:
					myFleet = buildFleet(in);
					game = new Game(myFleet);
					game.printMyBoard(false, true);
					break;
				case STATUS:
					if (myFleet != null)
						myFleet.printStatus();
					break;
				case MAPA:
					if (myFleet != null) {
						game.printMyBoard(false, true);
						BoardVisualizer.atualizar(myFleet, game.getAlienMoves(), false); // atualiza
					}
					break;
				case RAJADA:
					if (game != null) {
						game.readEnemyFire(in);
						myFleet.printStatus();
						game.printMyBoard(true, false);
						BoardVisualizer.atualizar(myFleet, game.getAlienMoves(), true);  // atualiza com tiros

						if (game.getRemainingShips() == 0) {
							game.over();
                            BoardVisualizer.fechar();                                  // fecha a janela
							String pdfFile = "historico_partida_" + System.currentTimeMillis() + ".pdf";
							GamePdfExporter.export(game, pdfFile);
							System.out.println("Histórico exportado para: " + pdfFile);
							String jsonFile = "historico_partida_" + System.currentTimeMillis() + ".json";
							GameJsonExporter.export(game, jsonFile);
							System.out.println("Histórico exportado para: " + jsonFile);

							GameStats stats = GameStatsRepository.load();
							stats.update(game, true); // true = jogador ganhou
							GameStatsRepository.save(stats);
							GameStatsPanel.mostrar();

							System.exit(0);
						}
					}
					break;
				case SIMULA:

					if (game != null) {
						ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

						IFleet finalMyFleet = myFleet;
						IGame finalGame = game;
						Runnable simulationStep = new Runnable() {
							@Override
							public void run() {
								if (finalGame.getRemainingShips() > 0) {
									// Jogo em curso — disparar rajada aleatória
									finalGame.randomEnemyFire();
									finalMyFleet.printStatus();
									finalGame.printMyBoard(true, false);
									BoardVisualizer.atualizar(finalMyFleet, finalGame.getAlienMoves(), true);
								} else {
									// Jogo terminado — exportar e terminar
									executor.shutdown();
									finalGame.over();
									BoardVisualizer.fechar();
									exportAndSaveStats(finalGame);
									System.exit(0);
								}
							}
						};

						// Executa de 3 em 3 segundos sem busy-waiting
						executor.scheduleAtFixedRate(simulationStep, 0, 3, TimeUnit.SECONDS);
					}
					break;
				case TIROS:
					if (game != null)
						game.printMyBoard(true, true);
					break;
                case AJUDA:
                    menuHelp();
                    break;
				case EXPORTJSON:
					if (game != null) {
						String jsonFile = "historico_partida_" + System.currentTimeMillis() + ".json";
						GameJsonExporter.export(game, jsonFile);
						System.out.println("Histórico exportado para: " + jsonFile);
					} else {
						System.out.println("Ainda não existe jogo iniciado!");
					}
					break;
				case RAJADAJSON:
					if (game != null) {
						((Game) game).readEnemyFireFromJson(in);
						myFleet.printStatus();
						game.printMyBoard(true, false);
						BoardVisualizer.atualizar(myFleet, game.getAlienMoves(), true);
						if (game.getRemainingShips() == 0) {
							game.over();
							BoardVisualizer.fechar();
							String pdfFile = "historico_partida_" + System.currentTimeMillis() + ".pdf";
							GamePdfExporter.export(game, pdfFile);
							String jsonFile = "historico_partida_" + System.currentTimeMillis() + ".json";
							GameJsonExporter.export(game, jsonFile);
							GameStats stats = GameStatsRepository.load();
							stats.update(game, true);
							GameStatsRepository.save(stats);
							GameStatsPanel.mostrar();
							System.exit(0);
						}
					}
					break;
				case IAJOGO2P:
					if (game != null) {
						System.out.println("A iniciar jogo bidirecional contra o LLM...");
						System.out.println("Tu atacas a frota da IA, a IA ataca a tua frota!");
						System.out.println("Usa o comando 'rajada' seguido de 3 coordenadas para atacar.");

						// Carregar system prompt
						try {
							HuggingFaceClient.setSystemPrompt(
									new String(java.nio.file.Files.readAllBytes(
											java.nio.file.Paths.get("battleship_system_prompt.txt")))
							);
						} catch (IOException e) {
							System.out.println("Erro ao carregar o system prompt: " + e.getMessage());
							break;
						}

						// Inicializar a IA
						System.out.println("A IA está a criar a sua frota...");
						HuggingFaceClient.initGame();

						// ✅ Frota da IA gerida em Java (honesta e consistente)
						IFleet aiFleet = Fleet.createRandom();
						System.out.println("✅ Frota da IA gerada. Jogo pronto!");

						// Contadores
						int[] aiShipsRemaining = {aiFleet.getFloatingShips().size()};
						List<IMove> aiReceivedMoves = new ArrayList<>();
						int[] aiMoveNumber = {1};
						String lastAiResult = null;

						while (game.getRemainingShips() > 0 && aiShipsRemaining[0] > 0) {

							// -------------------------------------------------------
							// FASE 1: IA ataca o teu tabuleiro
							// -------------------------------------------------------
							System.out.println("\n⚔️  A IA está a atacar...");
							List<IPosition> aiShots = HuggingFaceClient.getNextShots(lastAiResult);
							game.fireShots(aiShots);
							myFleet.printStatus();
							game.printMyBoard(true, false);
							BoardVisualizer.atualizar(myFleet, game.getAlienMoves(), game.getMyMoves(), true);
							lastAiResult = game.getAlienMoves()
									.get(game.getAlienMoves().size() - 1)
									.processEnemyFire(true);

// Guardar resultados dos tiros da IA no diário
							List<IPosition> lastAiShots = game.getAlienMoves()
									.get(game.getAlienMoves().size() - 1).getShots();
							List<IGame.ShotResult> lastAiShotResults = game.getAlienMoves()
									.get(game.getAlienMoves().size() - 1).getShotResults();

							for (int i = 0; i < lastAiShots.size(); i++) {
								IPosition pos = lastAiShots.get(i);
								String posKey = String.valueOf(pos.getClassicRow()) + pos.getClassicColumn();
								if (i < lastAiShotResults.size()) {
									IGame.ShotResult r = lastAiShotResults.get(i);
									String resultado;
									if (!r.valid())       resultado = "fora do tabuleiro";
									else if (r.repeated()) resultado = "repetido";
									else if (r.ship() != null) {
										resultado = r.sunk()
												? "🔥 " + r.ship().getCategory() + " AFUNDADO"
												: "💥 acerto em " + r.ship().getCategory();
									} else                resultado = "água";
									HuggingFaceClient.addShotResult(posKey, resultado);
								}
							}

							if (game.getRemainingShips() == 0) break;

							// -------------------------------------------------------
							// FASE 2: Tu atacas a frota da IA
							// -------------------------------------------------------
							System.out.println("\n🎯 A tua vez! Escreve 'rajada' seguido de 3 coordenadas:");
							System.out.print("> ");
							String playerCommand = in.next();

							if (playerCommand.equals(RAJADA)) {
								List<IPosition> myShots = readPlayerShots(in);

								// Processar tiros em Java (honesto e consistente)
								List<IGame.ShotResult> myResults = new ArrayList<>();
								List<IPosition> alreadyShot = new ArrayList<>();

								for (IPosition pos : myShots) {
									// Verificar se já foi disparado anteriormente
									boolean repeated = alreadyShot.contains(pos)
											|| aiReceivedMoves.stream()
											.flatMap(m -> m.getShots().stream())
											.anyMatch(p -> p.equals(pos));

									if (!pos.isInside()) {
										myResults.add(new IGame.ShotResult(false, false, null, false));
									} else if (repeated) {
										myResults.add(new IGame.ShotResult(true, true, null, false));
									} else {
										IShip ship = aiFleet.shipAt(pos);
										if (ship != null) {
											ship.shoot(pos);
											myResults.add(new IGame.ShotResult(
													true, false, ship, !ship.stillFloating()));
										} else {
											myResults.add(new IGame.ShotResult(true, false, null, false));
										}
									}
									alreadyShot.add(pos);
								}

								// Registar o move
								Move myMove = new Move(aiMoveNumber[0]++, myShots, myResults);
								aiReceivedMoves.add(myMove);
								game.getMyMoves().add(myMove);

								// Mostrar resultado ao jogador
								StringBuilder resultMsg = new StringBuilder("\n📊 Resultado dos teus tiros:\n");
								for (IGame.ShotResult r : myResults) {
									if (!r.valid()) {
										resultMsg.append("❌ Tiro fora do tabuleiro!\n");
									} else if (r.repeated()) {
										resultMsg.append("🔄 Tiro repetido!\n");
									} else if (r.ship() != null) {
										if (r.sunk()) {
											aiShipsRemaining[0]--;
											resultMsg.append("🔥 Afundaste: ")
													.append(r.ship().getCategory()).append("!\n");
										} else {
											resultMsg.append("💥 Acertaste numa ")
													.append(r.ship().getCategory()).append("!\n");
										}
									} else {
										resultMsg.append("💧 Tiro na água.\n");
									}
								}
								resultMsg.append("Navios da IA restantes: ")
										.append(Math.max(0, aiShipsRemaining[0]));
								System.out.println(resultMsg);

								// Atualizar tabuleiro
								BoardVisualizer.atualizar(myFleet, game.getAlienMoves(),
										game.getMyMoves(), true);
							}
						}

						// -------------------------------------------------------
						// Fim do jogo
						// -------------------------------------------------------
						if (game.getRemainingShips() == 0 && aiShipsRemaining[0] > 0) {
							System.out.println("\n💀 A tua frota foi destruída! A IA ganhou!");
						} else if (aiShipsRemaining[0] <= 0) {
							System.out.println("\n🏆 Afundaste toda a frota da IA! Ganhaste!");
						}

						game.over();
						BoardVisualizer.fechar();
						exportAndSaveStats(game);
						System.exit(0);
					}
				case STATS:
					GameStatsPanel.mostrar();
					break;

				case RESETSTATS:
					GameStatsRepository.reset();
					break;
				default:
					System.out.println("Que comando é esse??? Repete ...");
			}
			System.out.print("> ");
			command = in.next();
		}
		BoardVisualizer.fechar();
		System.out.println(GOODBYE_MESSAGE);
	}

	private static void exportAndSaveStats(IGame game) {

			String pdfFile  = "historico_partida_" + System.currentTimeMillis() + ".pdf";
			String jsonFile = "historico_partida_" + System.currentTimeMillis() + ".json";
			GamePdfExporter.export(game, pdfFile);
			System.out.println("Histórico PDF exportado para: " + pdfFile);
			GameJsonExporter.export(game, jsonFile);
			System.out.println("Histórico JSON exportado para: " + jsonFile);
			GameStats stats = GameStatsRepository.load();
			stats.update(game, game.getRemainingShips() == 0);
			GameStatsRepository.save(stats);
			System.out.println("Estatísticas guardadas.");
			GameStatsPanel.mostrar();

	}

	/**
	 * This function provides help information about the menu commands.
	 */
	public static void menuHelp() {
		System.out.println("======================= AJUDA DO MENU =========================");
		System.out.println("Digite um dos comandos abaixo para interagir com o jogo:");
		System.out.println("- " + GERAFROTA + ": Gera uma frota aleatória de navios.");
		System.out.println("- " + LEFROTA + ": Permite criar e carregar uma frota personalizada.");
		System.out.println("- " + STATUS + ": Mostra o status atual da frota.)");
		System.out.println("- " + MAPA + ": Exibe o mapa da frota.");
		System.out.println("- " + RAJADA + ": Realiza uma rajada de disparos.");
		System.out.println("- " + RAJADAJSON + ": Insere uma rajada em formato JSON (para jogar contra LLM).");
		System.out.println("- " + SIMULA + ": Simula um jogo completo.");
		System.out.println("- " + IAJOGO2P + ": Jogo bidirecional — tu e a IA atacam-se mutuamente.");
		System.out.println("- " + TIROS + ": Lista os tiros válidos realizados (* = tiro em navio, o = tiro na água)");
		System.out.println("- " + DESISTIR + ": Encerra o jogo.");
		System.out.println("- " + STATS + ": Mostra o painel de estatísticas.");
		System.out.println("- " + RESETSTATS + ": Apaga todas as estatísticas.");
		System.out.println("- " + EXPORTJSON + ": Exporta o histórico da partida para JSON.");
		System.out.println("===============================================================");
	}
	/**
	 * This operation allows the build up of a fleet, given user data
	 *
	 * @param in The scanner to read from
	 * @return The fleet that has been built
	 */
	public static Fleet buildFleet(Scanner in) {

		assert in != null;

		Fleet fleet = new Fleet();
		int i = 0; // i represents the total of successfully created ships
		while (i < Fleet.FLEET_SIZE) {
			IShip s = readShip(in);
			if (s != null) {
				boolean success = fleet.addShip(s);
				if (success)
					i++;
				else
					LOGGER.info("Falha na criacao de {} {} {}", s.getCategory(), s.getBearing(), s.getPosition());
			} else {
				LOGGER.info("Navio desconhecido!");
			}
		}
		LOGGER.info("{} navios adicionados com sucesso!", i);
		return fleet;
	}

	/**
	 * This operation reads data about a ship, build it and returns it
	 *
	 * @param in The scanner to read from
	 * @return The created ship based on the data that has been read
	 */
	public static Ship readShip(Scanner in) {

		assert in != null;

		String shipKind = in.next();
		Position pos = readPosition(in);
		char c = in.next().charAt(0);
		Compass bearing = Compass.charToCompass(c);
		return Ship.buildShip(shipKind, bearing, pos);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The position that has been read
	 */
	public static Position readPosition(Scanner in) {

		assert in != null;

		int row = in.nextInt();
		int column = in.nextInt();
		return new Position(row, column);
	}

	/**
	 * This operation allows reading a position in the map
	 *
	 * @param in The scanner to read from
	 * @return The classic position that has been read
	 */
	public static IPosition readClassicPosition(@NotNull Scanner in) {
		// Verifica se ainda há tokens disponíveis
		if (!in.hasNext()) {
			throw new IllegalArgumentException("Nenhuma posição válida encontrada!");
		}

		String part1 = in.next(); // Primeiro token
		String part2 = null;

		if (in.hasNextInt()) {
			part2 = in.next(); // Segundo token, se disponível
		}

		String input = (part2 != null) ? part1 + part2 : part1;

		// Normalizar o input para tratar letras maiúsculas e minúsculas
		input = input.toUpperCase();

		// Verificar os dois formatos possíveis: compactos e com espaço
		if (input.matches("[A-Z]\\d+")) {
			char column = input.charAt(0); // Extrair a coluna
			int row = Integer.parseInt(input.substring(1)); // Extrair a linha
			return new Position(column, row);
		} else {
			throw new IllegalArgumentException("Formato inválido. Use 'A3', 'A 3' ou similar.");
		}
	}

	/**
	 * Lê as posições dos tiros do jogador a partir do scanner.
	 */
	private static List<IPosition> readPlayerShots(Scanner in) {
		List<IPosition> shots = new ArrayList<>();
		String line = in.nextLine().trim();
		Scanner lineScanner = new Scanner(line);
		while (shots.size() < Game.NUMBER_SHOTS && lineScanner.hasNext()) {
			shots.add(readClassicPosition(lineScanner));
		}
		if (shots.size() != Game.NUMBER_SHOTS) {
			throw new IllegalArgumentException("Insere exatamente " + Game.NUMBER_SHOTS + " posições!");
		}
		return shots;
	}



}