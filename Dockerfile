FROM java:17

LABEL maintainer="hfredi35@gmail.com"

VOLUME /tmp

EXPOSE 8083

WORKDIR /app

COPY --from=build /app/target/traelo-delivery-1.0.0.jar traelo-delivery.jar

RUN bash -c 'touch /traelo-delivery.jar'

ENV JAVA_OPTS=""

# Run the jar file
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /traelo-delivery.jar"]