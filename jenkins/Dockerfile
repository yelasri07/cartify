FROM jenkins/agent:jdk21

USER root
RUN apt-get update && \
    apt-get install -y wget gpg && \
    wget -qO /usr/share/keyrings/adoptium.asc \
        https://packages.adoptium.net/artifactory/api/gpg/key/public && \
    echo "deb [signed-by=/usr/share/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb bookworm main" \
        > /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y temurin-17-jdk && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME_17=/usr/lib/jvm/temurin-17-jdk-amd64
USER jenkins