FROM openjdk:17-jdk-slim

# Install wget and unzip
RUN apt-get update && apt-get install -y wget unzip

# Install Gradle
ARG GRADLE_VERSION=7.5.1
ENV GRADLE_HOME=/opt/gradle/gradle-${GRADLE_VERSION}
ENV PATH=${GRADLE_HOME}/bin:${PATH}

RUN wget https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip \
    && mkdir /opt/gradle \
    && unzip -d /opt/gradle gradle-${GRADLE_VERSION}-bin.zip \
    && rm gradle-${GRADLE_VERSION}-bin.zip

WORKDIR /container_directory

CMD ["tail", "-f", "/dev/null"]
