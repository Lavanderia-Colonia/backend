# 🐳 Docker - Lavanderia Colônia API

Este documento explica a configuração Docker para a API.

## 📦 Dockerfile

O Dockerfile usa **multi-stage build** para otimizar o tamanho da imagem final:

### Stage 1: Build
- **Imagem Base**: `maven:3.9-eclipse-temurin-17-alpine`
- **Função**: Compilar a aplicação Java
- **Otimização**: Cache de dependências do Maven

### Stage 2: Runtime
- **Imagem Base**: `eclipse-temurin:17-jre-alpine`
- **Função**: Executar a aplicação
- **Tamanho**: ~200-250 MB (muito menor que incluir o Maven e o JDK completo)

## 🔨 Build Local

```bash
# Navegar para o diretório da API
cd api

# Build da imagem
docker build -t lavanderia-colonia-api:latest .

# Build com tag específica
docker build -t lavanderia-colonia-api:1.0.0 .
```

## 🚀 Executar Local

### Com Docker Run

```bash
docker run -d \
  --name lavanderia-api \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/lavanderia_colonia \
  -e SPRING_DATASOURCE_USERNAME=lavanderia_user \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_REDIS_PORT=6379 \
  lavanderia-colonia-api:latest
```

### Com Docker Compose (Desenvolvimento)

O arquivo `compose.yaml` já existente pode ser usado para desenvolvimento:

```bash
docker-compose up
```

## 🔧 Variáveis de Ambiente

| Variável | Descrição | Valor Padrão |
|----------|-----------|--------------|
| `SPRING_PROFILES_ACTIVE` | Profile ativo do Spring | - |
| `SERVER_PORT` | Porta do servidor | 8080 |
| `SPRING_DATASOURCE_URL` | URL do PostgreSQL | - |
| `SPRING_DATASOURCE_USERNAME` | Usuário do PostgreSQL | - |
| `SPRING_DATASOURCE_PASSWORD` | Senha do PostgreSQL | - |
| `SPRING_REDIS_HOST` | Host do Redis | - |
| `SPRING_REDIS_PORT` | Porta do Redis | 6379 |
| `JAVA_OPTS` | Opções da JVM | `-Xmx512m -Xms256m` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | Estratégia DDL Hibernate | update |

## 📊 Tamanhos das Imagens

- **Stage 1 (Build)**: ~500 MB (incluindo Maven + JDK)
- **Stage 2 (Runtime)**: ~200-250 MB (apenas JRE + aplicação)

## 🎯 Otimizações

1. **Alpine Linux**: Imagem base mínima
2. **Multi-stage Build**: Separação entre build e runtime
3. **Cache de Dependências**: Maven baixa dependências em camada separada
4. **Usuário não-root**: Segurança aprimorada
5. **.dockerignore**: Evita copiar arquivos desnecessários

## 🔍 Logs

```bash
# Ver logs
docker logs lavanderia-api

# Ver logs em tempo real
docker logs -f lavanderia-api

# Ver últimas 100 linhas
docker logs --tail 100 lavanderia-api
```

## 🛠️ Comandos Úteis

```bash
# Parar o container
docker stop lavanderia-api

# Iniciar o container
docker start lavanderia-api

# Reiniciar o container
docker restart lavanderia-api

# Remover o container
docker rm lavanderia-api

# Remover a imagem
docker rmi lavanderia-colonia-api:latest

# Acessar o shell do container
docker exec -it lavanderia-api sh

# Ver uso de recursos
docker stats lavanderia-api

# Inspecionar o container
docker inspect lavanderia-api
```

## 🧪 Health Check

Verificar se a aplicação está saudável:

```bash
curl http://localhost:8080/actuator/health
```

Resposta esperada:
```json
{
  "status": "UP"
}
```

## 🐛 Debug

Para executar em modo debug:

```bash
docker run -d \
  --name lavanderia-api \
  -p 8080:8080 \
  -p 5005:5005 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx512m -Xms256m -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/lavanderia_colonia \
  -e SPRING_DATASOURCE_USERNAME=lavanderia_user \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_REDIS_PORT=6379 \
  lavanderia-colonia-api:latest
```

Conecte seu IDE na porta 5005 para debug remoto.

## 📝 Notas

- A porta 8080 é exposta por padrão
- O Dockerfile usa JRE 17 (Alpine) para menor tamanho
- Logs do Spring Boot são configurados em `application-prod.properties`
- A aplicação roda com usuário não-root `spring:spring`

## 🔗 Links Úteis

- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Eclipse Temurin Images](https://hub.docker.com/_/eclipse-temurin)

