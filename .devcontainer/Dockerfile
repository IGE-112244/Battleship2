FROM amazoncorretto:21-al2023

ENV JAVA_TOOL_OPTIONS=""

RUN yum install -y wget tar git && \
    yum clean all

# Instalar Maven 3.9.9
ARG MAVEN_VERSION=3.9.9
RUN wget https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar -xzf apache-maven-${MAVEN_VERSION}-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven && \
    rm apache-maven-${MAVEN_VERSION}-bin.tar.gz

ENV MAVEN_HOME=/opt/maven
ENV PATH=${MAVEN_HOME}/bin:${PATH}
ENV JAVA_HOME=/usr/lib/jvm/java-21-amazon-corretto
ENV JAVA_TOOL_OPTIONS=""

RUN echo 'unset JAVA_TOOL_OPTIONS' >> /etc/profile
RUN echo 'unset JAVA_TOOL_OPTIONS' >> /root/.bashrc

RUN java -version && mvn -version && git --version

WORKDIR /IdeaProjects/Battleship2
