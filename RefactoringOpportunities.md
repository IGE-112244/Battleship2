# Refactoring Opportunities — BattleShip2
## Identificado com Qodana + SonarQube Cloud
### Curso: LIGE 2025/2026

| # | Local (Classe::Método) | Nome do Cheiro no Código | Nome da Refabricação | Nº Alun@ |
|---|------------------------|--------------------------|----------------------|-----------|
| 1 | `HuggingFaceClient` L18 | Lazy Class | Add Private Constructor | 112244 |
| 2 | `GameStatsRepository` L14 | Lazy Class | Add Private Constructor | 112244 |
| 3 | `GameStatsPanel` L14 | Lazy Class | Add Private Constructor | 112244 |
| 4 | `GameJsonExporter` L17 | Lazy Class | Add Private Constructor | 122989 |
| 5 | `GamePdfExporter` L17 | Lazy Class | Add Private Constructor | 122989 |
| 6 | `Tasks` L20 | Lazy Class | Add Private Constructor | 123022 |
| 7 | `BoardVisualizer` L19 | Lazy Class | Add Private Constructor | 123022 |
| 8 | `HuggingFaceClient` L97 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 9 | `HuggingFaceClient` L222 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 10 | `HuggingFaceClient` L223 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 11 | `HuggingFaceClient` L224 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 12 | `HuggingFaceClient` L248 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 13 | `HuggingFaceClient` L249 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 14 | `HuggingFaceClient` L250 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 15 | `HuggingFaceClient` L284 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 16 | `HuggingFaceClient` L327 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 17 | `HuggingFaceClient` L343 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 18 | `HuggingFaceClient` L351 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 19 | `GameStatsRepository` L36 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 20 | `GameStatsRepository` L50 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 21 | `GameStatsRepository` L59 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 22 | `Main` L22 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 112244 |
| 23 | `Game` L58 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 24 | `Game` L59 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 25 | `Game` L61 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 26 | `Game` L63 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 27 | `Game` L65 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 28 | `Game` L67 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 29 | `Game` L69 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 30 | `Game` L74 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 31 | `Game` L76 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 32 | `Game` L77 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 33 | `Game` L80 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 34 | `Game` L82 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 35 | `Game` L229 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 36 | `Game` L249 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 37 | `Game` L439 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 38 | `Game` L481 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 39 | `Move` L154 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 40 | `Move` L199 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 41 | `Move` L200 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 42 | `Tasks` L59 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 43 | `Tasks` L99 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 44 | `Tasks` L102 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 45 | `Tasks` L155 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 46 | `Tasks` L157 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 47 | `Tasks` L183 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 48 | `Tasks` L321 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 49 | `Tasks` L333 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 50 | `Tasks` L351 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 51 | `Tasks` L365 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 52 | `Tasks` L380 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 53 | `Tasks` L391 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 122989 |
| 54 | `BoardVisualizer` L35 | Comments (uso de System.err em vez de Logger) | Replace System.out with Logger | 123022 |
| 55 | `BoardVisualizer` L96 | Comments (uso de System.err em vez de Logger) | Replace System.out with Logger | 123022 |
| 56 | `BoardVisualizer` L215 | Comments (uso de System.err em vez de Logger) | Replace System.out with Logger | 123022 |
| 57 | `GameJsonExporter` L59 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 123022 |
| 58 | `GamePdfExporter` L59 | Comments (uso de System.out em vez de Logger) | Replace System.out with Logger | 123022 |
| 59 | `Tasks` — `"historico_partida_"` (7x) | Duplicated Code | Extract Constant | 112244 |
| 60 | `Tasks` — `".json"` (4x) | Duplicated Code | Extract Constant | 112244 |
| 61 | `Tasks` — `"Histórico exportado para: "` (3x) | Duplicated Code | Extract Constant | 112244 |
| 62 | `HuggingFaceClient` — `"content"` (5x) | Duplicated Code | Extract Constant | 112244 |
| 63 | `Fleet` — `"caravela"` (3x) | Duplicated Code | Extract Constant | 122989 |
| 64 | `Fleet` — `"barca"` (4x) | Duplicated Code | Extract Constant | 122989 |
| 65 | `Fleet` — `"fragata"`, `"galeao"`, `"nau"` | Duplicated Code | Extract Constant | 122989 |
| 66 | `Move` — `" tiro"` (6x) | Duplicated Code | Extract Constant | 122989 |
| 67 | `GameStatsPanel` — `"SansSerif"` (3x) | Duplicated Code | Extract Constant | 123022 |
| 68 | `GameJsonExporter` — `"sunkNames"` (3x) | Duplicated Code | Extract Constant | 123022 |
| 69 | `GamePdfExporter` — `"sunkNames"` (3x) | Duplicated Code | Extract Constant | 123022 |
| 70 | `Tasks` — `".pdf"` (4x) | Duplicated Code | Extract Constant | 123022 |
| 71 | `HuggingFaceClient::parseResult()` L307 | Long Method (CC=12, LOC=36) | Extract Method | 112244 |
| 72 | `HuggingFaceClient::callAPI()` L191 | Long Method (LOC=44) | Extract Method | 112244 |
| 73 | `HuggingFaceClient::buildAttackMessage()` | Long Method | Extract Method | 112244 |
| 74 | `HuggingFaceClient` L236 — extrair body | Long Method | Extract Method | 112244 |
| 75 | `HuggingFaceClient` L328 — extrair fixed | Long Method | Extract Method | 112244 |
| 76 | `GameStatsPanel::mostrar()` L22 (LOC=49) | Long Method | Extract Method | 112244 |
| 77 | `GameStatsPanel` L88 — extrair bottomPanel | Long Method | Extract Method | 112244 |
| 78 | `Move::processEnemyFire()` L66 (CC=37, LOC=77) | Long Method | Extract Method | 122989 |
| 79 | `Move::buildResultText()` | Long Method | Extract Method | 122989 |
| 80 | `Game::printBoard()` L26 (CC=20, LOC=47) | Long Method | Extract Method | 122989 |
| 81 | `Game::randomEnemyFire()` L211 (6 loops) | Long Method | Extract Method | 122989 |
| 82 | `Game::readEnemyFireFromJson()` L452 | Long Method | Extract Method | 122989 |
| 83 | `Game::fireSingleShot()` L359 | Long Method | Extract Method | 122989 |
| 84 | `Ship::getAdjacentPositions()` (2 loops) | Long Method | Extract Method | 122989 |
| 85 | `Galleon::fillSouth()` (2 loops) | Long Method | Extract Method | 122989 |
| 86 | `Carrack` (4 loops) | Long Method | Extract Method | 122989 |
| 87 | `GameJsonExporter::buildResultText()` L101 (CC=18) | Long Method | Extract Method | 123022 |
| 88 | `GameJsonExporter::buildMovesList()` L69 | Long Method | Extract Method | 123022 |
| 89 | `GameJsonExporter` L39 — extrair resumo | Long Method | Extract Method | 123022 |
| 90 | `GamePdfExporter::buildResultText()` L233 (CC=18) | Long Method | Extract Method | 123022 |
| 91 | `GamePdfExporter::addMovesTable()` L133 (LOC=34) | Long Method | Extract Method | 123022 |
| 92 | `BoardVisualizer::atualizar()` L47 (LOC=32) | Long Method | Extract Method | 123022 |
| 93 | `BoardVisualizer::buildMyMap()` L114 (4 loops) | Long Method | Extract Method | 123022 |
| 94 | `BoardVisualizer::drawBoard()` L175 (2 loops) | Long Method | Extract Method | 123022 |
| 95 | `BoardVisualizer::initMap()` L163 (2 loops) | Long Method | Extract Method | 123022 |
| 96 | `BoardVisualizer::buildAiMap()` L138 (2 loops) | Long Method | Extract Method | 123022 |
| 97 | `Tasks::menu()` L118 — extrair simulationStep | Long Method | Extract Method | 123022 |
| 98 | `Frigate` (4 loops) | Long Method | Extract Method | 123022 |
| 99 | `Caravel` (4 loops) | Long Method | Extract Method | 123022 |
| 100 | `Fleet` L90 — `assert s != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 112244 |
| 101 | `Fleet` L115 — `assert s != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 112244 |
| 102 | `Fleet` L181 — `assert category != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 112244 |
| 103 | `Fleet` L228 — `assert pos != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 112244 |
| 104 | `Fleet` L257 — `assert ships != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 112244 |
| 105 | `Game` L28 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 106 | `Game` L29 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 107 | `Game` L107 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 108 | `Game` L275 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 109 | `Game` L325 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 110 | `Game` L361 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 111 | `Game` L420 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 122989 |
| 112 | `Ship` L326 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 113 | `Ship` L348 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 114 | `Ship` L372 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 115 | `Ship` L394 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 116 | `Ship` L395 — `assert` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 117 | `Tasks` L405 — `assert in != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 118 | `Tasks` L433 — `assert in != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 119 | `Tasks` L450 — `assert in != null` | Inappropriate Intimacy (assert como validação) | Replace Assert with If/Throw | 123022 |
| 120 | `Ship` L228 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 121 | `Ship` L248 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 122 | `Ship` L268 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 123 | `Ship` L288 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 124 | `Ship` L308 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 125 | `Ship` L353 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 126 | `Ship` L376 | Comments (indentação enganosa) | Add Braces to if Statement | 123022 |
| 127 | `Game` L123 — `jsonString = null` redundante | Speculative Generality | Remove Redundant Initialization | 122989 |
| 128 | `Ship` L116 — `category` useless assignment | Speculative Generality | Remove Useless Assignment | 123022 |
| 129 | `Ship` L117 — `bearing` useless assignment | Speculative Generality | Remove Useless Assignment | 123022 |
| 130 | `Ship` L118 — `pos` useless assignment | Speculative Generality | Remove Useless Assignment | 123022 |
| 131 | `Tasks` L335 — `part1` useless assignment | Speculative Generality | Remove Useless Assignment | 112244 |
| 132 | `HuggingFaceClient` L35 — `aiFleetJson` não usado | Speculative Generality | Remove Unused Field | 112244 |
| 133 | `Tasks` L45 — `IAJOGO` não usado | Speculative Generality | Remove Unused Field | 112244 |
| 134 | `GamePdfExporter` L33 — `FONT_TABLE_BODY` não usado | Speculative Generality | Remove Unused Field | 123022 |
| 135 | `Tasks` L11 — `import JsonNode` | Speculative Generality | Remove Unused Import | 112244 |
| 136 | `Tasks` L12 — `import ObjectMapper` | Speculative Generality | Remove Unused Import | 112244 |
| 137 | `GameStats` L7 — `import JsonIgnoreProperties` | Speculative Generality | Remove Unused Import | 112244 |
| 138 | `HuggingFaceClientTest` L28 — `import File` | Speculative Generality | Remove Unused Import | 112244 |
| 139 | `HuggingFaceClientTest` L29 — `import FileReader` | Speculative Generality | Remove Unused Import | 112244 |
| 140 | `HuggingFaceClientTest` L30 — `import FileWriter` | Speculative Generality | Remove Unused Import | 112244 |
| 141 | `HuggingFaceClientTest` L35 — `import Properties` | Speculative Generality | Remove Unused Import | 112244 |
| 142 | `Carrack` L3 — `import List` | Speculative Generality | Remove Unused Import | 122989 |
| 143 | `IGameTest` L3 — `import Assertions` | Speculative Generality | Remove Unused Import | 122989 |
| 144 | `Game` L166 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 145 | `Game` L167 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 146 | `Game` L216 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 147 | `Game` L227 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 148 | `Game` L327 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 149 | `Game` L332 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 150 | `Fleet` L138 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 151 | `Fleet` L159 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 122989 |
| 152 | `Position` L137 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 123022 |
| 153 | `Ship` L159 | Primitive Obsession (Raw Type) | Replace with Diamond Operator | 123022 |
| 154 | `GameJsonExporter` L115 — `sunkNames.length() > 0` | Speculative Generality | Replace with isEmpty() | 112244 |
| 155 | `GameJsonExporter` L126 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 112244 |
| 156 | `GameJsonExporter` L127 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 112244 |
| 157 | `GameJsonExporter` L128 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 112244 |
| 158 | `GameJsonExporter` L129 — `sb.length() == 0` | Speculative Generality | Replace with isEmpty() | 112244 |
| 159 | `GamePdfExporter` L246 — `sunkNames.length() > 0` | Speculative Generality | Replace with isEmpty() | 123022 |
| 160 | `GamePdfExporter` L257 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 123022 |
| 161 | `GamePdfExporter` L258 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 123022 |
| 162 | `GamePdfExporter` L259 — `sb.length() > 0` | Speculative Generality | Replace with isEmpty() | 123022 |
| 163 | `GamePdfExporter` L260 — `sb.length() == 0` | Speculative Generality | Replace with isEmpty() | 123022 |
| 164 | `Game` L191 — `getAlienFleet` idêntico a `getMyFleet` | Duplicated Code | Inline Method | 122989 |
| 165 | `GameStatsRepositoryTest` L31 — setUp duplicado | Duplicated Code | Remove Duplicated Code | 112244 |
| 166 | `Fleet` L240 — código comentado (7 linhas) | Comments | Remove Commented Code | 122989 |
| 167 | `Position` L240 — código comentado | Comments | Remove Commented Code | 123022 |
| 168 | `Game` L131 — código comentado | Comments | Remove Commented Code | 122989 |
| 169 | `Game` L340 — código comentado | Comments | Remove Commented Code | 122989 |
| 170 | `PositionTest` L39 — código comentado | Comments | Remove Commented Code | 112244 |
| 171 | `PositionTest` L189 — código comentado | Comments | Remove Commented Code | 112244 |
| 172 | `HuggingFaceClientTest` L828 — código comentado | Comments | Remove Commented Code | 112244 |
| 173 | `Position` L37 — `Math.random()` em vez de `Random.nextInt()` | Primitive Obsession | Replace with Random.nextInt() | 123022 |
| 174 | `Position` L38 — `Math.random()` em vez de `Random.nextInt()` | Primitive Obsession | Replace with Random.nextInt() | 123022 |
| 175 | `Compass` L34 — `Math.random()` em vez de `Random.nextInt()` | Primitive Obsession | Replace with Random.nextInt() | 122989 |
| 176 | `Position` L214 — instanceof + cast | Speculative Generality | Replace with Pattern Matching instanceof | 123022 |
| 177 | `Tasks` L120 — classe anónima Runnable | Speculative Generality | Replace Anonymous Class with Lambda | 112244 |
| 178 | `TasksTest` L48 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 179 | `TasksTest` L56 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 180 | `TasksTest` L64 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 181 | `TasksTest` L72 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 182 | `TasksTest` L80 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 183 | `TasksTest` L88 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 184 | `TasksTest` L96 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 185 | `TasksTest` L104 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 186 | `TasksTest` L112 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 187 | `TasksTest` L120 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 188 | `TasksTest` L128 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 189 | `TasksTest` L136 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 190 | `TasksTest` L144 — `() -> Tasks.menu()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 191 | `TasksTest` L155 — `() -> Tasks.menuHelp()` | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 192 | `GalleonTest` L82 — lambda | Speculative Generality | Replace Lambda with Method Reference | 122989 |
| 193 | `CarrackTest` L138 — lambda | Speculative Generality | Replace Lambda with Method Reference | 122989 |
| 194 | `HuggingFaceClientTest` L276 — lambda | Speculative Generality | Replace Lambda with Method Reference | 112244 |
| 195 | `Tasks::menu()` L181 — case sem break | Switch Statements | Add Unconditional Break (BLOCKER) | 123022 |
| 196 | `Game` L128 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 122989 |
| 197 | `Game` L471 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 122989 |
| 198 | `HuggingFaceClient` L238 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 199 | `HuggingFaceClient` L243 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 200 | `HuggingFaceClient` L273 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 201 | `HuggingFaceClient` L296 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 202 | `HuggingFaceClient` L303 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 203 | `HuggingFaceClient` L312 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 204 | `HuggingFaceClient` L362 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 112244 |
| 205 | `Move` L196 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 122989 |
| 206 | `GameJsonExporter` L61 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 123022 |
| 207 | `GamePdfExporter` L62 — RuntimeException genérica | Inappropriate Intimacy | Replace with Specific Exception | 123022 |
| 208 | `GameStatsPanel` L31 — acesso não-estático a WindowConstants | Inappropriate Intimacy | Use Static Access for WindowConstants | 123022 |
| 209 | `Tasks` L409 — acesso não-estático a FLEET_SIZE | Inappropriate Intimacy | Use Static Access for IFleet.FLEET_SIZE | 123022 |
| 210 | `Ship` L110 — construtor público em classe com subclasses | Refused Bequest | Change Constructor Visibility to Protected | 123022 |
| 211 | `Game::fireSingleShot()` L359 — 4 negações | Long Method | Decompose Conditional | 122989 |
| 212 | `Move::processEnemyFire()` L66 — 8 negações | Long Method | Decompose Conditional | 122989 |
| 213 | `Tasks::menu()` L53 — 16 negações | Long Method | Decompose Conditional | 122989 |
| 214 | `Tasks` L116 — `ScheduledExecutorService` sem try-with-resources | Incomplete Library Class | Wrap in Try-with-resources | 112244 |
| 215 | `Caravel` L28 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 123022 |
| 216 | `Caravel` L36 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 123022 |
| 217 | `Carrack` L30 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 122989 |
| 218 | `Carrack` L38 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 122989 |
| 219 | `Frigate` L28 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 122989 |
| 220 | `Frigate` L36 — switch com duplicate branches | Switch Statements | Consolidate Duplicate Conditional Fragments | 122989 |
| 221 | `HuggingFaceClient` L20 — `HF_TOKEN` naming incorreto | Comments (naming) | Rename Field to camelCase | 112244 |
| 222 | `Game` L26 — variável com naming incorreto | Comments (naming) | Rename Local Variable | 122989 |
| 223 | `Game` L428 — variável com naming incorreto | Comments (naming) | Rename Local Variable | 122989 |
| 224 | `IGame` L115 — variáveis com naming incorreto | Comments (naming) | Rename Local Variable | 122989 |
| 225 | `IGame` L120 — variáveis com naming incorreto | Comments (naming) | Rename Local Variable | 122989 |
| 226 | `Position` L139 — `row` esconde campo | Comments (naming) | Rename Local Variable | 123022 |
| 227 | `Tasks` L162 — cast desnecessário | Speculative Generality | Remove Unnecessary Cast | 112244 |
| 228 | `GamePdfExporter` L171 — `toString()` desnecessário | Speculative Generality | Remove Unnecessary toString() Call | 123022 |
| 229 | `GamePdfExporter` L70 — parâmetro `game` não usado | Speculative Generality | Remove Unused Method Parameter | 123022 |
| 230 | `BoardVisualizer` L175 — parâmetro `showShips` não usado | Speculative Generality | Remove Unused Method Parameter | 123022 |
| 231 | `GameStats` L3 — Javadoc solto | Comments | Fix Dangling Javadoc Comment | 112244 |
| 232 | `Ship` L95 — `size` pode ser final | Speculative Generality | Add Final Modifier to Field | 122989 |
| 233 | `HuggingFaceClient` L24 — `httpClient` pode ser final | Speculative Generality | Add Final Modifier to Field | 112244 |
| 234 | `Game` L221 — `set.removeAll(list)` lento | Incomplete Library Class | Substitute Algorithm (convert List to Set) | 122989 |
| 235 | `Game` L222 — `set.removeAll(list)` lento | Incomplete Library Class | Substitute Algorithm (convert List to Set) | 122989 |
| 236 | `GameStatsPanel` L98 — lambda demasiado longa | Long Method | Extract Method from Lambda | 123022 |
| 237 | `Fleet::shipAt()` L179 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 112244 |
| 238 | `GameStats::getAccuracy()` L49 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 112244 |
| 239 | `GameStatsRepository::load()` L30 — 3 return points | Long Method | Replace Nested Conditional with Guard Clauses | 112244 |
| 240 | `HuggingFaceClient::loadToken()` — 3 return points | Long Method | Replace Nested Conditional with Guard Clauses | 112244 |
| 241 | `Game::repeatedShot()` L418 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 122989 |
| 242 | `Game::fireSingleShot()` L359 — 4 return points | Long Method | Replace Nested Conditional with Guard Clauses | 122989 |
| 243 | `Fleet::colisionRisk()` L209 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 122989 |
| 244 | `GameJsonExporter::buildResultText()` — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 245 | `GameJsonExporter::buildMovesList()` — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 246 | `Ship::stillFloating()` L224 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 247 | `Ship::occupies()` L324 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 248 | `Ship::tooCloseTo()` L346 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 249 | `Ship::tooCloseTo()` L370 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 250 | `BoardVisualizer::atualizar()` L47 — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 251 | `Position::equals()` L210 — 3 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 252 | `GamePdfExporter::getShotColor()` L224 — 5 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 253 | `GamePdfExporter::addMovesTable()` — 2 return points | Long Method | Replace Nested Conditional with Guard Clauses | 123022 |
| 254 | `pom.xml` — `log4j-core` vulnerável (CVE-2026-34480) | Incomplete Library Class | Update Dependency Version | 112244 |
| 255 | `pom.xml` — `jackson-core` vulnerável (GHSA-72hv) | Incomplete Library Class | Update Dependency Version | 112244 |
| 256 | `pom.xml` — `commons-lang3` vulnerável (CVE-2025-48924) | Incomplete Library Class | Update Dependency Version | 112244 |
| 257 | `pom.xml` — `classgraph` vulnerável (CVE-2021-47621) | Incomplete Library Class | Update Dependency Version | 112244 |
| 258 | `PositionTest` L155 — `assertTrue(a.equals(b))` | Comments (test code smell) | Replace assertTrue with assertEquals | 112244 |
| 259 | `PositionTest` L177 — `assertTrue(a.equals(b))` | Comments (test code smell) | Replace assertTrue with assertEquals | 112244 |
| 260 | `PositionTest` L160 — `assertFalse(a.equals(b))` | Comments (test code smell) | Replace assertFalse with assertNotEquals | 112244 |
| 261 | `PositionTest` L166 — `assertFalse(a.equals(b))` | Comments (test code smell) | Replace assertFalse with assertNotEquals | 112244 |
| 262 | `PositionTest` L172 — `assertFalse(a.equals(b))` | Comments (test code smell) | Replace assertFalse with assertNotEquals | 112244 |
| 263 | `PositionTest` L288 — `assertFalse(a.equals(b))` | Comments (test code smell) | Replace assertFalse with assertNotEquals | 122989 |
| 264 | `HuggingFaceClientTest` L691 — `assertTrue(x != null)` | Comments (test code smell) | Replace assertTrue with assertNotNull | 112244 |
| 265 | `HuggingFaceClientTest` L716 — `assertTrue(x != null)` | Comments (test code smell) | Replace assertTrue with assertNotNull | 112244 |
| 266 | `Tasks::menu()` L53 — nesting depth = 6 | Long Method | Decompose Conditional / Extract Method | 122989 |
| 267 | `BoardVisualizer::drawBoard()` L114 — CC=18 | Large Class | Extract Method | 123022 |
| 268 | `BoardVisualizer::drawBoard()` L138 — CC=18 | Large Class | Extract Method | 123022 |
| 269 | `GamePdfExporter` L233 — CC=18 | Large Class | Extract Method | 123022 |
| 270 | `CaravelTest` L104 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 122989 |
| 271 | `CaravelTest` L114 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 122989 |
| 272 | `CarrackTest` L200 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 122989 |
| 273 | `ShipTest` L283 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 123022 |
| 274 | `ShipTest` L426 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 123022 |
| 275 | `ShipTest` L434 — lambda com múltiplas invocações | Comments (test code smell) | Refactor Lambda in assertThrows | 123022 |
| 276 | `HuggingFaceClientTest` L191 — 5 testes duplicados | Duplicated Code | Replace with Parameterized Test | 112244 |
| 277 | `GamePdfExporterTest` L72 — 3 testes duplicados | Duplicated Code | Replace with Parameterized Test | 123022 |
| 278 | `TasksTest` L318 — 3 testes duplicados | Duplicated Code | Replace with Parameterized Test | 112244 |
| 279 | `BargeTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 112244 |
| 280 | `FleetTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 112244 |
| 281 | `GameTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 122989 |
| 282 | `MainTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 112244 |
| 283 | `ShipTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 123022 |
| 284 | `PositionTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 123022 |
| 285 | `GalleonTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 122989 |
| 286 | `CompassTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 122989 |
| 287 | `FrigateTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 123022 |
| 288 | `CarrackTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 122989 |
| 289 | `GameJsonExporterTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 122989 |
| 290 | `GamePdfExporterTest` — `public` modifier desnecessário | Speculative Generality | Remove Public Modifier from Test Class | 123022 |
| 291 | `HuggingFaceClientTest` L66 — throws Exception desnecessário | Speculative Generality | Remove Unnecessary throws Declaration | 112244 |
| 292 | `GameStatsTest` L79 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 122989 |
| 293 | `GameStatsTest` L116 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 122989 |
| 294 | `GameStatsTest` L119 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 122989 |
| 295 | `GameStatsTest` L122 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 122989 |
| 296 | `MoveTest` L112 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 123022 |
| 297 | `MoveTest` L115 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 123022 |
| 298 | `MainTest` L61 — método vazio sem comentário | Comments | Add Comment or Implement Empty Test Method | 112244 |
| 299 | `IGameTest` — classe sem testes (BLOCKER) | Lazy Class | Add Tests to Empty Test Class (BLOCKER) | 122989 |
| 300 | `GameJsonExporter` L105 — declarações na mesma linha | Comments | Declare Variables on Separate Lines | 123022 |
| 301 | `GamePdfExporter` L236 — declarações na mesma linha | Comments | Declare Variables on Separate Lines | 123022 |
| 302 | `GameJsonExporter` L108 — múltiplos break/continue no loop | Long Method | Reduce Break/Continue Statements in Loop | 123022 |
| 303 | `GamePdfExporter` L239 — múltiplos break/continue no loop | Long Method | Reduce Break/Continue Statements in Loop | 123022 |
| 304 | `Tasks` L297 — múltiplos break/continue no loop | Long Method | Reduce Break/Continue Statements in Loop | 122989 |
| 305 | `GameJsonExporter` L126-L128 — if sem execução condicional | Comments | Fix Conditional Statement (Misleading if) | 123022 |
| 306 | `GamePdfExporter` L257-L259 — if sem execução condicional | Comments | Fix Conditional Statement (Misleading if) | 123022 |
