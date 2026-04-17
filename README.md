## Vídeo de Demonstração
https://youtu.be/-CgKMK2VNCU

# ⚓ Battleship 2.0

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java Version](https://img.shields.io/badge/Java-17%2B-blue)
![License](https://img.shields.io/badge/license-MIT-green)

> A modern take on the classic naval warfare game, designed for the XVII century setting with updated software engineering patterns.

---
## Grupo: TP06-5

### Curso
Informática e Gestão de Empresas

## Membros
| Número  | Nome              |
|---------|-------------------|
| 112244  | Fábio Reis        |
| 122989  | Carolina Lisboa   |
| 123022  | Rita Peixoto      |

NOTA: O repositório foi criado pelo aluno com o número (112244) para que pudessemos dar seguimento ao trabalho em regime PL, uma vez que o regime de diurno não alcançou a parte B durante a sua aula.

## Respostas à Ficha 2:

**B.**

2. O Maven descobre as dependências transitivas através de um processo recursivo de leitura de ficheiros pom.xml das dependências diretas, ou seja, quando declaramos uma dependência direta no  pom.xml, o Maven vai ao repositório buscar não só o .jar dessa biblioteca, mas também o seu pom.xml. Esse ficheiro declara por sua vez as dependências daquela biblioteca (que para o nosso projeto são transitivas). O Maven repete este processo para cada nova dependência encontrada, até esgotar toda a árvore, gerindo automaticamente duplicados que possam surgir por caminhos diferentes.
   
As compilações seguintes são mais rápidas porque, na primeira compilação, o Maven descarrega todas as dependências (diretas e transitivas) do repositório central remoto para o repositório local, o que envolve transferências, naturalmente lentas. Nas compilações seguintes, o Maven verifica primeiro o repositório local e, como as dependências já lá estão, não precisa de as voltar a descarregar, acedendo diretamente às cópias locais, sendo o acesso ao disco substancialmente mais rápido do que as transferências.

A única exceção são dependências marcadas como SNAPSHOT, que o Maven verifica periodicamente no repositório remoto (por omissão, uma vez por dia) para garantir que a versão de desenvolvimento mais recente é sempre utilizada.

**D.**

2. PROMPT DA ESTRATÉGIA (usámos este prompt após termos ensinado o Gemini com as prompts do enunciado):
  
Vamos agora afinar a tua estratégia de jogo com regras adicionais  para jogares como um humano experiente.

FASE 1 - ABERTURA (primeiras 5 rajadas):
- Começa com um padrão em xadrez (casas alternadas), por exemplo:
  A1, A3, A5, B2, B4, C1, C3...
- Este padrão garante que não desperdiças tiros em posições adjacentes a água confirmada, já que nenhum navio pode estar nessas posições.
- Nunca dispares duas rajadas seguidas na mesma zona do tabuleiro.
- Divide o tabuleiro em 4 quadrantes (A-E / 1-5, A-E / 6-10, F-J / 1-5, F-J / 6-10) e distribui os tiros pelos quadrantes.

FASE 2 - CAÇA (quando há um acerto sem afundar):
- Após um acerto, na rajada seguinte dispara nas 4 posições ortogonais (Norte, Sul, Este, Oeste) do acerto.
- Se confirmares a direção do navio (dois acertos em linha), continua nessa direção até o afundar.
- Nunca dispares nas diagonais de um acerto — são sempre água (exceto no corpo do Galeão).

FASE 3 - ELIMINAÇÃO (após afundar um navio):
- Quando afundares um navio, marca todas as posições adjacentes (incluindo diagonais) como água intransitável no teu Diário de Bordo.
- Retoma o padrão em xadrez nas zonas ainda não exploradas.

REGRAS DE MEMÓRIA OBRIGATÓRIAS:
- Mantém sempre o teu Diário de Bordo atualizado com:
  * Tiros na água (○)
  * Acertos em navios (✕)
  * Navios afundados e as suas posições exatas
  * Zonas de exclusão (adjacentes a navios afundados)
- Nunca repitas um tiro em posição já tentada.
- Nunca dispares em posições de exclusão.

PRIORIDADE DE ALVOS:
1. Continuar a caçar um navio já atingido mas não afundado
2. Explorar zonas do tabuleiro ainda não testadas
3. Preferir zonas onde ainda cabem navios não encontrados (ex: se só falta o Galeão de 5, não dispares em zonas com menos de 5 posições livres consecutivas)

Confirmas que entendeste estas regras adicionais? 
Mostra-me o teu Diário de Bordo atual e diz-me qual seria a tua próxima rajada com base nesta estratégia melhorada.





3. Para funcionar é necessário criar uma Variável de Ambiente de nome HF_TOKEN = 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

Importa realçar que devido às limitações da API de IA gratuita o prompt tem de ser mais curto do que o necessário, impossibilitando a IA de atingir todo o seu potencial criando o jogo mais interativo e podendo levar a possíveis erros como jogadas repetidas ou fora do tabuleiro (raramente). Como é necessário informar a IA das jogadas que já e se foi ou não um tiro certeiro e o tipo de barco, quanto mais jogadas são feitas maior é o tamanho da prompt enviada, levando a que, eventualmente, possa aparecer o seguinte erro:

Exception in thread "main" java.lang.RuntimeException: Erro na API do Hugging Face: 400 - 
Detalhes: {"message":"Please reduce the length of the messages or completion. Current length is 8677 while limit is 8192","type":"invalid_request_error","param":"messages","code":"context_length_exceeded","id":""}
	at battleship.HuggingFaceClient.callAPI(HuggingFaceClient.java:240)
	at battleship.HuggingFaceClient.getNextShots(HuggingFaceClient.java:71)
	at battleship.Tasks.menu(Tasks.java:217)
	at battleship.Main.main(Main.java:24)

## Respostas à Ficha 3

**Tarefa 1. **

B)

2. Análise dos resultados da inspeção de código:

**Security**
Foram detetados 4 warnings e 1 weak warning:

Link with unencrypted protocol, o ficheiro jqueryUI.md usa HTTP em vez de HTTPS
Vulnerable declared dependency, o pom.xml declara dependências com vulnerabilidades conhecidas, nomeadamente o log4j-core:2.20.0 (CVE associada à Log4Shell), com dependências transitivas vulneráveis adicionais como jackson-core e commons

**Java**
Foram detetados 125 warnings e 10 weak warnings, distribuídos por 11 subcategorias. As mais relevantes são:

Java language level migration aids (40),  código que pode ser modernizado para versões mais recentes do Java
Declaration redundancy (35), variáveis, métodos ou imports declarados mas não utilizados
Probable bugs (14), código com potencial para erros em runtime, merece atenção prioritária
Code style issues (12), violações de convenções de escrita
Restantes categorias com menor expressão: Javadoc (7), Imports (6), Test frameworks (5), Code maturity (3), Performance (2), e Threading issues (1)

Em suma, as categorias mais críticas são as dependências vulneráveis em Security e os Probable bugs em Java, por terem maior impacto potencial na fiabilidade e segurança do sistema.


3. Através do Qodana, verificamos ainda, entre outras, as seguintes inconsistências:

**Security**: 4 problemas no pom.xml
A análise Qodana identificou vulnerabilidades mais detalhadas que a inspeção manual:

log4j-core:2.20.0 — CVE-2025-68161, severidade 5.4 (Insufficient Information) — dependência direta vulnerável
jackson-core:2.15.2 — GHSA-72hv-8253-57qq, severidade 7.5 (Insufficient Information) — dependência transitiva
classgraph:4.6.18 — CVE-2021-47621, severidade 7.5 (Improper Restriction of XML External Entity Reference — XXE) — dependência transitiva particularmente grave pois ataques XXE permitem leitura de ficheiros internos do servidor
commons-lang3:3.15.0 — CVE-2025-48924, severidade 5.3 — dependência transitiva

**Threading issues**: 1 problema
Detetado um problema de concorrência no código, que merece atenção especial pois erros de threading são difíceis de reproduzir e depurar.

O Qodana fornece uma análise mais detalhada que a inspeção manual, identificando os CVEs e severidades exatas de cada vulnerabilidade. A mais grave é a do classgraph (XXE, 7.5) por ser explorável remotamente. A solução passa por atualizar estas dependências para versões sem vulnerabilidades conhecidas no pom.xml.



4. O ficheiro de configuração do Qodana tem os seguintes elementos relevantes:
5. 
Perfil de inspeção:

yamlprofile:
  name: qodana.starter
Usa o perfil qodana.starter, o perfil básico/inicial do Qodana.

JDK configurado:
yamlprojectJDK: "25"
Usa o Java 25, o que é consistente com o projeto.

Linter:
yamllinter: jetbrains/qodana-jvm:2025.3
Usa o linter específico para JVM, adequado para projetos Java como o Battleship2.

Quality Gates — desativados:
yaml#failureConditions:

severityThresholds:
any: 15
critical: 5

Os quality gates estão comentados, o que significa que o pipeline CI/CD nunca falha por problemas de qualidade. Para a Tarefa 2 (onde criámos quality gates com GitHub Actions) foi necessário ativar e configurar estas condições, definindo limiares adequados para as severidades dos problemas detetados.

Aspetos não configurados:
As secções include/exclude (para ativar/desativar inspeções específicas), bootstrap (scripts de preparação) e plugins estão todas comentadas, indicando uma configuração mínima por omissão.

5. O relatório gerado pelo Qodana identificou um total de 18 problemas distribuídos por 5 ficheiros do projeto, organizados nas categorias Probable Bugs, Security, Performance e Threading Issues.

A categoria com maior expressão é a de Probable Bugs, com ocorrências em três ficheiros distintos. O ficheiro Ship.java concentra a maioria dos problemas, com oito ocorrências no total: duas relacionadas com variáveis que recebem um valor que já detinham, constituindo código redundante, e seis casos de indentação suspeita após instruções if sem chavetas delimitadoras. Este último tipo de problema é particularmente insidioso, uma vez que a formatação visual do código sugere que determinadas instruções fazem parte de um bloco condicional quando na realidade não fazem, podendo originar comportamentos inesperados difíceis de diagnosticar. No ficheiro Game.java foi identificada uma inicialização redundante de uma variável a null, e no HuggingFaceClient.java foi detetado um risco de NullPointerException em tempo de execução.

Relativamente à categoria Security, foram identificadas quatro vulnerabilidades no pom.xml, já detalhadas no ponto anterior, associadas a dependências diretas e transitivas com CVEs conhecidas.

Na categoria Performance, o ficheiro Game.java apresenta duas ocorrências do uso de removeAll sobre um Set passando uma List como argumento, operação com complexidade O(n²) que poderia ser otimizada convertendo previamente a lista para um conjunto.

Na categoria Threading Issues, o ficheiro Tasks.java contém uma chamada a Thread.sleep() dentro de um ciclo, padrão conhecido como espera ativa, que representa uma utilização ineficiente de recursos do sistema e deveria ser substituído por mecanismos de sincronização mais adequados.

Em síntese, o ficheiro mais problemático é o Ship.java, e o problema de maior criticidade para a estabilidade do sistema é o potencial NullPointerException no HuggingFaceClient.java. Estes resultados evidenciam a importância da análise estática de código como complemento ao processo de desenvolvimento, permitindo identificar problemas que poderiam passar despercebidos numa revisão manual.

**Tarefa 2**
A) 

1. Os cheiros no código (code smells) são sintomas de potenciais problemas estruturais no software que, embora não impeçam o seu funcionamento correto, sinalizam situações onde o código pode ser difícil de compreender, manter, testar ou evoluir, podendo gerar bugs no futuro. O conceito foi popularizado por Martin Fowler no seu livro Refactoring: Improving the Design of Existing Code, onde identificou um catálogo de padrões problemáticos recorrentes no desenvolvimento de software.

Os principais cheiros no código e as suas razões de ser são os seguintes:
**Long Method** ocorre quando um método cresce demasiado em número de linhas de código, tornando-se difícil de compreender e testar. A razão subjacente é normalmente a acumulação progressiva de lógica num único método ao longo do tempo, violando o princípio da responsabilidade única.
**Large Class** (ou God Class) verifica-se quando uma classe acumula demasiadas responsabilidades, conhecimento e comportamento, tornando-se um ponto central de dependência no sistema. Resulta frequentemente de um crescimento desordenado do código sem refatoração adequada, violando os princípios de coesão e encapsulamento.
**Feature Envy** acontece quando um método usa mais dados e métodos de outras classes do que da sua própria, sugerindo que a lógica está no lugar errado e deveria ser movida para a classe com a qual mais interage.
**Data Class** descreve classes que contêm apenas campos e métodos de acesso (getters e setters), sem comportamento próprio. Estas classes são frequentemente manipuladas excessivamente por outras classes, o que é sintoma de uma distribuição desequilibrada de responsabilidades.
**Duplicate Code** é um dos cheiros mais comuns e prejudiciais, ocorrendo quando a mesma estrutura de código aparece em múltiplos locais. Dificulta a manutenção porque qualquer correção tem de ser replicada em todos os locais onde o código duplicado existe.
**Long Parameter List** verifica-se quando um método recebe demasiados parâmetros, tornando-o difícil de invocar e compreender. Frequentemente resulta da fusão de várias operações num único método ou da falta de objetos de contexto adequados.
**Divergent Change** ocorre quando uma classe é frequentemente modificada por razões distintas e não relacionadas, indicando que a classe tem demasiadas responsabilidades e deveria ser dividida.
**Shotgun Surgery** é o oposto do anterior — uma única mudança lógica obriga a modificações em múltiplas classes dispersas pelo sistema, indicando que a responsabilidade está fragmentada de forma inadequada.
**Comments** pode paradoxalmente ser um cheiro no código quando os comentários existem para explicar código complexo ou confuso. Fowler argumenta que, se o código precisar de muitos comentários para ser compreendido, provavelmente deve ser refatorado para se tornar autoexplicativo.

Os cheiros no código têm como razão de ser comum a violação de princípios fundamentais de engenharia de software, nomeadamente a coesão, o baixo acoplamento e a responsabilidade única. A sua deteção precoce e eliminação através de refatorações adequadas é essencial para garantir a manutenibilidade e evolução sustentável do software ao longo do tempo.


2. No segumento da pergunta anterior, e com base no capítulo 3 do livro Refactoring de Martin Fowler e Kent Beck, e cruzando com as estratégias quantitativas de deteção apresentadas no livro Object-Oriented Metrics in Practice de Lanza e Marinescu, os seguintes cheiros no código do catálogo de Fowler são formalizados através de métricas objetivas:

**Long Method** é formalizado através das métricas LOC (Lines of Code) e CC (McCabe Cyclomatic Complexity), com limiares definidos como LOC ≥ 16 e CC ≥ 3. Fowler descreve este cheiro como o resultado da tendência dos programadores para acrescentar lógica a métodos existentes sem os decompor, tornando-os progressivamente mais difíceis de compreender e testar.
**Large Class** (ou God Class) é formalizado com uma combinação de ATFD (Access To Foreign Data), WMC (Weighted Methods per Class) e TCC (Tight Class Cohesion), sendo considerado God Class quando ATFD > 4, WMC ≥ 47 e TCC < 0.33. Fowler identifica este cheiro quando uma classe acumula demasiadas variáveis de instância e responsabilidades, tornando-se um ponto central de dependência no sistema.
**Feature Envy** é formalizado com as métricas ATFD (Access To Foreign Data), LAA (Locality of Attribute Accesses) e FDP (Foreign Data Providers), sendo detetado quando ATFD > 4, LAA < 0.33 e FDP ≤ 5. Fowler descreve este cheiro como um método que parece mais interessado nos dados de outra classe do que nos da sua própria.
**Data Class** é formalizado com NOAM (Number of Accessor Methods), NOPA (Number of Public Attributes) e WMC, sendo detetado quando NOAM ≥ 4, NOPA ≥ 3, WMC < 15 e WOC < 0.34. Fowler caracteriza estas classes como simples contentores de dados sem comportamento próprio, manipulados excessivamente por outras classes.
**Shotgun Surgery** é formalizado através das métricas CM (Changing Methods) e CC, detetando situações em que uma única alteração lógica obriga a modificações em múltiplas classes dispersas pelo sistema.
**Long Parameter List** é formalizado com a métrica NOPM (Number of Parameters), com limiar NOPM ≥ 4. Fowler adverte que listas longas de parâmetros são difíceis de compreender e tendem a mudar frequentemente, sendo sintoma de que o método está a receber dados que deveria obter diretamente dos objetos com que interage.
**Divergent Change e Shotgun Surgery** são formalizados indiretamente através de métricas de acoplamento como CBO (Coupling Between Objects) e RFC (Response For a Class), que medem o grau de dependência entre classes e a dispersão de responsabilidades.

Em síntese, nem todos os cheiros do catálogo de Fowler possuem uma formalização quantitativa direta, cheiros como Comments, Lazy Class ou Speculative Generality dependem mais de julgamento humano do que de métricas objetivas. Os cheiros mais bem formalizados são precisamente os que têm expressão estrutural clara no código, como o tamanho de métodos e classes, o acoplamento entre objetos e a distribuição de responsabilidades, tornando-os passíveis de deteção automática por ferramentas como o Qodana ou o MetricsTree.

**5 - 112244-Fábio Reis**

O workflow de deteção do code smell Long Method foi implementado com sucesso no repositório BattleShip2, utilizando o Qodana for JVM através do GitHub Actions. O quality gate foi configurado com base nos critérios quantitativos propostos por Lanza e Marinescu no livro Object-Oriented Metrics in Practice, nomeadamente LOC ≥ 16 e CC ≥ 3.

Após a execução do workflow, o Qodana detetou 67 instâncias do code smell Long Method distribuídas pelo projeto, todas com severidade High. O quality gate falhou por exceder o limiar definido de 5 problemas, confirmando a presença generalizada deste code smell. Os casos mais graves identificados foram o método menu() da classe Tasks.java, com 210 linhas de código e complexidade ciclomática de 52, e o método processEnemyFire() da classe Move.java, com 77 linhas e complexidade ciclomática de 37. Estes valores excedem largamente os limiares definidos, confirmando que a parametrização inicial sugerida pelo livro se revelou adequada para o projeto, não sendo necessário ajustá-la. O relatório completo foi disponibilizado no Qodana Cloud e o workflow YAML foi carregado no repositório com o meu número de aluno e nome na primeira linha, conforme solicitado.

**B**
**6 - 112244-Fábio Reis**

Para verificar o correto funcionamento do workflow de análise com SonarQube Cloud, foi introduzido intencionalmente um bug de fiabilidade no código, concretamente um acesso a um índice inválido de um array (array[10] num array de dimensão 5), garantidamente causador de um ArrayIndexOutOfBoundsException em runtime.

Após fazer push desta alteração para o branch main, o workflow SonarQube Cloud Analysis disparou automaticamente, tal como configurado. O SonarQube analisou o projeto e detetou o bug introduzido na categoria Reliability, fazendo falhar o Quality Gate com a mensagem QUALITY GATE STATUS: FAILED. Este resultado confirma que o pipeline de integração contínua está a funcionar corretamente, qualquer push para o branch main que introduza problemas de qualidade é automaticamente detetado e sinalizado antes de poder comprometer a integridade do projeto.

Após a verificação, o erro intencional foi revertido através de um novo commit, e o workflow voltou a executar, confirmando o restabelecimento do estado anterior do projeto. Esta experiência demonstra na prática o valor da integração de ferramentas de análise estática de qualidade em pipelines de CI/CD, permitindo detetar e corrigir problemas de qualidade de forma contínua e automatizada.

## 📖 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [Technical Stack](#-technical-stack)
- [Installation & Setup](#-installation--setup)
- [Code Architecture](#-code-architecture)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)

---

## 🎯 Project Overview
This project serves as a template and reference for students learning **Object-Oriented Programming (OOP)** and **Software Quality**. It simulates a battleship environment where players must strategically place ships and sink the enemy fleet.

### 🎮 The Rules
The game is played on a grid (typically 10x10). The coordinate system is defined as:

$$(x, y) \in \{0, \dots, 9\} \times \{0, \dots, 9\}$$

Hits are calculated based on the intersection of the shot vector and the ship's bounding box.

---

## ✨ Key Features
| Feature | Description | Status |
| :--- | :--- | :---: |
| **Grid System** | Flexible $N \times N$ board generation. | ✅ |
| **Ship Varieties** | Galleons, Frigates, and Brigantines (XVII Century theme). | ✅ |
| **AI Opponent** | Heuristic-based targeting system. | 🚧 |
| **Network Play** | Socket-based multiplayer. | ❌ |

---

## 🛠 Technical Stack
* **Language:** Java 17
* **Build Tool:** Maven / Gradle
* **Testing:** JUnit 5
* **Logging:** Log4j2

---

## 🚀 Installation & Setup

### Prerequisites
* JDK 17 or higher
* Git

### Step-by-Step
1. **Clone the repository:**
   ```bash
   git clone [https://github.com/britoeabreu/Battleship2.git](https://github.com/britoeabreu/Battleship2.git)
   ```
2. **Navigate to directory:**
   ```bash
   cd Battleship2
   ```
3. **Compile and Run:**
   ```bash
   javac Main.java && java Main
   ```

---

## 📚 Documentation

You can access the generated Javadoc here:

👉 [Battleship2 API Documentation](https://britoeabreu.github.io/Battleship2/)


### Core Logic
```java
public class Ship {
    private String name;
    private int size;
    private boolean isSunk;

    // TODO: Implement damage logic
    public void hit() {
        // Implementation here
    }
}
```

### Design Patterns Used:
- **Strategy Pattern:** For different AI difficulty levels.
- **Observer Pattern:** To update the UI when a ship is hit.
</details>

### Logic Flow
```mermaid
graph TD
    A[Start Game] --> B{Place Ships}
    B --> C[Player Turn]
    C --> D[Target Coordinate]
    D --> E{Hit or Miss?}
    E -- Hit --> F[Check if Sunk]
    E -- Miss --> G[AI Turn]
    F --> G
    G --> C
```

---

## 🗺 Roadmap
- [x] Basic grid implementation
- [x] Ship placement validation
- [ ] Add sound effects (SFX)
- [ ] Implement "Fog of War" mechanic
- [ ] **Multiplayer Integration** (High Priority)

---

## 🧪 Testing
We use high-coverage unit testing to ensure game stability. Run tests using:
```bash
mvn test
```

> [!TIP]
> Use the `-Dtest=ClassName` flag to run specific test suites during development.

---

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a **Pull Request**

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

---
**Maintained by:** [@britoeabreu](https://github.com/britoeabreu)  
*Created for the Software Engineering students at ISCTE-IUL.*
