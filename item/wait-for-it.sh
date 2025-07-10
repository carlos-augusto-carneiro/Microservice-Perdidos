#!/bin/sh
set -e

# Espera o Eureka Server (deve ser o primeiro a estar pronto)
until nc -z -v -w30 eureka-server 8761; do
  echo "Item Service: Aguardando Eureka Server..."
  sleep 5
done
echo "Item Service: Eureka Server está pronto!"

# Espera o PostgreSQL
until nc -z -v -w30 postgres 5432; do
  echo "Item Service: Aguardando PostgreSQL..."
  sleep 5
done
echo "Item Service: PostgreSQL está pronto!"

# Espera o Kafka
until nc -z kafka 29092; do
  echo "Item Service: Aguardando Kafka..."
  sleep 5
done
echo "Item Service: Kafka está pronto!"

# Aguarda um pouco mais para garantir que o Kafka esteja totalmente inicializado
sleep 10

exec java -jar /app/app.jar