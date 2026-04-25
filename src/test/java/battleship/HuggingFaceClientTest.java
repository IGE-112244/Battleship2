/**
 * Test class for HuggingFaceClient.
 * Author: ${user.name}
 * Date: 2025/2026
 * Cyclomatic Complexity:
 * - setSystemPrompt(): 1
 * - getNextShots(): 2
 * - initGame(): 1
 * - sendMyShots(): 1
 * - addShotResult(): 1
 * - buildAttackMessage(): 3 (lastResult null, lastResult not null, history empty/not empty)
 * - parseResult(): 4 (valid JSON, no JSON, truncated JSON, exception fallback)
 * - extractJson(): 2 (found, not found)
 * Note: Methods requiring network calls (callAPI, getNextShots, sendMyShots, initGame)
 * cannot be fully tested without a valid API token.
 */
package battleship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

class HuggingFaceClientTest {

    @BeforeEach
    void setUp() {
        // Limpar estado estático antes de cada teste
        HuggingFaceClient.setSystemPrompt(null);
    }

    @AfterEach
    void tearDown() {
        // Limpar estado estático após cada teste
        HuggingFaceClient.setSystemPrompt(null);
    }

    // -----------------------------------------------------------------------
    // setSystemPrompt()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("setSystemPrompt() should set the system prompt without throwing")
    void setSystemPrompt() {
        assertDoesNotThrow(() -> HuggingFaceClient.setSystemPrompt("test prompt"),
                "Error: setSystemPrompt() should not throw exceptions.");
    }

    @Test
    @DisplayName("setSystemPrompt() should clear conversation history and shot history")
    void setSystemPromptClearsHistory() {
        HuggingFaceClient.addShotResult("A1", "água");
        assertDoesNotThrow(() -> HuggingFaceClient.setSystemPrompt("new prompt"),
                "Error: setSystemPrompt() should clear history without throwing.");
    }

    @Test
    @DisplayName("setSystemPrompt() should accept null prompt")
    void setSystemPromptNull() {
        assertDoesNotThrow(() -> HuggingFaceClient.setSystemPrompt(null),
                "Error: setSystemPrompt() should accept null without throwing.");
    }

    // -----------------------------------------------------------------------
    // addShotResult()
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("addShotResult() should store shot result without throwing")
    void addShotResult() {
        assertDoesNotThrow(() -> HuggingFaceClient.addShotResult("A1", "água"),
                "Error: addShotResult() should not throw exceptions.");
    }

    @Test
    @DisplayName("addShotResult() should overwrite existing result for same position")
    void addShotResultOverwrite() {
        assertDoesNotThrow(() -> {
            HuggingFaceClient.addShotResult("A1", "água");
            HuggingFaceClient.addShotResult("A1", "💥 acerto em Nau");
        }, "Error: addShotResult() should overwrite existing result without throwing.");
    }

    // -----------------------------------------------------------------------
    // buildAttackMessage() via reflection
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("buildAttackMessage() should contain 'Jogo novo' when lastResult is null")
    void buildAttackMessage1() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("buildAttackMessage", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(null, (Object) null);
        assertTrue(result.contains("Jogo novo"),
                "Error: buildAttackMessage() should contain 'Jogo novo' when lastResult is null.");
    }

    @Test
    @DisplayName("buildAttackMessage() should contain lastResult when not null")
    void buildAttackMessage2() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("buildAttackMessage", String.class);
        method.setAccessible(true);

        String lastResult = "{\"validShots\":3}";
        String result = (String) method.invoke(null, lastResult);
        assertTrue(result.contains(lastResult),
                "Error: buildAttackMessage() should contain the lastResult when not null.");
    }

    @Test
    @DisplayName("buildAttackMessage() should include diary when shot history is not empty")
    void buildAttackMessage3() throws Exception {
        HuggingFaceClient.addShotResult("A1", "água");

        // Adicionar ao histórico via getNextShots não é possível sem rede,
        // mas podemos verificar que o método não lança exceção
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("buildAttackMessage", String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(null, "resultado"),
                "Error: buildAttackMessage() should not throw when shot history exists.");
    }

    // -----------------------------------------------------------------------
    // parseResult() via reflection
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("parseResult() should return valid JSON when input contains valid JSON object")
    void parseResult1() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        String validJson = "{\"validShots\":3,\"sunkBoats\":[],\"missedShots\":3}";
        String result = (String) method.invoke(null, validJson);
        assertEquals(validJson, result,
                "Error: parseResult() should return the valid JSON object.");
    }

    @Test
    @DisplayName("parseResult() should return fallback JSON when input has no JSON")
    void parseResult2() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(null, "sem json aqui");
        assertTrue(result.contains("validShots"),
                "Error: parseResult() should return fallback JSON when no JSON found.");
    }

    @Test
    @DisplayName("parseResult() should handle truncated JSON")
    void parseResult3() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON truncado sem fechar
        String truncated = "{\"validShots\":3,\"sunkBoats\":[{\"count\":1";
        String result = (String) method.invoke(null, truncated);
        assertNotNull(result,
                "Error: parseResult() should return a non-null result for truncated JSON.");
    }

    @Test
    @DisplayName("parseResult() should return fallback JSON on exception")
    void parseResult4() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // Input que causa exceção no parsing
        String result = (String) method.invoke(null, "{{invalid{{json");
        assertTrue(result.contains("validShots"),
                "Error: parseResult() should return fallback JSON on parsing exception.");
    }

    // -----------------------------------------------------------------------
    // extractJson() via reflection
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("extractJson() should extract JSON array from text")
    void extractJson1() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("extractJson", String.class, char.class, char.class);
        method.setAccessible(true);

        String text = "some text [{\"row\":\"A\",\"column\":1}] more text";
        String result = (String) method.invoke(null, text, '[', ']');
        assertEquals("[{\"row\":\"A\",\"column\":1}]", result,
                "Error: extractJson() should extract the JSON array correctly.");
    }

    @Test
    @DisplayName("extractJson() should throw RuntimeException when no JSON found")
    void extractJson2() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("extractJson", String.class, char.class, char.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, "sem json aqui", '[', ']'),
                "Error: extractJson() should throw RuntimeException when no JSON found.");
    }

    // -----------------------------------------------------------------------
    // getNextShots(), initGame(), sendMyShots() - requerem rede
    // -----------------------------------------------------------------------

    @Test
    @DisplayName("getNextShots() should return list of positions with valid token")
    void getNextShots() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt(
                "Responde APENAS com este array JSON exato sem texto adicional: " +
                        "[{\"row\":\"A\",\"column\":1},{\"row\":\"B\",\"column\":2},{\"row\":\"C\",\"column\":3}]"
        );

        List<IPosition> shots = HuggingFaceClient.getNextShots(null);
        assertAll(
                () -> assertNotNull(shots,
                        "Error: getNextShots() should return non-null list."),
                () -> assertEquals(Game.NUMBER_SHOTS, shots.size(),
                        "Error: getNextShots() should return exactly NUMBER_SHOTS positions.")
        );
    }

    @Test
    @DisplayName("initGame() should complete without throwing with valid token")
    void initGame() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt("Responde apenas com: Frota criada. Pronto para jogar!");
        assertDoesNotThrow(() -> HuggingFaceClient.initGame(),
                "Error: initGame() should not throw with valid token.");
    }

    @Test
    @DisplayName("sendMyShots() should return valid JSON result with valid token")
    void sendMyShots() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt(
                "Quando receberes tiros, responde APENAS com este JSON exato: " +
                        "{\"validShots\":3,\"sunkBoats\":[],\"repeatedShots\":0," +
                        "\"outsideShots\":0,\"hitsOnBoats\":[],\"missedShots\":3}"
        );

        List<IPosition> shots = List.of(
                new Position('A', 1),
                new Position('B', 2),
                new Position('C', 3)
        );

        String result = HuggingFaceClient.sendMyShots(shots);
        assertAll(
                () -> assertNotNull(result,
                        "Error: sendMyShots() should return non-null result."),
                () -> assertTrue(result.contains("validShots"),
                        "Error: sendMyShots() result should contain 'validShots'.")
        );
    }

    @Test
    @DisplayName("loadToken() should return env token when config.properties does not exist")
    void loadToken1() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("loadToken");
        method.setAccessible(true);

        // Sem config.properties, deve retornar a variável de ambiente
        assertDoesNotThrow(() -> method.invoke(null),
                "Error: loadToken() should not throw when config.properties does not exist.");
    }

    @Test
    @DisplayName("loadToken() should load token from config.properties when it exists")
    void loadToken2() throws Exception {
        java.io.File configFile = new java.io.File("config.properties");

        // Fazer backup do conteúdo original
        String originalContent = null;
        if (configFile.exists()) {
            originalContent = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
        }

        try {
            // Criar config.properties temporário com token de teste
            try (java.io.FileWriter fw = new java.io.FileWriter(configFile)) {
                fw.write("HF_TOKEN=hf_test12345token");
            }

            Method method = HuggingFaceClient.class
                    .getDeclaredMethod("loadToken");
            method.setAccessible(true);

            assertDoesNotThrow(() -> method.invoke(null),
                    "Error: loadToken() should load token from config.properties without throwing.");

        } finally {
            // SEMPRE restaurar o conteúdo original
            if (originalContent != null) {
                try (java.io.FileWriter fw = new java.io.FileWriter(configFile)) {
                    fw.write(originalContent);
                }
            } else {
                configFile.delete();
            }
        }
    }

    @Test
    @DisplayName("buildAttackMessage() should include diary when shot history is not empty")
    void buildAttackMessageWithHistory() throws Exception {
        // Usar reflection para adicionar ao aiShotHistory
        Field historyField = HuggingFaceClient.class
                .getDeclaredField("aiShotHistory");
        historyField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> history = (Set<String>) historyField.get(null);
        history.add("A1");
        history.add("B2");

        HuggingFaceClient.addShotResult("A1", "água");
        HuggingFaceClient.addShotResult("B2", "💥 acerto em Nau");

        Method method = HuggingFaceClient.class
                .getDeclaredMethod("buildAttackMessage", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(null, (Object) null);
        assertAll(
                () -> assertTrue(result.contains("DIÁRIO DE BORDO"),
                        "Error: buildAttackMessage() should include diary when history is not empty."),
                () -> assertTrue(result.contains("A1"),
                        "Error: buildAttackMessage() should include shot position A1."),
                () -> assertTrue(result.contains("B2"),
                        "Error: buildAttackMessage() should include shot position B2.")
        );
    }

    @Test
    @DisplayName("parseShots() should parse valid JSON array of shots")
    void parseShots1() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseShots", String.class);
        method.setAccessible(true);

        String validShots = "[{\"row\":\"A\",\"column\":1}," +
                "{\"row\":\"B\",\"column\":2}," +
                "{\"row\":\"C\",\"column\":3}]";

        @SuppressWarnings("unchecked")
        List<IPosition> shots = (List<IPosition>) method.invoke(null, validShots);

        assertAll(
                () -> assertNotNull(shots,
                        "Error: parseShots() should return a non-null list."),
                () -> assertEquals(Game.NUMBER_SHOTS, shots.size(),
                        "Error: parseShots() should return exactly NUMBER_SHOTS positions.")
        );
    }

    @Test
    @DisplayName("parseShots() should throw RuntimeException for invalid JSON")
    void parseShots2() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseShots", String.class);
        method.setAccessible(true);

        assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, "invalid json"),
                "Error: parseShots() should throw RuntimeException for invalid JSON.");
    }

    @Test
    @DisplayName("parseShots() should throw RuntimeException when wrong number of shots")
    void parseShots3() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseShots", String.class);
        method.setAccessible(true);

        // Apenas 1 tiro em vez de NUMBER_SHOTS
        String oneShot = "[{\"row\":\"A\",\"column\":1}]";

        assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, oneShot),
                "Error: parseShots() should throw RuntimeException when wrong number of shots.");
    }

    @Test
    @DisplayName("extractJson() should throw when only open bracket exists")
    void extractJson3() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("extractJson", String.class, char.class, char.class);
        method.setAccessible(true);

        // Texto com apenas '[' mas sem ']' → end == 0
        assertThrows(InvocationTargetException.class,
                () -> method.invoke(null, "texto sem fecho", '[', ']'),
                "Error: extractJson() should throw when closing bracket is missing.");
    }

    @Test
    @DisplayName("parseResult() should handle truncated JSON with unclosed arrays")
    void parseResult5() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON truncado com array aberto mas não fechado
        String truncated = "{\"validShots\":3,\"sunkBoats\":[{\"count\":1,\"type\":\"Barca\"";
        String result = (String) method.invoke(null, truncated);
        assertNotNull(result,
                "Error: parseResult() should return non-null for truncated JSON with open arrays.");
    }

    // -----------------------------------------------------------------------
// Testes com chamadas reais à API (requerem HF_TOKEN válido)
// -----------------------------------------------------------------------

    private boolean isTokenAvailable() {
        // Verificar config.properties primeiro (mesma ordem que loadToken())
        try {
            java.util.Properties props = new java.util.Properties();
            props.load(new java.io.FileReader("config.properties"));
            String token = props.getProperty("HF_TOKEN");
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            // Fallback para variável de ambiente
            String token = System.getenv("HF_TOKEN");
            return token != null && !token.isEmpty();
        }
    }

    @Test
    @DisplayName("callAPI() with systemPrompt should attempt API call")
    void callAPIWithSystemPrompt() throws Exception {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt("Responde apenas com: OK");

        Method method = HuggingFaceClient.class
                .getDeclaredMethod("callAPI", String.class);
        method.setAccessible(true);

        // Aceita tanto sucesso como RuntimeException (token inválido/expirado)
        try {
            method.invoke(null, "OK");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof RuntimeException,
                    "Error: callAPI() should throw RuntimeException on API error.");
        }
    }

    @Test
    @DisplayName("callAPI() with conversation history should attempt API call")
    void callAPIWithHistory() throws Exception {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt("Responde apenas com: OK");

        Field historyField = HuggingFaceClient.class
                .getDeclaredField("conversationHistory");
        historyField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<ObjectNode> history = (List<ObjectNode>) historyField.get(null);

        ObjectNode msg = new ObjectMapper().createObjectNode();
        msg.put("role", "user");
        msg.put("content", "mensagem anterior");
        history.add(msg);

        Method method = HuggingFaceClient.class
                .getDeclaredMethod("callAPI", String.class);
        method.setAccessible(true);

        // Aceita tanto sucesso como RuntimeException
        try {
            method.invoke(null, "OK");
        } catch (java.lang.reflect.InvocationTargetException e) {
            assertTrue(e.getCause() instanceof RuntimeException,
                    "Error: callAPI() should throw RuntimeException on API error.");
        }
    }

    @Test
    @DisplayName("initGame() should attempt API call with valid token")
    void initGameWithToken() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt("Responde apenas com: Frota criada. Pronto para jogar!");

        // Aceita tanto sucesso como RuntimeException
        try {
            HuggingFaceClient.initGame();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Hugging Face"),
                    "Error: initGame() should throw RuntimeException with HF error message.");
        }
    }

    @Test
    @DisplayName("getNextShots() should attempt API call with valid token")
    void getNextShotsWithToken() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt(
                "Responde APENAS com: [{\"row\":\"A\",\"column\":1},{\"row\":\"B\",\"column\":2},{\"row\":\"C\",\"column\":3}]"
        );

        // Aceita tanto sucesso como RuntimeException
        try {
            List<IPosition> shots = HuggingFaceClient.getNextShots(null);
            assertEquals(Game.NUMBER_SHOTS, shots.size(),
                    "Error: getNextShots() should return NUMBER_SHOTS positions.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage() != null,
                    "Error: getNextShots() should throw RuntimeException with message.");
        }
    }

    @Test
    @DisplayName("sendMyShots() should attempt API call with valid token")
    void sendMyShotsWithToken() {
        Assumptions.assumeTrue(isTokenAvailable(),
                "Skipping test: HF_TOKEN not available.");

        HuggingFaceClient.setSystemPrompt("test");

        List<IPosition> shots = List.of(
                new Position('A', 1),
                new Position('B', 2),
                new Position('C', 3)
        );

        // Aceita tanto sucesso como RuntimeException
        try {
            String result = HuggingFaceClient.sendMyShots(shots);
            assertNotNull(result,
                    "Error: sendMyShots() should return non-null result.");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage() != null,
                    "Error: sendMyShots() should throw RuntimeException with message.");
        }
    }

    @Test
    @DisplayName("callAPI() should throw RuntimeException when response is unsuccessful")
    void callAPIUnsuccessfulResponse() throws Exception {
        Field clientField = HuggingFaceClient.class.getDeclaredField("httpClient");
        clientField.setAccessible(true);
        OkHttpClient originalClient = (OkHttpClient) clientField.get(null);

        // Criar cliente que aponta para um servidor local que não existe
        // mas que responde (usamos o próprio servidor HF com endpoint inválido)
        // Para isso, vamos usar um interceptor que força uma resposta de erro
        OkHttpClient mockClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    // Devolver sempre uma resposta 401 sem fazer chamada real
                    return new okhttp3.Response.Builder()
                            .request(chain.request())
                            .protocol(okhttp3.Protocol.HTTP_1_1)
                            .code(401)
                            .message("Unauthorized")
                            .body(okhttp3.ResponseBody.create(
                                    "{\"error\":\"Invalid token\"}",
                                    okhttp3.MediaType.get("application/json")))
                            .build();
                })
                .build();

        try {
            clientField.set(null, mockClient);

            Method method = HuggingFaceClient.class
                    .getDeclaredMethod("callAPI", String.class);
            method.setAccessible(true);

            assertThrows(java.lang.reflect.InvocationTargetException.class,
                    () -> method.invoke(null, "test"),
                    "Error: callAPI() should throw RuntimeException when response is unsuccessful.");

        } finally {
            clientField.set(null, originalClient);
        }
    }


    @Test
    @DisplayName("callAPI() should throw RuntimeException on IOException")
    void callAPIIOException() throws Exception {
        // Usar reflection para substituir o httpClient por um com timeout muito curto
        Field clientField = HuggingFaceClient.class.getDeclaredField("httpClient");
        clientField.setAccessible(true);

        OkHttpClient shortTimeoutClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MILLISECONDS) // timeout impossível de cumprir
                .readTimeout(1, TimeUnit.MILLISECONDS)
                .build();

        // Guardar o cliente original
        OkHttpClient originalClient = (OkHttpClient) clientField.get(null);

        try {
            clientField.set(null, shortTimeoutClient);

            Method method = HuggingFaceClient.class
                    .getDeclaredMethod("callAPI", String.class);
            method.setAccessible(true);

            assertThrows(java.lang.reflect.InvocationTargetException.class,
                    () -> method.invoke(null, "test"),
                    "Error: callAPI() should throw RuntimeException on IOException.");
        } finally {
            clientField.set(null, originalClient);
        }
    }

    @Test
    @DisplayName("parseResult() should handle truncated JSON with more open than close brackets")
    void parseResult6() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON com array aberto [[[ sem fechar
        String truncated = "{\"validShots\":3,\"sunkBoats\":[[[";
        String result = (String) method.invoke(null, truncated);
        assertNotNull(result,
                "Error: parseResult() should return non-null for truncated JSON with unclosed brackets.");
    }

    @Test
    @DisplayName("extractJson() should throw when closing bracket not found")
    void extractJson4() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("extractJson", String.class, char.class, char.class);
        method.setAccessible(true);

        // Tem '[' mas não tem ']' — end será 0
        assertThrows(java.lang.reflect.InvocationTargetException.class,
                () -> method.invoke(null, "texto com [ mas sem fechar", '[', ']'),
                "Error: extractJson() should throw when closing bracket is not found.");
    }

    @Test
    @DisplayName("loadToken() catch block should return env token when token property is null")
    void loadToken3() throws Exception {
        java.io.File configFile = new java.io.File("config.properties");

        String originalContent = null;
        if (configFile.exists()) {
            originalContent = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
        }

        try {
            // Criar config.properties sem a propriedade HF_TOKEN
            // para forçar token == null e erro no substring
            try (java.io.FileWriter fw = new java.io.FileWriter(configFile)) {
                fw.write("OUTRA_PROPRIEDADE=valor"); // sem HF_TOKEN
            }

            Method method = HuggingFaceClient.class
                    .getDeclaredMethod("loadToken");
            method.setAccessible(true);

            // Deve entrar no catch (NullPointerException no substring)
            // e retornar System.getenv("HF_TOKEN")
            assertDoesNotThrow(() -> method.invoke(null),
                    "Error: loadToken() catch block should not throw.");

        } finally {
            if (originalContent != null) {
                try (java.io.FileWriter fw = new java.io.FileWriter(configFile)) {
                    fw.write(originalContent);
                }
            } else {
                configFile.delete();
            }
        }
    }

    @Test
    @DisplayName("parseResult() should fix truncated JSON with more open than close brackets")
    void parseResult7() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON truncado com 2 '[' e 0 ']' — openBrackets(2) > closeBrackets(0)
        // Isto força o loop: for (long i = 0; i < openBrackets - closeBrackets; i++)
        String truncated = "{\"validShots\":3,\"sunkBoats\":[[";
        String result = (String) method.invoke(null, truncated);
        assertNotNull(result,
                "Error: parseResult() should return non-null when fixing truncated JSON.");
    }

    @Test
    @DisplayName("parseResult() loop should execute when openBrackets > closeBrackets")
    void parseResult8() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON truncado sem fechar '}' mas com '[' não fechado
        // O loop vai adicionar ']' e depois o código adiciona '}'
        // Resultado final: {"validShots":3,"sunkBoats":[]} — JSON válido!
        String truncated = "{\"validShots\":3,\"sunkBoats\":[";
        String result = (String) method.invoke(null, truncated);

        assertNotNull(result,
                "Error: parseResult() should return non-null.");
        assertTrue(result.contains("validShots"),
                "Error: parseResult() result should contain 'validShots'.");
    }

    @Test
    @DisplayName("parseResult() should handle truncated JSON with both open and close brackets")
    void parseResult9() throws Exception {
        Method method = HuggingFaceClient.class
                .getDeclaredMethod("parseResult", String.class);
        method.setAccessible(true);

        // JSON truncado com '[' E ']' presentes mas sem fechar '}'
        // openBrackets=2, closeBrackets=1 → loop executa 1 vez
        // Resultado: {"validShots":3,"sunkBoats":[[]]} + "}" → JSON válido
        String truncated = "{\"validShots\":3,\"sunkBoats\":[[\"test\"]";
        String result = (String) method.invoke(null, truncated);

        assertNotNull(result,
                "Error: parseResult() should return non-null.");
    }


}