package battleship;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Visualizador gráfico do jogo da Batalha Naval.
 * Mostra os dois tabuleiros lado a lado:
 * - Esquerda: o teu tabuleiro (com os ataques da IA)
 * - Direita:  o tabuleiro da IA (com os teus ataques e resultados conhecidos)
 */
public class BoardVisualizer {
    private BoardVisualizer() {}

    private static final Logger LOGGER = LogManager.getLogger();

    private static Screen screen = null;

    // Offset horizontal do tabuleiro da IA (à direita)
    private static final int RIGHT_BOARD_OFFSET = 26;

    /**
     * Inicializa a janela gráfica.
     */
    public static void iniciar() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            LOGGER.error("Erro ao iniciar visualização: {}", e.getMessage());
        }
    }

    /**
     * Atualiza ambos os tabuleiros na janela gráfica.
     *
     * @param myFleet      a tua frota
     * @param alienMoves   jogadas da IA (ataques ao teu tabuleiro)
     * @param myMoves      as tuas jogadas (ataques ao tabuleiro da IA)
     * @param showShots    se true, mostra os tiros em ambos os tabuleiros
     */
    public static void atualizar(IFleet myFleet, List<IMove> alienMoves,
                                 List<IMove> myMoves, boolean showShots) {
        if (screen == null) return;

        try {
            screen.clear();
            TextGraphics tg = screen.newTextGraphics();

            // --- Títulos ---
            tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
            tg.putString(3,  0, "=== O TEU TABULEIRO ===");
            tg.putString(RIGHT_BOARD_OFFSET + 2, 0, "=== TABULEIRO DA IA ===");

            // --- Cabeçalho das colunas ---
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(2, 1, "1 2 3 4 5 6 7 8 9 10");
            tg.putString(RIGHT_BOARD_OFFSET + 2, 1, "1 2 3 4 5 6 7 8 9 10");

            // --- Construir o mapa do teu tabuleiro ---
            char[][] myMap = buildMyMap(myFleet, alienMoves, showShots);

            // --- Construir o mapa do tabuleiro da IA ---
            char[][] aiMap = buildAiMap(myMoves);

            // --- Desenhar ambos os tabuleiros ---
            drawBoard(tg, myMap,  0,                   true);
            drawBoard(tg, aiMap, RIGHT_BOARD_OFFSET,   false);

            // --- Legenda ---
            int legendaY = Game.BOARD_SIZE + 3;
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(0, legendaY,     "# = navio");
            tg.setForegroundColor(TextColor.ANSI.RED);
            tg.putString(0, legendaY + 1, "* = acerto");
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(0, legendaY + 2, "o = tiro na agua");
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(0, legendaY + 3, ". = vazio");

            tg.setForegroundColor(TextColor.ANSI.RED);
            tg.putString(RIGHT_BOARD_OFFSET, legendaY,     "* = acerto confirmado");
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(RIGHT_BOARD_OFFSET, legendaY + 1, "o = tiro na agua");
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(RIGHT_BOARD_OFFSET, legendaY + 2, ". = nao explorado");

            screen.refresh();

        } catch (IOException e) {
            LOGGER.error("Erro ao atualizar visualização: {}", e.getMessage());
        }
    }

    /**
     * Versão simplificada — atualiza apenas o teu tabuleiro (compatibilidade).
     */
    public static void atualizar(IFleet myFleet, List<IMove> alienMoves, boolean showShots) {
        atualizar(myFleet, alienMoves, List.of(), showShots);
    }

    // -----------------------------------------------------------------------
    // Construção dos mapas
    // -----------------------------------------------------------------------

    /**
     * Constrói o mapa do teu tabuleiro com a tua frota e os ataques da IA.
     */
    private static char[][] buildMyMap(IFleet myFleet, List<IMove> alienMoves, boolean showShots) {
        char[][] map = initMap();

        // Colocar navios
        for (IShip ship : myFleet.getShips())
            for (IPosition pos : ship.getPositions())
                map[pos.getRow()][pos.getColumn()] = '#';

        // Colocar tiros da IA
        if (showShots)
            for (IMove move : alienMoves)
                for (IPosition shot : move.getShots())
                    if (shot.isInside()) {
                        int r = shot.getRow();
                        int c = shot.getColumn();
                        map[r][c] = (map[r][c] == '#') ? '*' : 'o';
                    }

        return map;
    }

    /**
     * Constrói o mapa do tabuleiro da IA com os resultados conhecidos dos teus tiros.
     */
    private static char[][] buildAiMap(List<IMove> myMoves) {
        char[][] map = initMap();

        for (IMove move : myMoves) {
            List<IPosition> shots = move.getShots();
            List<IGame.ShotResult> results = move.getShotResults();

            for (int i = 0; i < shots.size(); i++) {
                IPosition pos = shots.get(i);
                if (!pos.isInside()) continue;

                int r = pos.getRow();
                int c = pos.getColumn();

                if (i < results.size()) {
                    IGame.ShotResult result = results.get(i);
                    if (!result.valid() || result.repeated()) continue;
                    map[r][c] = (result.ship() != null) ? '*' : 'o';
                }
            }
        }

        return map;
    }

    private static char[][] initMap() {
        char[][] map = new char[Game.BOARD_SIZE][Game.BOARD_SIZE];
        for (int r = 0; r < Game.BOARD_SIZE; r++)
            for (int c = 0; c < Game.BOARD_SIZE; c++)
                map[r][c] = '.';
        return map;
    }

    // -----------------------------------------------------------------------
    // Desenho do tabuleiro
    // -----------------------------------------------------------------------

    private static void drawBoard(TextGraphics tg, char[][] map, int xOffset, boolean showShips) {
        for (int row = 0; row < Game.BOARD_SIZE; row++) {
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            char rowLabel = (char) ('A' + row);
            tg.putString(xOffset,     row + 2, String.valueOf(rowLabel));
            tg.putString(xOffset + 1, row + 2, "|");

            for (int col = 0; col < Game.BOARD_SIZE; col++) {
                char cell = map[row][col];
                int x = xOffset + 2 + col * 2;
                int y = row + 2;

                switch (cell) {
                    case '#':
                        tg.setForegroundColor(TextColor.ANSI.GREEN);
                        break;
                    case '*':
                        tg.setForegroundColor(TextColor.ANSI.RED);
                        break;
                    case 'o':
                        tg.setForegroundColor(TextColor.ANSI.CYAN);
                        break;
                    default:
                        tg.setForegroundColor(TextColor.ANSI.WHITE);
                        break;
                }
                tg.putString(x, y, cell + " ");
            }
        }
    }

    /**
     * Fecha a janela gráfica.
     */
    public static void fechar() {
        if (screen != null) {
            try {
                screen.stopScreen();
                screen = null;
            } catch (IOException e) {
                LOGGER.error("Erro ao fechar visualização: {}", e.getMessage());
            }
        }
    }
}