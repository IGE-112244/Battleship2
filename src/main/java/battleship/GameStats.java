package battleship;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * Modelo de dados que representa as estatísticas acumuladas do jogador.
 * É serializado/desserializado em JSON pelo GameStatsRepository.
 */
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class GameStats {

    private int totalJogos;
    private int jogosGanhos;
    private int totalTiros;
    private int tirosAcertados;

    // Construtor vazio necessário para o Jackson
    public GameStats() {
        this.totalJogos     = 0;
        this.jogosGanhos    = 0;
        this.totalTiros     = 0;
        this.tirosAcertados = 0;
    }

    /**
     * Atualiza as estatísticas com os dados do jogo terminado.
     *
     * @param game  a instância do jogo terminado
     * @param ganhou true se o jogador ganhou (todos os navios afundados)
     */
    public void update(IGame game, boolean ganhou) {
        totalJogos++;
        if (ganhou) jogosGanhos++;

        // Total de tiros válidos disparados pelo inimigo neste jogo
        int tirosNesteJogo = game.getAlienMoves().size() * Game.NUMBER_SHOTS
                - game.getInvalidShots()
                - game.getRepeatedShots();

        totalTiros     += tirosNesteJogo;
        tirosAcertados += game.getHits();
    }

    /**
     * Calcula a percentagem de precisão dos tiros.
     *
     * @return precisão em percentagem (0.0 a 100.0)
     */
    @JsonIgnore
    public double getAccuracy() {
        if (totalTiros == 0) return 0.0;
        return (tirosAcertados * 100.0) / totalTiros;
    }

    /**
     * Repõe todas as estatísticas a zero.
     */
    public void reset() {
        totalJogos     = 0;
        jogosGanhos    = 0;
        totalTiros     = 0;
        tirosAcertados = 0;
    }

    // -----------------------------------------------------------------------
    // Getters e Setters (necessários para o Jackson)
    // -----------------------------------------------------------------------

    public int getTotalJogos()      { return totalJogos; }
    public int getJogosGanhos()     { return jogosGanhos; }
    public int getTotalTiros()      { return totalTiros; }
    public int getTirosAcertados()  { return tirosAcertados; }

    public void setTotalJogos(int totalJogos)           { this.totalJogos = totalJogos; }
    public void setJogosGanhos(int jogosGanhos)         { this.jogosGanhos = jogosGanhos; }
    public void setTotalTiros(int totalTiros)           { this.totalTiros = totalTiros; }
    public void setTirosAcertados(int tirosAcertados)   { this.tirosAcertados = tirosAcertados; }
}
