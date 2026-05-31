FROM amazoncorretto:21

# Instalar Maven 3.9.9 manualmente
ARG MAVEN_VERSION=3.9.9
RUN yum install -y wget tar git && \
    wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    yum clean all

ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}

WORKDIR /IdeaProjects/Battleship2

RUN java -version && mvn -version && git --version