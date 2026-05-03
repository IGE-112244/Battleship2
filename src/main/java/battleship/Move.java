package battleship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Shot
 *
 * @author Your Name
 * Date: 20/02/2026
 * Time: 19:39
 */
public class Move implements IMove {

	public static final String TIRO = " tiro";
	//-------------------------------------------------------------------
	private final int number;
	private final List<IPosition> shots;
	private final List<IGame.ShotResult> shotResults;

	//-------------------------------------------------------------------
	public Move(int moveNumber, List<IPosition> moveShots, List<IGame.ShotResult> moveResults) {
		this.number = moveNumber;
		this.shots = moveShots;
		this.shotResults = moveResults;
	}

	@Override
	public String toString() {
		return "Move{" +
				"number=" + number +
				", shots=" + shots.size() +
				", results=" + shotResults.size() +
				'}';
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public List<IPosition> getShots() {
		return this.shots;
	}

	@Override
	public List<IGame.ShotResult> getShotResults() {
		return this.shotResults;
	}

	/**
	 * Processes the results of enemy fire on the game board, analyzing the outcomes of shots,
	 * such as valid shots, repeated shots, missed shots, hits on ships, and sunk ships. It can
	 * also display a detailed summary of the shot results if verbose mode is activated.
	 *
	 * @param verbose a boolean indicating whether a detailed summary should be printed to the console
	 *                for the processed enemy fire data.
	 * @return a JSON-formatted string that encapsulates the results, including counts of valid shots,
	 *         repeated shots, missed shots, shots outside the game board, and details of hits and
	 *         sunk ships.
	 */
	@Override
	public String processEnemyFire(boolean verbose) {

		int validShots = 0;
		int repeatedShots = 0;
		int missedShots = 0;

		Map<String, Integer> sunkBoatsCount = new HashMap<>(); // Rastrear quantos navios de cada tipo afundaram
		Map<String, Integer> hitsPerBoat = new HashMap<>();

		// Processar cada resultado de tiro
		processShotResults result = getProcessShotResults(repeatedShots, validShots, missedShots, hitsPerBoat, sunkBoatsCount);

		// Determinar número de tiros fora do tabuleiro
		int outsideShots = Game.NUMBER_SHOTS - result.validShots() - result.repeatedShots();

		if (verbose) {
			// Construção da mensagem de saída
			StringBuilder output = new StringBuilder();

			if (result.validShots() == 0 && result.repeatedShots() > 0) {
				output.append(result.repeatedShots()).append(TIRO).append(result.repeatedShots() > 1 ? "s" : "").append(" repetido").append(result.repeatedShots() > 1 ? "s" : "");
			} else {
				if (result.validShots() > 0) {
					output.append(result.validShots()).append(TIRO).append(result.validShots() > 1 ? "s" : "").append(" válido").append(result.validShots() > 1 ? "s" : "").append(": ");
				}

				// Atualizar lógica para contar múltiplos barcos afundados do mesmo tipo
				if (!sunkBoatsCount.isEmpty()) {
					for (Map.Entry<String, Integer> entry : sunkBoatsCount.entrySet()) {
						String boatName = entry.getKey();
						int count = entry.getValue();
						output.append(count).append(" ").append(boatName).append(count > 1 ? "s" : "").append(" ao fundo").append(" + ");
					}
				}

				if (!hitsPerBoat.isEmpty()) {
					for (Map.Entry<String, Integer> entry : hitsPerBoat.entrySet()) {
						String boatName = entry.getKey();
						int hits = entry.getValue();
						if (!sunkBoatsCount.containsKey(boatName)) {
							output.append(hits).append(TIRO).append(hits > 1 ? "s" : "").append(" num(a) ").append(boatName).append(" + ");
						}
					}
				}

				if (result.missedShots() > 0) {
					output.append(result.missedShots()).append(TIRO).append(result.missedShots() > 1 ? "s" : "").append(" na água");
				} else if (!sunkBoatsCount.isEmpty() || !hitsPerBoat.isEmpty()) {
					output.setLength(output.length() - 2); // Remover o "+" final
				}

				if (result.repeatedShots() > 0) {
					if (result.validShots() > 0) {
						output.append(", ");
					}
					output.append(result.repeatedShots()).append(TIRO).append(result.repeatedShots() > 1 ? "s" : "").append(" repetido").append(result.repeatedShots() > 1 ? "s" : "");
				}
			}

			// Adicionar contagem de tiros fora do tabuleiro
			if (outsideShots > 0) {
				if (!output.isEmpty()) {
					output.append(", ");
				}
				output.append(outsideShots).append(TIRO).append(outsideShots > 1 ? "s" : "").append(" exterior").append(outsideShots > 1 ? "es" : "");
			}

			// Imprimir na consola se verbose for true
			System.out.println("Jogada nº" + this.number + " -> " + output);
		}

		// Criar o mapa para o JSON
		Map<String, Object> response = new HashMap<>();
		response.put("validShots", result.validShots());
		response.put("outsideShots", outsideShots);
		response.put("repeatedShots", result.repeatedShots());
		response.put("missedShots", result.missedShots());

		// Criar a lista de barcos afundados
		List<Map<String, Object>> sunkBoats = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : sunkBoatsCount.entrySet()) {
			Map<String, Object> boat = new HashMap<>();
			boat.put("type", entry.getKey());
			boat.put("count", entry.getValue());
			sunkBoats.add(boat);
		}
		response.put("sunkBoats", sunkBoats);

		// Criar a lista de acertos em barcos que não foram afundados
		List<Map<String, Object>> boatHits = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : hitsPerBoat.entrySet()) {
			if (!sunkBoatsCount.containsKey(entry.getKey())) {
				Map<String, Object> boat = new HashMap<>();
				boat.put("type", entry.getKey());
				boat.put("hits", entry.getValue());
				boatHits.add(boat);
			}
		}
		response.put("hitsOnBoats", boatHits);

		// Serializar o JSON utilizando Jackson
		String jsonString;

		// Serializar os tiros gerados em JSON usando a biblioteca Jackson
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Erro ao serializar o JSON dos resultados da jogada", e);
		}

		System.out.println(jsonString);
		System.out.println();

		// Retornar o JSON
		return jsonString;
	}

	private @NotNull processShotResults getProcessShotResults(int repeatedShots, int validShots, int missedShots, Map<String, Integer> hitsPerBoat, Map<String, Integer> sunkBoatsCount) {
		for (IGame.ShotResult result : this.shotResults) {
			if (!result.valid()) {
				// Tiro inválido - apenas ignorar
				continue;
			}

			if (result.repeated())
				repeatedShots++; // tiro repetido
			else {
				// Tiro válido
				validShots++;
				if (result.ship() == null)
					missedShots++; // Tiro na água
				else{
					String boatName = result.ship().getCategory();
					hitsPerBoat.put(boatName, hitsPerBoat.getOrDefault(boatName, 0) + 1);
					if (result.sunk())
						sunkBoatsCount.put(boatName, sunkBoatsCount.getOrDefault(boatName, 0) + 1); // Contar barcos do mesmo tipo afundados
				}
			}
		}
		processShotResults result = new processShotResults(validShots, repeatedShots, missedShots);
		return result;
	}

	private record processShotResults(int validShots, int repeatedShots, int missedShots) {
	}
}