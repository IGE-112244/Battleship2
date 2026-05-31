FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive

# Instalar dependências base incluindo gnupg
RUN apt-get update && apt-get install -y \
    wget \
    tar \
    git \
    curl \
    ca-certificates \
    gnupg \
    && apt-get clean

# Instalar Amazon Corretto 21
RUN curl -fsSL https://apt.corretto.aws/corretto.key | \
    gpg --dearmor -o /usr/share/keyrings/corretto.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/corretto.gpg] \
    https://apt.corretto.aws stable main" | \
    tee /etc/apt/sources.list.d/corretto.list && \
    apt-get update && \
    apt-get install -y java-21-amazon-corretto-jdk && \
    apt-get clean

# Instalar Maven 3.9.9
ARG MAVEN_VERSION=3.9.9
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV DEBIAN_FRONTEND=dialog

RUN java -version && mvn -version && git --version

WORKDIR /IdeaProjects/Battleship2