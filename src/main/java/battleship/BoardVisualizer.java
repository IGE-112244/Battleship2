package battleship;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

/**
 * Visualização gráfica do tabuleiro com cores usando a biblioteca Lanterna.
 * A janela fica aberta durante todo o jogo e atualiza após cada jogada.
 */
public class BoardVisualizer {

    private static Screen screen = null;

    /**
     * Inicializa a janela gráfica — chamar apenas uma vez no início do jogo.
     */
    public static void iniciar() {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            screen = new TerminalScreen(terminal);
            screen.startScreen();
        } catch (IOException e) {
            System.err.println("Erro ao iniciar visualização: " + e.getMessage());
        }
    }

    /**
     * Atualiza o tabuleiro na janela gráfica.
     *
     * @param fleet     a frota do jogador
     * @param moves     as jogadas realizadas pelo inimigo
     * @param showShots se true, mostra os tiros recebidos
     */
    public static void atualizar(IFleet fleet, List<IMove> moves, boolean showShots) {
        if (screen == null) return;

        try {
            screen.clear();
            TextGraphics tg = screen.newTextGraphics();

            // Título


            // Cabeçalho das colunas
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(2, 1, "1 2 3 4 5 6 7 8 9 10");

            // Construir o mapa
            char[][] map = new char[Game.BOARD_SIZE][Game.BOARD_SIZE];
            for (int r = 0; r < Game.BOARD_SIZE; r++)
                for (int c = 0; c < Game.BOARD_SIZE; c++)
                    map[r][c] = '.';

            // Colocar navios no mapa
            for (IShip ship : fleet.getShips())
                for (IPosition pos : ship.getPositions())
                    map[pos.getRow()][pos.getColumn()] = '#';

            // Colocar tiros no mapa
            if (showShots)
                for (IMove move : moves)
                    for (IPosition shot : move.getShots())
                        if (shot.isInside()) {
                            int r = shot.getRow();
                            int c = shot.getColumn();
                            if (map[r][c] == '#')
                                map[r][c] = '*';
                            else
                                map[r][c] = 'o';
                        }

            // Desenhar o mapa com cores
            for (int row = 0; row < Game.BOARD_SIZE; row++) {
                tg.setForegroundColor(TextColor.ANSI.YELLOW);
                char rowLabel = (char) ('A' + row);
                tg.putString(0, row + 2, String.valueOf(rowLabel));  // só a letra, sem " |"
                tg.putString(1, row + 2, "|");                        // pipe separado

                for (int col = 0; col < Game.BOARD_SIZE; col++) {
                    char cell = map[row][col];
                    int x = 2 + col * 2;  // começa em 2, espaçamento de 2
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
                    tg.putString(x, y, cell + " ");  // célula + espaço
                }
            }

            // Legenda
            int legendaY = Game.BOARD_SIZE + 3;
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(0, legendaY, "# = navio");
            tg.setForegroundColor(TextColor.ANSI.RED);
            tg.putString(0, legendaY + 1, "* = acerto");
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(0, legendaY + 2, "o = tiro na agua");
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(0, legendaY + 3, ". = vazio");

            screen.refresh();

        } catch (IOException e) {
            System.err.println("Erro ao atualizar visualização: " + e.getMessage());
        }
    }

    /**
     * Fecha a janela gráfica — chamar quando o jogo terminar.
     */
    public static void fechar() {
        if (screen != null) {
            try {
                screen.stopScreen();
                screen = null;
            } catch (IOException e) {
                System.err.println("Erro ao fechar visualização: " + e.getMessage());
            }
        }
    }
}