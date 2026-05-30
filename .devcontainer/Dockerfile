FROM amazoncorretto:21

# Instalar Maven e Git
RUN yum update -y && \
    yum install -y maven git && \
    yum clean all

# Definir pasta de trabalho
WORKDIR /workspace