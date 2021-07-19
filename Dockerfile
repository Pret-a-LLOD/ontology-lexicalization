FROM maven:3.8-jdk-11 AS buildweb

ADD pom.xml /source/pom.xml
RUN cd /source && mvn verify clean --fail-never

ADD ./src /source/src
RUN cd /source && mvn -B package -DskipTests

FROM maven:3.8-jdk-11 AS buildconv

# Update the repository sources list
RUN apt-get update

# Install compiler and perl stuff
RUN apt-get install --yes \
 build-essential \
 gcc-multilib \
 apt-utils \
 perl \
 expat \
 libexpat-dev 

# Install perl modules 
RUN apt-get install -y cpanminus

RUN cpanm CPAN::Meta \
 readline \ 
 Term::ReadKey \
 YAML \
 Digest::SHA \
 Module::Build \
 ExtUtils::MakeMaker \
 Test::More \
 Data::Stag \
 Config::Simple \
 Statistics::Lite \
 Statistics::Descriptive \
 YAML::Syck \
 URL::Encode \
 Number::Bytes::Human \
 JSON \
 Data::Dumper \
 FileHandle \
 File::Basename \
 utf8 \
 File::Slurp \
 JSON::Parse \
 Term::ReadKey \
 Data::Dumper \
 Text::CSV

# wget installation
# Install wget and install/updates certificates
RUN apt-get update \
 && apt-get install -y -q --no-install-recommends \
    ca-certificates \
 && apt-get clean \
 && rm -r /var/lib/apt/lists/*


EXPOSE 8080
USER root

WORKDIR /app
COPY ./ /app
RUN chmod +x /app
COPY ./openapi.yaml /openapi.yaml
CMD ["java","-jar","target/rest-service-0.0.1-SNAPSHOT.jar"]
