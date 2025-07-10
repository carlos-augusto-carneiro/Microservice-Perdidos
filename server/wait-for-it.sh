#!/bin/sh
set -e

echo "Eureka Server: Iniciando serviço de descoberta..."
echo "Eureka Server: Este é o primeiro serviço a iniciar - outros aguardarão por ele"

# Inicia diretamente o Eureka Server
exec java -jar /app/app.jar 