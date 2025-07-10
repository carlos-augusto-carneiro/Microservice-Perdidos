#!/bin/sh
set -e

# Aguardar o Eureka Server primeiro
echo "API Gateway: Aguardando Eureka Server..."
until nc -z -v -w30 eureka-server 8761; do
  echo "API Gateway: Aguardando eureka-server..."
  sleep 5
done
echo "API Gateway: Eureka Server está pronto!"

# Aguardar o serviço de usuários estar pronto
echo "API Gateway: Aguardando serviço de usuários..."
until nc -z -v -w30 usuarios-services 8081; do
  echo "API Gateway: Aguardando usuários-services..."
  sleep 5
done
echo "API Gateway: Serviço de usuários está pronto!"

# Aguardar o serviço de email estar pronto
echo "API Gateway: Aguardando serviço de email..."
until nc -z -v -w30 email-services 8082; do
  echo "API Gateway: Aguardando email-services..."
  sleep 5
done
echo "API Gateway: Serviço de email está pronto!"

# Aguarda um pouco mais para garantir que os serviços estejam totalmente inicializados
sleep 10

echo "API Gateway: Iniciando..."
exec java -jar /app/app.jar 