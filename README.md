# How to build the application:
'''
    gradle build
'''

# How to run he built application
'''
    java -jar build/libs/rest-service-0.0.1-SNAPSHOT.jar
'''


# building docker container
'''
    docker build -t teanga_dummy_service_java .
'''

# running docker container on port 8001 in localhost
'''
    docker run -p 8001:8080 -t teanga_dummy_service
'''
