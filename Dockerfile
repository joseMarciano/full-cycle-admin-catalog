FROM eclipse-temurin:17.0.5_8-jre-alpine

COPY build/libs/*.jar /opt/app/application.jar

# create a group called 'spring', create a user 'spring' and add in a created 'spring' group
RUN addgroup -S spring && adduser -S spring -G spring

#set user spring for cli.
USER spring:spring

CMD java -jar /opt/app/application.jar
