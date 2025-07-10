#!/bin/sh
set -e

# Espera o Eureka Server (deve ser o primeiro a estar pronto)
until nc -z -v -w30 eureka-server 8761; do
  echo "Email Service: Aguardando Eureka Server..."
  sleep 5
done
echo "Email Service: Eureka Server está pronto!"

# Espera o PostgreSQL
until nc -z -v -w30 postgres 5432; do
  echo "Email Service: Aguardando PostgreSQL..."
  sleep 5
done
echo "Email Service: PostgreSQL está pronto!"

# Espera o Kafka
until nc -z kafka 29092; do
  echo "Email Service: Aguardando Kafka..."
  sleep 5
done
echo "Email Service: Kafka está pronto!"

# Aguarda um pouco mais para garantir que o Kafka esteja totalmente inicializado
sleep 10

exec java -jar /app/app.jar