#!/bin/bash

# Configurar safe directory do Git
git config --global --add safe.directory /IdeaProjects/Battleship2

# Encontrar a pasta de configuração do IntelliJ
INTELLIJ_CONFIG=$(find /.jbdevcontainer/config/JetBrains -maxdepth 1 -type d -name "IntelliJIdea*" 2>/dev/null | head -1)

if [ -z "$INTELLIJ_CONFIG" ]; then
    INTELLIJ_CONFIG="/.jbdevcontainer/config/JetBrains/IntelliJIdea2026.1"
    mkdir -p "$INTELLIJ_CONFIG"
fi

echo "Configurando SDK em: $INTELLIJ_CONFIG"

# Criar ficheiro jdk.table.xml
cat > "$INTELLIJ_CONFIG/jdk.table.xml" << 'EOF'
<application>
  <component name="ProjectJdkTable">
    <jdk version="2">
      <name value="corretto-21" />
      <type value="JavaSDK" />
      <version value="java version &quot;21.0.11&quot;" />
      <homePath value="/usr/lib/jvm/java-21-amazon-corretto" />
      <roots>
        <annotationsPath>
          <root type="composite" />
        </annotationsPath>
        <classPath>
          <root type="composite" />
        </classPath>
        <javadocPath>
          <root type="composite" />
        </javadocPath>
        <sourcePath>
          <root type="composite" />
        </sourcePath>
      </roots>
    </jdk>
  </component>
</application>
EOF

echo "SDK corretto-21 configurado com sucesso!"

# Forçar import do Maven no IntelliJ
echo "A configurar Maven import automático..."
mkdir -p /IdeaProjects/Battleship2/.idea
cat > /IdeaProjects/Battleship2/.idea/compiler.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="CompilerConfiguration">
    <option name="DEFAULT_COMPILER" value="Javac" />
  </component>
</project>
EOF

# Resolver dependências e compilar
echo "A resolver dependências Maven..."
mvn -f /IdeaProjects/Battleship2/pom.xml dependency:resolve -q && \
    echo "Dependências resolvidas!"

echo "A compilar o projeto..."
mvn -f /IdeaProjects/Battleship2/pom.xml clean compile -q && \
    echo "BUILD SUCCESS" || echo "BUILD FAILED"