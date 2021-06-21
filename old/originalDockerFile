FROM maven:3.8-jdk-11 AS buildweb

ADD pom.xml /source/pom.xml
RUN cd /source && mvn verify clean --fail-never

ADD ./src /source/src
RUN cd /source && mvn -B package -DskipTests

FROM maven:3.8-jdk-11 AS buildconv

# Install runtime packages
RUN apt-get update \
    && apt-get install -y \
      perl


EXPOSE 8080
USER root

WORKDIR /app
COPY ./ /app
RUN chmod +x /app
COPY ./openapi.yaml /openapi.yaml
CMD ["java","-jar","target/rest-service-0.0.1-SNAPSHOT.jar"]

