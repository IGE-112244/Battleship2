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
| 105498  | Luís Manteigas    |
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

Importa realçar que devido às limitações da API de IA gratuita o prompt tem de ser mais curto do que o necessário, impossibilitando a IA de atingir todo o seu potencial criando o jogo mais interativo e podendo levar a possíveis erros como jogadas repetidas ou fora do tabuleiro (raramente). Eventualmente, aparecia este erro:

Exception in thread "main" java.lang.RuntimeException: Erro na API do Hugging Face: 400 - 
Detalhes: {"message":"Please reduce the length of the messages or completion. Current length is 8677 while limit is 8192","type":"invalid_request_error","param":"messages","code":"context_length_exceeded","id":""}
	at battleship.HuggingFaceClient.callAPI(HuggingFaceClient.java:240)
	at battleship.HuggingFaceClient.getNextShots(HuggingFaceClient.java:71)
	at battleship.Tasks.menu(Tasks.java:217)
	at battleship.Main.main(Main.java:24)

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
