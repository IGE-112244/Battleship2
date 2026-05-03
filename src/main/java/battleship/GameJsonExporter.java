package battleship;

// CRIADO PARA RESOLVER A ISSUE #7
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Exporta o histórico completo de uma partida de Batalha Naval para um ficheiro JSON.
 * Utiliza a biblioteca Jackson (com.fasterxml.jackson.core:jackson-databind).
 */
public class GameJsonExporter {

    private GameJsonExporter() {
        // Utility class — prevent instantiation
    }
    /**
     * Exporta o histórico completo do jogo para um ficheiro JSON no caminho indicado.
     *
     * @param game     a instância do jogo com o histórico de movimentos
     * @param filePath o caminho completo onde o JSON será guardado (ex: "partida.json")
     * @throws RuntimeException se ocorrer um erro na criação do ficheiro
     */
    public static void export(IGame game, String filePath) {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Construir o mapa raiz do JSON
        Map<String, Object> root = new LinkedHashMap<>();

        // Data e hora
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        root.put("data", now);

        // Resumo da partida
        Map<String, Object> resumo = new LinkedHashMap<>();
        resumo.put("totalJogadas",      game.getAlienMoves().size());
        resumo.put("acertos",           game.getHits());
        resumo.put("naviosAfundados",   game.getSunkShips());
        resumo.put("naviosRestantes",   game.getRemainingShips());
        resumo.put("tirosInvalidos",    game.getInvalidShots());
        resumo.put("tirosRepetidos",    game.getRepeatedShots());
        resumo.put("tirosPorJogada",    Game.NUMBER_SHOTS);
        resumo.put("tamanhoTabuleiro",  Game.BOARD_SIZE + "x" + Game.BOARD_SIZE);
        root.put("resumo", resumo);

        // Jogadas do inimigo (tiros no meu tabuleiro)
        root.put("jogadasInimigo", buildMovesList(game.getAlienMoves()));

        // As minhas jogadas (tiros no tabuleiro inimigo)
        root.put("minhasJogadas", buildMovesList(game.getMyMoves()));

        // Escrever para ficheiro
        try {
            objectMapper.writeValue(new File(filePath), root);
            System.out.println("JSON exportado com sucesso: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao exportar o JSON: " + e.getMessage(), e);
        }
    }

    // -----------------------------------------------------------------------
    // Constrói a lista de jogadas para serialização
    // -----------------------------------------------------------------------

    private static List<Map<String, Object>> buildMovesList(List<IMove> moves) {

        List<Map<String, Object>> jogadas = new ArrayList<>();

        if (moves == null || moves.isEmpty())
            return jogadas;

        for (IMove move : moves) {
            Map<String, Object> jogada = new LinkedHashMap<>();

            jogada.put("numero", move.getNumber());

            // Lista de tiros
            List<String> tiros = new ArrayList<>();
            for (IPosition pos : move.getShots()) {
                tiros.add(pos.toString());
            }
            jogada.put("tiros", tiros);

            // Resultado textual
            jogada.put("resultado", buildResultText(move.getShotResults()));

            jogadas.add(jogada);
        }

        return jogadas;
    }

    // -----------------------------------------------------------------------
    // Constrói o texto do resultado de uma jogada
    // -----------------------------------------------------------------------

    private static String buildResultText(List<IGame.ShotResult> results) {

        if (results == null || results.isEmpty()) return "—";

        int hits = 0, misses = 0, repeated = 0, invalid = 0, sunk = 0;
        StringBuilder sunkNames = new StringBuilder();

        for (IGame.ShotResult r : results) {
            if (!r.valid())    { invalid++;  continue; }
            if (r.repeated())  { repeated++; continue; }
            if (r.ship() != null) {
                hits++;
                if (r.sunk()) {
                    sunk++;
                    if (sunkNames.length() > 0) sunkNames.append(", ");
                    sunkNames.append(r.ship().getCategory());
                }
            } else {
                misses++;
            }
        }

        StringBuilder sb = new StringBuilder();
        if (hits > 0)     sb.append(hits).append(" acerto(s)");
        if (sunk > 0)     sb.append(" [").append(sunkNames).append(" ao fundo]");
        if (misses > 0)   { if (sb.length() > 0) sb.append(" | "); sb.append(misses).append(" água"); }
        if (repeated > 0) { if (sb.length() > 0) sb.append(" | "); sb.append(repeated).append(" repetido(s)"); }
        if (invalid > 0)  { if (sb.length() > 0) sb.append(" | "); sb.append(invalid).append(" inválido(s)"); }
        if (sb.length() == 0) sb.append("Sem resultado");

        return sb.toString();
    }
}
