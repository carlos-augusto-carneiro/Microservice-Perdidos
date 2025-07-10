# Build stage
FROM maven:3.6.3-openjdk-17 AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package -DskipTests

#Eureka-Server service image
FROM openjdk:17-slim AS eureka-server
RUN apt-get update && apt-get install -y netcat-openbsd
COPY --from=build /usr/src/app/server/target/server-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /usr/src/app/server/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
WORKDIR /app
EXPOSE 8761
CMD ["/app/wait-for-it.sh", "localhost:8761", "--", "java", "-jar", "/app/app.jar"]

# Gateway service image
FROM openjdk:17-slim AS gateway
RUN apt-get update && apt-get install -y netcat-openbsd
COPY --from=build /usr/src/app/gateway/target/gateway-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /usr/src/app/gateway/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
WORKDIR /app
EXPOSE 8080
CMD ["/app/wait-for-it.sh"]

# Email service image
FROM openjdk:17-slim AS email
RUN apt-get update && apt-get install -y netcat-openbsd
COPY --from=build /usr/src/app/email/target/email-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /usr/src/app/email/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
WORKDIR /app
EXPOSE 8082
CMD ["/app/wait-for-it.sh"]

# Usuarios service image
FROM openjdk:17-slim AS usuarios
RUN apt-get update && apt-get install -y netcat-openbsd
COPY --from=build /usr/src/app/usuarios/target/usuarios-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /usr/src/app/usuarios/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
WORKDIR /app
EXPOSE 8081
CMD ["/app/wait-for-it.sh"]

# Item service image
FROM openjdk:17-slim AS item
RUN apt-get update && apt-get install -y netcat-openbsd
COPY --from=build /usr/src/app/item/target/item-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=build /usr/src/app/usuarios/wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
WORKDIR /app
EXPOSE 8083
CMD ["/app/wait-for-it.sh"]