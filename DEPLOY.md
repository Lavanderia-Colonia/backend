# 🚀 Guia de Deploy no Render

Este guia explica como fazer o deploy da aplicação Lavanderia Colônia no Render usando Docker.

## 📋 Pré-requisitos

1. Conta no [Render](https://render.com) (gratuita)
2. Repositório Git com o código (GitHub, GitLab ou Bitbucket)
3. Código commitado e enviado para o repositório remoto

## 🐳 Arquivos Docker Criados

- **`api/Dockerfile`**: Imagem Docker otimizada com multi-stage build
- **`api/.dockerignore`**: Exclui arquivos desnecessários do build
- **`render.yaml`**: Configuração completa da infraestrutura no Render

## 🎯 Método 1: Deploy Automático (Recomendado)

### Passo 1: Preparar o Repositório

```bash
# Adicionar os novos arquivos ao git
git add api/Dockerfile api/.dockerignore api/src/main/resources/application-prod.properties api/pom.xml render.yaml

# Fazer commit
git commit -m "Adicionar configuração Docker e Render"

# Enviar para o repositório remoto
git push origin main
```

### Passo 2: Criar o Blueprint no Render

1. Acesse [Render Dashboard](https://dashboard.render.com)
2. Clique em **"New +"** → **"Blueprint"**
3. Conecte seu repositório (GitHub, GitLab ou Bitbucket)
4. O Render detectará automaticamente o arquivo `render.yaml`
5. Clique em **"Apply"**

O Render criará automaticamente:
- ✅ PostgreSQL Database (gratuito)
- ✅ Redis Instance (gratuito)
- ✅ Web Service com a API (gratuito)

### Passo 3: Aguardar o Deploy

O primeiro deploy pode levar de 5 a 10 minutos. O Render irá:
1. Construir a imagem Docker
2. Provisionar o banco de dados PostgreSQL
3. Provisionar o Redis
4. Conectar tudo automaticamente
5. Iniciar a aplicação

## 🔧 Método 2: Deploy Manual

### Passo 1: Criar PostgreSQL Database

1. No Dashboard do Render, clique em **"New +"** → **"PostgreSQL"**
2. Configure:
   - **Name**: `lavanderia-colonia-db`
   - **Database**: `lavanderia_colonia`
   - **User**: `lavanderia_user`
   - **Region**: Oregon (ou sua preferência)
   - **Plan**: Free
3. Clique em **"Create Database"**
4. Anote as credenciais de conexão

### Passo 2: Criar Redis Instance

1. Clique em **"New +"** → **"Redis"**
2. Configure:
   - **Name**: `lavanderia-colonia-redis`
   - **Region**: Oregon (mesma do banco)
   - **Plan**: Free
3. Clique em **"Create Redis"**

### Passo 3: Criar Web Service

1. Clique em **"New +"** → **"Web Service"**
2. Conecte seu repositório Git
3. Configure:
   - **Name**: `lavanderia-colonia-api`
   - **Region**: Oregon
   - **Branch**: `main`
   - **Runtime**: Docker
   - **Dockerfile Path**: `./api/Dockerfile`
   - **Docker Build Context Directory**: `./api`
   - **Plan**: Free

### Passo 4: Configurar Variáveis de Ambiente

Na seção **Environment Variables**, adicione:

```
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080
SPRING_DATASOURCE_URL=[Internal Connection String do PostgreSQL]
SPRING_DATASOURCE_USERNAME=[User do PostgreSQL]
SPRING_DATASOURCE_PASSWORD=[Password do PostgreSQL]
SPRING_REDIS_HOST=[Internal Redis Host]
SPRING_REDIS_PORT=[Internal Redis Port]
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false
JAVA_OPTS=-Xmx512m -Xms256m
```

**Nota**: Use as URLs/credenciais **internas** do Render para comunicação entre serviços (mais rápido e gratuito).

### Passo 5: Configurar Health Check

- **Health Check Path**: `/actuator/health`

### Passo 6: Deploy

1. Clique em **"Create Web Service"**
2. Aguarde o build e deploy

## 🔍 Verificar o Deploy

Após o deploy, sua API estará disponível em:
```
https://lavanderia-colonia-api.onrender.com
```

Teste o health check:
```bash
curl https://lavanderia-colonia-api.onrender.com/actuator/health
```

Resposta esperada:
```json
{
  "status": "UP"
}
```

## ⚙️ Configurações Importantes

### Limites do Plano Free

- **Web Service**: 
  - 512 MB RAM
  - CPU compartilhada
  - 750 horas/mês gratuitas
  - Suspende após 15 minutos de inatividade (cold start ao receber requisição)

- **PostgreSQL**:
  - 1 GB de armazenamento
  - Expira após 90 dias (precisa criar um novo)

- **Redis**:
  - 25 MB de armazenamento
  - Expira após 90 dias

### Auto Deploy

O Render faz deploy automático quando você faz push para a branch configurada (geralmente `main`).

Para desabilitar:
1. Vá em **Settings** do seu serviço
2. Desmarque **"Auto-Deploy"**

### Logs

Para ver os logs da aplicação:
1. Acesse seu Web Service no Dashboard
2. Clique na aba **"Logs"**
3. Ou use o Render CLI:
   ```bash
   render logs lavanderia-colonia-api
   ```

### Variáveis de Ambiente Sensíveis

Para adicionar secrets (senhas, tokens, etc.):
1. Vá em **Environment** do seu serviço
2. Adicione as variáveis
3. Clique em **"Save Changes"**
4. O serviço será redeploy automaticamente

## 🧪 Testar Localmente com Docker

Antes de fazer o deploy, teste localmente:

```bash
# Navegar para o diretório da API
cd api

# Build da imagem
docker build -t lavanderia-colonia-api .

# Executar (assumindo PostgreSQL e Redis rodando)
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/mydatabase \
  -e SPRING_DATASOURCE_USERNAME=myuser \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SPRING_REDIS_HOST=host.docker.internal \
  -e SPRING_REDIS_PORT=6379 \
  -e SPRING_PROFILES_ACTIVE=prod \
  lavanderia-colonia-api
```

Ou use o docker-compose existente para ambiente de desenvolvimento.

## 🚨 Troubleshooting

### Build falha

- Verifique se o `Dockerfile` está no caminho correto: `api/Dockerfile`
- Verifique se o `pom.xml` está correto
- Verifique os logs de build no Render

### Aplicação não inicia

- Verifique as variáveis de ambiente
- Verifique os logs da aplicação
- Certifique-se de que o PostgreSQL e Redis foram criados primeiro

### Health check falha

- Verifique se a aplicação está respondendo na porta 8080
- Verifique se o endpoint `/actuator/health` está acessível
- Verifique se o Spring Boot Actuator está configurado

### Cold Starts (Plano Free)

No plano gratuito, o serviço suspende após 15 minutos de inatividade. A primeira requisição após a suspensão levará ~30-60 segundos para responder.

Para evitar:
- Upgrade para um plano pago (Starting at $7/month)
- Use um serviço de ping (ex: UptimeRobot) para manter o serviço ativo

## 📚 Recursos Adicionais

- [Documentação do Render](https://render.com/docs)
- [Deploy de Spring Boot no Render](https://render.com/docs/deploy-spring-boot)
- [Blueprint Specs](https://render.com/docs/blueprint-spec)
- [Docker no Render](https://render.com/docs/docker)

## 💡 Dicas

1. **Use Cache do Maven**: O Dockerfile usa multi-stage build e cache de dependências para builds mais rápidos
2. **Monitore os Recursos**: Fique de olho no uso de RAM e CPU no Dashboard
3. **Logs Estruturados**: Configure logs JSON para melhor análise
4. **Backups**: O PostgreSQL free não tem backups automáticos, considere fazer backups manuais
5. **Custom Domain**: Você pode configurar um domínio personalizado nas configurações do serviço

## 🔄 Atualizações

Para atualizar a aplicação:
```bash
# Faça suas alterações
git add .
git commit -m "Descrição das alterações"
git push origin main
```

O Render fará o deploy automaticamente!

---

**Boa sorte com o deploy! 🎉**

