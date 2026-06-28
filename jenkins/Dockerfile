FROM jenkins/agent:jdk21

# USER root
# RUN apt-get update && \
#     apt-get install -y wget gpg && \
#     wget -qO /usr/share/keyrings/adoptium.asc \
#         https://packages.adoptium.net/artifactory/api/gpg/key/public && \
#     echo "deb [signed-by=/usr/share/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb bookworm main" \
#         > /etc/apt/sources.list.d/adoptium.list && \
#     apt-get update && \
#     apt-get install -y temurin-17-jdk && \
#     apt-get clean && rm -rf /var/lib/apt/lists/*

# ENV JAVA_HOME_17=/usr/lib/jvm/temurin-17-jdk-amd64
# USER jenkins

USER root
RUN apt-get update && apt-get install -y lsb-release python3-pip
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli

# Add jenkins user to the docker group
RUN groupadd -f docker && usermod -aG docker jenkins

USER jenkins