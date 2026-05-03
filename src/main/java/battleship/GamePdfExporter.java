package battleship;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

    /**
     * Exporta o histórico de jogadas de uma partida de Batalha Naval para um ficheiro PDF.
     * Utiliza a biblioteca OpenPDF (com.github.librepdf:openpdf).
     */
    public class GamePdfExporter {

        private static final Logger LOGGER = LogManager.getLogger();

        // Cores do tema naval
        private static final Color COLOR_NAVY     = new Color(10, 36, 99);
        private static final Color COLOR_OCEAN    = new Color(30, 100, 180);
        private static final Color COLOR_HIT      = new Color(200, 50, 50);
        private static final Color COLOR_MISS     = new Color(70, 130, 180);
        private static final Color COLOR_HEADER   = new Color(230, 238, 250);
        private static final Color COLOR_ROW_ALT  = new Color(245, 249, 255);
        private static final Color COLOR_WHITE    = Color.WHITE;

        // Fontes
        private static final Font FONT_TITLE      = new Font(Font.HELVETICA, 20, Font.BOLD,   COLOR_NAVY);
        private static final Font FONT_SUBTITLE   = new Font(Font.HELVETICA, 11, Font.ITALIC, COLOR_OCEAN);
        private static final Font FONT_SECTION    = new Font(Font.HELVETICA, 13, Font.BOLD,   COLOR_NAVY);
        private static final Font FONT_TABLE_HDR  = new Font(Font.HELVETICA, 10, Font.BOLD,   COLOR_WHITE);
        private static final Font FONT_SUMMARY    = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.DARK_GRAY);
        private static final Font FONT_SUMMARY_V  = new Font(Font.HELVETICA, 10, Font.BOLD,   COLOR_NAVY);

        private GamePdfExporter() {
            // Utility class — prevent instantiation
        }
        /**
         * Gera o PDF do histórico de jogadas e guarda-o no caminho indicado.
         *
         * @param game     a instância do jogo com o histórico de movimentos
         * @param filePath o caminho completo onde o PDF será guardado (ex: "partida.pdf")
         * @throws RuntimeException se ocorrer um erro na criação do ficheiro
         */
        public static void export(IGame game, String filePath) {
            Document document = new Document(PageSize.A4, 50, 50, 60, 50);

            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                writer.setPageEvent(new HeaderFooterEvent());
                document.open();

                addTitle(document, game);
                addSummary(document, game);
                addMovesTable(document, game.getAlienMoves(), "Jogadas do Inimigo (tiros no meu tabuleiro)");
                document.add(new Paragraph(" "));
                addMovesTable(document, game.getMyMoves(), "As Minhas Jogadas (tiros no tabuleiro inimigo)");

                document.close();
                LOGGER.info("PDF exportado com sucesso: {}", filePath);

            } catch (DocumentException | IOException e) {
                throw new RuntimeException("Erro ao gerar o PDF: " + e.getMessage(), e);
            }
        }

        // -----------------------------------------------------------------------
        // Secção: Título
        // -----------------------------------------------------------------------

        private static void addTitle(Document doc, IGame game) throws DocumentException {
            // Título principal
            Paragraph title = new Paragraph("Batalha Naval", FONT_TITLE);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            doc.add(title);

            // Subtítulo com data/hora
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            Paragraph subtitle = new Paragraph("Histórico completo da partida  •  " + now, FONT_SUBTITLE);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(18);
            doc.add(subtitle);

            // Linha separadora
            addSeparator(doc);
        }

        // -----------------------------------------------------------------------
        // Secção: Resumo da partida
        // -----------------------------------------------------------------------

        private static void addSummary(Document doc, IGame game) throws DocumentException {
            Paragraph sectionTitle = new Paragraph("Resumo da Partida", FONT_SECTION);
            sectionTitle.setSpacingBefore(14);
            sectionTitle.setSpacingAfter(8);
            doc.add(sectionTitle);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingAfter(14);

            addSummaryCell(table, "Jogadas totais",     String.valueOf(game.getAlienMoves().size()));
            addSummaryCell(table, "Acertos",            String.valueOf(game.getHits()));
            addSummaryCell(table, "Navios afundados",   String.valueOf(game.getSunkShips()));
            addSummaryCell(table, "Navios restantes",   String.valueOf(game.getRemainingShips()));
            addSummaryCell(table, "Tiros inválidos",    String.valueOf(game.getInvalidShots()));
            addSummaryCell(table, "Tiros repetidos",    String.valueOf(game.getRepeatedShots()));
            addSummaryCell(table, "Tiros por jogada",   String.valueOf(Game.NUMBER_SHOTS));
            addSummaryCell(table, "Tamanho tabuleiro",  Game.BOARD_SIZE + " x " + Game.BOARD_SIZE);

            doc.add(table);
            addSeparator(doc);
        }

        private static void addSummaryCell(PdfPTable table, String label, String value) {
            PdfPCell cell = new PdfPCell();
            cell.setBorderColor(COLOR_OCEAN);
            cell.setBorderWidth(0.5f);
            cell.setPadding(8);
            cell.setBackgroundColor(COLOR_HEADER);

            Paragraph p = new Paragraph();
            p.add(new Chunk(label + "\n", FONT_SUMMARY));
            p.add(new Chunk(value, FONT_SUMMARY_V));
            cell.addElement(p);
            table.addCell(cell);
        }

        // -----------------------------------------------------------------------
        // Secção: Tabela de jogadas
        // -----------------------------------------------------------------------

        private static void addMovesTable(Document doc, List<IMove> moves, String title) throws DocumentException {
            Paragraph sectionTitle = new Paragraph(title, FONT_SECTION);
            sectionTitle.setSpacingBefore(14);
            sectionTitle.setSpacingAfter(8);
            doc.add(sectionTitle);

            if (moves == null || moves.isEmpty()) {
                doc.add(new Paragraph("Sem jogadas registadas.", FONT_SUMMARY));
                return;
            }

            // Colunas: Nº | Tiro 1 | Tiro 2 | Tiro 3 | Resultado
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{8f, 17f, 17f, 17f, 41f});
            table.setSpacingAfter(10);
            table.setHeaderRows(1);

            // Cabeçalho
            addHeaderCell(table, "Nº");
            addHeaderCell(table, "Tiro 1");
            addHeaderCell(table, "Tiro 2");
            addHeaderCell(table, "Tiro 3");
            addHeaderCell(table, "Resultado");

            // Linhas de dados
            int row = 0;
            for (IMove move : moves) {
                Color bg = (row % 2 == 0) ? COLOR_WHITE : COLOR_ROW_ALT;
                List<IPosition> shots = move.getShots();
                List<IGame.ShotResult> results = move.getShotResults();

                addBodyCell(table, String.valueOf(move.getNumber()), bg, Element.ALIGN_CENTER);

                // Tiros 1, 2, 3
                for (int i = 0; i < Game.NUMBER_SHOTS; i++) {
                    if (i < shots.size()) {
                        IPosition pos = shots.get(i);
                        String shotStr = pos.toString() + (pos.isInside() ? "" : " (!)");
                        Color textColor = getShotColor(results, i);
                        addBodyCellColored(table, shotStr, bg, textColor, Element.ALIGN_CENTER);
                    } else {
                        addBodyCell(table, "-", bg, Element.ALIGN_CENTER);
                    }
                }

                // Resultado textual
                String resultText = buildResultText(results);
                addBodyCell(table, resultText, bg, Element.ALIGN_LEFT);

                row++;
            }

            doc.add(table);
        }

        // -----------------------------------------------------------------------
        // Helpers de células
        // -----------------------------------------------------------------------

        private static void addHeaderCell(PdfPTable table, String text) {
            PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TABLE_HDR));
            cell.setBackgroundColor(COLOR_NAVY);
            cell.setBorderColor(COLOR_OCEAN);
            cell.setBorderWidth(0.5f);
            cell.setPadding(7);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }

        private static void addBodyCell(PdfPTable table, String text, Color bg, int align) {
            addBodyCellColored(table, text, bg, Color.DARK_GRAY, align);
        }

        private static void addBodyCellColored(PdfPTable table, String text, Color bg, Color fg, int align) {
            Font f = new Font(Font.HELVETICA, 9, Font.NORMAL, fg);
            PdfPCell cell = new PdfPCell(new Phrase(text, f));
            cell.setBackgroundColor(bg);
            cell.setBorderColor(new Color(200, 215, 235));
            cell.setBorderWidth(0.4f);
            cell.setPadding(6);
            cell.setHorizontalAlignment(align);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }

        // -----------------------------------------------------------------------
        // Helpers de lógica
        // -----------------------------------------------------------------------

        private static Color getShotColor(List<IGame.ShotResult> results, int index) {
            if (results == null || index >= results.size()) return Color.DARK_GRAY;
            IGame.ShotResult r = results.get(index);
            if (!r.valid())    return new Color(150, 150, 150); // cinzento — inválido
            if (r.repeated())  return new Color(180, 120, 0);   // laranja — repetido
            if (r.ship() != null) return COLOR_HIT;             // vermelho — acerto
            return COLOR_MISS;                                   // azul — água
        }

        private static String buildResultText(List<IGame.ShotResult> results) {
            if (results == null || results.isEmpty()) return "—";

            int hits = 0;
            int misses = 0;
            int repeated = 0;
            int invalid = 0;
            int sunk = 0;
            StringBuilder sunkNames = new StringBuilder();

            for (IGame.ShotResult r : results) {
                if (!r.valid()) { invalid++; continue; }
                if (r.repeated()) { repeated++; continue; }
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

        // -----------------------------------------------------------------------
        // Linha separadora
        // -----------------------------------------------------------------------

        private static void addSeparator(Document doc) throws DocumentException {
            Paragraph sep = new Paragraph(" ");
            sep.setSpacingAfter(2);
            doc.add(sep);
        }

        // -----------------------------------------------------------------------
        // Cabeçalho e rodapé de página
        // -----------------------------------------------------------------------

        static class HeaderFooterEvent extends PdfPageEventHelper {
            private static final Font FONT_FOOTER = new Font(Font.HELVETICA, 8, Font.ITALIC, Color.GRAY);

            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                PdfContentByte cb = writer.getDirectContent();
                float x = (document.left() + document.right()) / 2;
                float y = document.bottom() - 15;

                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        new Phrase("Batalha Naval  •  Página " + writer.getPageNumber(), FONT_FOOTER),
                        x, y, 0);

                // Linha no rodapé
                cb.setColorStroke(COLOR_OCEAN);
                cb.setLineWidth(0.5f);
                cb.moveTo(document.left(), document.bottom() - 5);
                cb.lineTo(document.right(), document.bottom() - 5);
                cb.stroke();
            }
        }
    }

