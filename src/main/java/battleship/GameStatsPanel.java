package battleship;

import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;

/**
 * Painel visual de estatísticas do jogador.
 * Apresenta métricas acumuladas e um gráfico de barras com XChart.
 * É visualmente distinto da grelha de jogo.
 */
public class GameStatsPanel {

    private static JFrame frame;

    private GameStatsPanel() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Abre (ou atualiza) o painel de estatísticas com os dados atuais.
     * Se o painel já estiver aberto, fecha e reabre com dados atualizados.
     */
    public static void mostrar() {
        GameStats stats = GameStatsRepository.load();

        // Fechar janela anterior se existir
        if (frame != null && frame.isVisible()) {
            frame.dispose();
        }

        frame = new JFrame("📊 Estatísticas do Jogador");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(15, 40, 80)); // azul naval escuro

        // -----------------------------------------------------------------------
        // Painel de métricas (topo)
        // -----------------------------------------------------------------------
        JPanel metricsPanel = buildMetricsPanel(stats);

        frame.add(metricsPanel, BorderLayout.NORTH);

        // -----------------------------------------------------------------------
        // Gráfico de barras (centro) — usando XChart
        // -----------------------------------------------------------------------
        JPanel chartPanel = buildChartPanel(stats);
        frame.add(chartPanel, BorderLayout.CENTER);

        // -----------------------------------------------------------------------
        // Botão Reset Stats (fundo)
        // -----------------------------------------------------------------------
        JPanel bottomPanel = buildBottomPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // -----------------------------------------------------------------------
        // Mostrar janela
        // -----------------------------------------------------------------------
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static @NotNull JPanel buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(15, 40, 80));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JButton resetButton = new JButton("🗑️ Reset Stats");
        resetButton.setBackground(new Color(180, 40, 40));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resetButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "Tens a certeza que queres apagar todas as estatísticas?",
                    "Confirmar Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                GameStatsRepository.reset();
                frame.dispose();
                mostrar(); // Reabrir com stats a zero
            }
        });

        bottomPanel.add(resetButton);
        return bottomPanel;
    }

    private static @NotNull JPanel buildChartPanel(GameStats stats) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(500)
                .height(300)
                .title("Desempenho Acumulado")
                .xAxisTitle("")
                .yAxisTitle("Quantidade")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAxisTitlesVisible(true);
        chart.getStyler().setChartBackgroundColor(new Color(20, 55, 110));
        chart.getStyler().setChartFontColor(Color.WHITE);
        chart.getStyler().setAxisTickLabelsColor(Color.WHITE);
        chart.getStyler().setLegendBackgroundColor(new Color(20, 55, 110));
        chart.getStyler().setLegendBorderColor(new Color(20, 55, 110));
        chart.getStyler().setPlotBackgroundColor(new Color(30, 70, 130));
        chart.getStyler().setPlotBorderColor(new Color(30, 70, 130));

        // Séries do gráfico
        chart.addSeries("Jogos",
                java.util.Arrays.asList("Realizados", "Ganhos"),
                java.util.Arrays.asList(stats.getTotalJogos(), stats.getJogosGanhos()));

        chart.addSeries("Tiros",
                java.util.Arrays.asList("Total", "Acertados"),
                java.util.Arrays.asList(stats.getTotalTiros(), stats.getTirosAcertados()));

        JPanel chartPanel = new XChartPanel<>(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return chartPanel;
    }

    private static @NotNull JPanel buildMetricsPanel(GameStats stats) {
        JPanel metricsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        metricsPanel.setBackground(new Color(15, 40, 80));
        metricsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        metricsPanel.add(createMetricCard("🎮 Jogos Realizados",
                String.valueOf(stats.getTotalJogos())));
        metricsPanel.add(createMetricCard("🏆 Jogos Ganhos",
                String.valueOf(stats.getJogosGanhos())));
        metricsPanel.add(createMetricCard("🎯 Precisão",
                String.format("%.1f%%", stats.getAccuracy())));
        return metricsPanel;
    }

    /**
     * Fecha o painel de estatísticas se estiver aberto.
     */
    public static void fechar() {
        if (frame != null && frame.isVisible()) {
            frame.dispose();
        }
    }

    // -----------------------------------------------------------------------
    // Helper: cria um cartão de métrica
    // -----------------------------------------------------------------------

    private static JPanel createMetricCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(25, 60, 120));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 200), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel labelComp = new JLabel(label, SwingConstants.CENTER);
        labelComp.setForeground(new Color(170, 200, 240));
        labelComp.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JLabel valueComp = new JLabel(value, SwingConstants.CENTER);
        valueComp.setForeground(Color.WHITE);
        valueComp.setFont(new Font("SansSerif", Font.BOLD, 28));

        card.add(labelComp, BorderLayout.NORTH);
        card.add(valueComp, BorderLayout.CENTER);

        return card;
    }
}