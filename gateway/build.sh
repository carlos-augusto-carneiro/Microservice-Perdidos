#!/bin/bash

echo "Building API Gateway..."

# Limpar e compilar o projeto
mvn clean package -DskipTests

echo "API Gateway build completed!"
echo "JAR file created at: target/gateway-0.0.1-SNAPSHOT.jar" 