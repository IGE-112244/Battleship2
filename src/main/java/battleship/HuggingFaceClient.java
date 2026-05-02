package battleship;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Cliente para comunicação direta com o modelo Llama-3.2-3B-Instruct
 * via Hugging Face Inference API (formato compatível com OpenAI).
 * Suporta jogo bidirecional (2 jogadores): o jogador humano e a IA.
 */
public class HuggingFaceClient {

    public static final String CONTENT = "content";
    private static final Logger log = LogManager.getLogger(HuggingFaceClient.class);
    private static String HF_TOKEN = loadToken();
    private static final String MODEL_URL = "https://router.huggingface.co/v1/chat/completions";
    private static final String MODEL_ID  = "meta-llama/Llama-3.1-8B-Instruct:cerebras";
    private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();

    // System prompt com as regras e estratégia do jogo
    private static String systemPrompt = null;

    // Frota da IA (posições declaradas no início)
    private static String aiFleetJson = null;

    // Posições já disparadas pela IA
    private static final Set<String> aiShotHistory = new LinkedHashSet<>();
    private static final Map<String, String> aiShotResults = new LinkedHashMap<>();

    // Histórico completo da conversa (mantém contexto entre chamadas)
    private static final List<ObjectNode> conversationHistory = new ArrayList<>();

    // -----------------------------------------------------------------------
    // Configuração
    // -----------------------------------------------------------------------

    private HuggingFaceClient() {
        throw new UnsupportedOperationException("Utility class");
    }
    /**
     * Define o prompt de sistema com as regras e estratégia do jogo.
     * Deve ser chamado antes de iniciar o jogo.
     */
    public static void setSystemPrompt(String prompt) {
        systemPrompt = prompt;
        conversationHistory.clear();
        aiShotHistory.clear();
        aiShotResults.clear(); // ✅ limpar também os resultados
    }

    // -----------------------------------------------------------------------
    // MODO 1: IA ataca o tabuleiro do jogador
    // -----------------------------------------------------------------------

    /**
     * Pede à IA a próxima rajada de ataque contra o tabuleiro do jogador.
     *
     * @param lastResult JSON do resultado da última jogada da IA (null na primeira jogada)
     * @return lista de posições da nova rajada
     */
    public static List<IPosition> getNextShots(String lastResult) {
        String userMessage = buildAttackMessage(lastResult);
        String responseText = callAPI(userMessage);
        List<IPosition> shots = parseShots(responseText);

        // Guardar posições no histórico
        for (IPosition pos : shots) {
            aiShotHistory.add(pos.getClassicRow() + "" + pos.getClassicColumn());
        }

        return shots;
    }

    /**
     * Inicializa o jogo — força a IA a criar a frota antes de começar.
     */
    public static void initGame() {
        conversationHistory.clear();
        aiShotHistory.clear();
        aiFleetJson = null;

        String initMessage =
                "Antes de começar o jogo, cria agora a tua frota secreta de 11 navios " +
                        "seguindo as regras. Declara a posição de cada navio internamente no teu " +
                        "Diário de Bordo mas NÃO a reveles. Responde apenas com: " +
                        "\"Frota criada. Pronto para jogar!\"";

        callAPI(initMessage);
        log.info("✅ IA criou a sua frota. Jogo pronto!");
    }

    // -----------------------------------------------------------------------
    // MODO 2: Jogador ataca a frota da IA
    // -----------------------------------------------------------------------

    /**
     * Envia os tiros do jogador à IA e recebe o resultado na frota da IA.
     *
     * @param shots lista de posições disparadas pelo jogador
     * @return JSON com o resultado dos tiros na frota da IA
     */
    public static String sendMyShots(List<IPosition> shots) {
        String userMessage = buildDefendMessage(shots);
        String responseText = callAPI(userMessage);
        return parseResult(responseText);
    }

    // -----------------------------------------------------------------------
    // Construção das mensagens
    // -----------------------------------------------------------------------

    private static String buildAttackMessage(String lastResult) {
        StringBuilder sb = new StringBuilder();

        if (lastResult != null) {
            sb.append("Resultado da última rajada:\n");
            sb.append(lastResult).append("\n\n");
        } else {
            sb.append("Jogo novo. Começa com padrão em xadrez.\n\n");
        }

        // Histórico completo de tiros e resultados
        if (!aiShotHistory.isEmpty()) {
            sb.append("=== DIÁRIO DE BORDO ===\n");
            sb.append("Posições já disparadas e resultados:\n");
            for (String pos : aiShotHistory) {
                String resultado = aiShotResults.getOrDefault(pos, "água");
                sb.append("  - ").append(pos).append(": ").append(resultado).append("\n");
            }
            sb.append("=== FIM DO DIÁRIO ===\n\n");
            sb.append("⚠️ NUNCA dispares nas ").append(aiShotHistory.size())
                    .append(" posições acima!\n\n");
        }

        sb.append("Escolhe 3 posições NOVAS não listadas acima.\n");
        sb.append("Responde APENAS com o array JSON, sem texto adicional:\n");
        sb.append("[{\"row\":\"A\",\"column\":1},{\"row\":\"B\",\"column\":2},{\"row\":\"C\",\"column\":3}]");

        return sb.toString();
    }

    /**
     * Regista o resultado de um tiro da IA para incluir no Diário de Bordo.
     */
    public static void addShotResult(String position, String result) {
        aiShotResults.put(position, result);
    }



    private static String buildDefendMessage(List<IPosition> shots) {
        StringBuilder sb = new StringBuilder();

        // Construir lista de tiros em formato simples
        StringBuilder shotsStr = new StringBuilder();
        for (IPosition pos : shots) {
            shotsStr.append(pos.getClassicRow())
                    .append(pos.getClassicColumn())
                    .append(" ");
        }

        sb.append("O jogador disparou contra a tua frota nas posições: ");
        sb.append(shotsStr.toString().trim()).append("\n\n");
        sb.append("Verifica estas posições contra a tua frota secreta.\n");
        sb.append("Responde APENAS com este JSON (sem texto adicional, sem comentários):\n");
        sb.append("{\n");
        sb.append("  \"validShots\": 3,\n");
        sb.append("  \"sunkBoats\": [],\n");
        sb.append("  \"repeatedShots\": 0,\n");
        sb.append("  \"outsideShots\": 0,\n");
        sb.append("  \"hitsOnBoats\": [],\n");
        sb.append("  \"missedShots\": 3\n");
        sb.append("}\n");
        sb.append("Substitui os valores conforme o resultado real na tua frota secreta.");

        return sb.toString();
    }

    // -----------------------------------------------------------------------
    // Chamada à API do Hugging Face (com histórico de conversa)
    // -----------------------------------------------------------------------

    private static String callAPI(String userMessage) {
        try {
            // Construir o array de mensagens com histórico completo
            ArrayNode messages = mapper.createArrayNode();

            // 1. System prompt
            if (systemPrompt != null) {
                ObjectNode systemMsg = mapper.createObjectNode();
                systemMsg.put("role",    "system");
                systemMsg.put(CONTENT, systemPrompt);
                messages.add(systemMsg);
            }

            // 2. Histórico completo da conversa
            for (ObjectNode msg : conversationHistory) {
                messages.add(msg);
            }

            // 3. Nova mensagem do utilizador
            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role",    "user");
            userMsg.put(CONTENT, userMessage);
            messages.add(userMsg);

            // Construir o body do pedido
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("model",       MODEL_ID);
            requestBody.put("max_tokens",  500);
            requestBody.put("temperature", 0.3);
            requestBody.set("messages",    messages);

            log.info("\n--- Mensagem enviada ao LLM ---");
            log.info(userMessage);
            log.info("-------------------------------\n");

            // Fazer o pedido HTTP
            Request request = new Request.Builder()
                    .url(MODEL_URL)
                    .addHeader("Authorization", "Bearer " + loadToken())
                    .addHeader("Content-Type",  "application/json")
                    .post(RequestBody.create(mapper.writeValueAsString(requestBody), JSON_TYPE))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                // Lê SEMPRE o body, mesmo em caso de erro
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new RuntimeException("Resposta da API sem corpo (body null)");
                }
                String body = responseBody.string();

                if (!response.isSuccessful()) {
                    throw new RuntimeException("Erro na API do Hugging Face: "
                            + response.code() + " - " + response.message()
                            + "\nDetalhes: " + body);
                }

                log.info("--- Resposta bruta do LLM ---");
                log.info(body);
                log.info("-----------------------------\n");

                // Extrair o conteúdo da resposta (formato OpenAI)
                JsonNode root = mapper.readTree(body);
                String assistantContent = root.get("choices").get(0)
                        .get("message").get(CONTENT).asText();

                // Guardar a mensagem do utilizador no histórico
                ObjectNode userHistory = mapper.createObjectNode();
                userHistory.put("role",    "user");
                userHistory.put(CONTENT, userMessage);
                conversationHistory.add(userHistory);

                // Guardar a resposta da IA no histórico
                ObjectNode assistantHistory = mapper.createObjectNode();
                assistantHistory.put("role",    "assistant");
                assistantHistory.put(CONTENT, assistantContent);
                conversationHistory.add(assistantHistory);

                return assistantContent;
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro ao comunicar com o Hugging Face: " + e.getMessage(), e);
        }
    }

    // -----------------------------------------------------------------------
    // Parsing das respostas
    // -----------------------------------------------------------------------

    private static List<IPosition> parseShots(String content) {
        try {
            String jsonArray = extractJson(content, '[', ']');
            log.info("Rajada da IA: " + jsonArray);

            ArrayNode shotsNode = (ArrayNode) mapper.readTree(jsonArray);
            List<IPosition> shots = new ArrayList<>();

            for (JsonNode shotNode : shotsNode) {
                String row    = shotNode.get("row").asText().toUpperCase();
                int    column = shotNode.get("column").asInt();
                shots.add(new Position(row.charAt(0), column));
            }

            if (shots.size() != Game.NUMBER_SHOTS) {
                throw new RuntimeException("A IA devolveu " + shots.size()
                        + " tiros em vez de " + Game.NUMBER_SHOTS);
            }

            return shots;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao interpretar rajada da IA: " + e.getMessage(), e);
        }
    }

    private static String parseResult(String content) {
        try {
            // Encontrar início do JSON
            int start = content.indexOf('{');
            if (start == -1) {
                throw new RuntimeException("Sem JSON na resposta: " + content);
            }

            // Reconstruir JSON válido contando chavetas
            int depth = 0;
            int end = -1;
            for (int i = start; i < content.length(); i++) {
                if (content.charAt(i) == '{') depth++;
                if (content.charAt(i) == '}') depth--;
                if (depth == 0) { end = i + 1; break; }
            }

            String jsonObject;
            if (end == -1) {
                // JSON truncado — tentar completar
                log.info("⚠️ JSON truncado, a tentar completar...");
                String truncated = content.substring(start);
                // Fechar arrays abertos
                long openBrackets = 0;
                long closeBrackets = 0;
                for (char c : truncated.toCharArray()) {
                    if (c == '[') openBrackets++;
                    if (c == ']') closeBrackets++;
                }StringBuilder fixed = new StringBuilder(truncated);
                for (long i = 0; i < openBrackets - closeBrackets; i++) fixed.append("]");
                fixed.append("}");
                jsonObject = fixed.toString();
            } else {
                jsonObject = content.substring(start, end);
            }

            log.info("Resultado da frota da IA: " + jsonObject);

            // Validar JSON
            mapper.readTree(jsonObject);
            return jsonObject;

        } catch (Exception e) {
            // Fallback — assumir tiros na água
            log.info("⚠️ Erro ao interpretar resposta, assumindo tiros na água.");
            return "{\"validShots\":3,\"sunkBoats\":[],\"repeatedShots\":0," +
                    "\"outsideShots\":0,\"hitsOnBoats\":[],\"missedShots\":3}";
        }
    }

    private static String extractJson(String text, char open, char close) {
        int start = text.indexOf(open);
        int end   = text.lastIndexOf(close) + 1;

        if (start == -1 || end == 0) {
            throw new RuntimeException("Não foi encontrado JSON válido na resposta da IA:\n" + text);
        }

        return text.substring(start, end);
    }

    static String getEnvToken() {
        return System.getenv("HF_TOKEN");
    }

    private static String loadToken() {
        String envToken = getEnvToken();
        if (envToken != null) return envToken;

        try {
            java.util.Properties props = new java.util.Properties();
            props.load(new java.io.FileReader("config.properties"));
            return props.getProperty("HF_TOKEN");
        } catch (Exception e) {
            return null;
        }
    }
}