FROM maven:3.8-jdk-11 AS buildweb

ADD pom.xml /source/pom.xml
RUN cd /source && mvn verify clean --fail-never

ADD ./src /source/src
RUN cd /source && mvn -B package -DskipTests

FROM maven:3.8-jdk-11 AS buildconv

FROM ubuntu:bionic

ENV TERM xterm
ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update && apt-get install -y \
    acedb-other-dotter \
    build-essential \
    bzip2 \
    cpanminus

# accepts Microsoft EULA agreement without prompting
# view EULA at http://wwww.microsoft.com/typography/fontpack/eula.htm
RUN { echo ttf-mscorefonts-installer msttcorefonts/accepted-mscorefonts-eula select true; } | debconf-set-selections \
    && apt-get update && apt-get install -y ttf-mscorefonts-installer


RUN export PERL5LIB=.:$PERL5LIB && \
    cpanm \
    Archive::Zip \
    BSD::Resource \
    YAML::Syck \
    URL::Encode \
    Text::CSV \
    Number::Bytes::Human \
    IO::Uncompress::Bunzip2

FROM openjdk:11.0.7


EXPOSE 8080
USER root

WORKDIR /app
COPY ./ /app
RUN chmod +x /app
COPY ./openapi.yaml /openapi.yaml
CMD ["java","-jar","target/rest-service-0.0.1-SNAPSHOT.jar"]

