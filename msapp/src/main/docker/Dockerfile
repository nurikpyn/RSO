FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0 \
    JAVA_OPTS=""

CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war

EXPOSE 8081

ADD *.war /app.war
ADD **/*.properties /src/main/resources/
ADD h2db/db/msapp.mv.db /target/h2db/db/msapp.mv.db
ADD h2db/db/msapp.trace.db /target/h2db/db/msapp.trace.db