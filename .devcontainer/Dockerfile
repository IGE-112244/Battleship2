FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y openjdk-21-jdk wget tar git && \
    apt-get clean

# Instalar Maven 3.9.9 manualmente
ARG MAVEN_VERSION=3.9.9
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV MAVEN_HOME=/opt/maven
ENV PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH

WORKDIR /IdeaProjects/Battleship2

RUN java -version && mvn -version && git --version