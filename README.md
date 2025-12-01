# 🧺 Sistema de Gestão - Lavanderia Colônia

> Sistema de gerenciamento completo para lavanderia desenvolvido como projeto acadêmico da disciplina de Engenharia de Software III

## 📚 Informações Acadêmicas

**Instituição:** Faculdade de Tecnologia de Mogi das Cruzes (FATEC)  
**Curso:** Análise e Desenvolvimento de Sistemas  
**Disciplina:** Engenharia de Software III  
**Empresa:** Lavanderia Colônia  

---

## 📋 Sobre o Projeto

Sistema backend desenvolvido em Java com Spring Boot para gerenciar operações de uma lavanderia, incluindo:

- ✅ Cadastro e gestão de clientes
- ✅ Controle de pedidos e itens
- ✅ Acompanhamento de status de pedidos
- ✅ Sistema de auditoria com padrão Singleton
- ✅ Autenticação e autorização com JWT
- ✅ API RESTful completa

---

## 🏗️ Arquitetura e Padrões de Projeto

### **Padrões Criacionais**

#### 1. **Singleton Pattern** 🔒
Implementado no sistema de auditoria para garantir uma única instância do logger em toda a aplicação.

```java
public class AuditLogger {
    private static volatile AuditLogger instance;
    
    public static AuditLogger getInstance() {
        if (instance == null) {
            synchronized (AuditLogger.class) {
                if (instance == null) {
                    instance = new AuditLogger();
                }
            }
        }
        return instance;
    }
}
```

**Vantagens:**
- Thread-safe com Double-Checked Locking
- Única instância global do logger
- Integração automática com o banco de dados
- Rastreabilidade completa de todas as operações

**Uso:**
```java
AuditLogger logger = AuditLogger.getInstance();
logger.logOrder(AuditAction.ORDER_CREATED, orderId, "Cliente: João Silva");
```

#### 2. **Abstract Factory Pattern** 🏭
Utilizado para criação de `OrderItem` com diferentes estratégias (novo vs. existente).

```java
public interface OrderItemFactory {
    OrderItemProduct createOrderItem(OrderItemDTO dto);
}

// Factory para novos items
public class NewOrderItemFactory implements OrderItemFactory { ... }

// Factory para items existentes
public class ExistingOrderItemFactory implements OrderItemFactory { ... }
```

**Vantagens:**
- Separação de responsabilidades
- Facilita extensão sem modificar código existente
- Princípio Open/Closed (SOLID)

---

## 🛠️ Tecnologias Utilizadas

### **Backend**
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **MySQL** - Banco de dados relacional
- **JWT (JSON Web Tokens)** - Autenticação stateless
- **Lombok** - Redução de boilerplate

### **Ferramentas**
- **Maven** - Gerenciamento de dependências
- **Git** - Controle de versão

---

## 📦 Estrutura do Projeto

```
src/main/java/com/lavanderia_colonia/api/
│
├── config/                          # Configurações (Security, CORS)
├── controller/                      # Endpoints REST
│   ├── AuditController.java
│   ├── AuthController.java
│   ├── ClientController.java
│   ├── OrderController.java
│   └── ...
│
├── dto/                            # Data Transfer Objects
│   ├── AuditDTO.java
│   ├── ClientDTO.java
│   ├── OrderDTO.java
│   └── OrderItemDTO.java
│
├── enums/                          # Enumerações
│   ├── AuditAction.java
│   ├── OrderType.java
│   └── UserRole.java
│
├── model/                          # Entidades JPA
│   ├── Audit.java
│   ├── Client.java
│   ├── Order.java
│   ├── OrderItem.java
│   └── User.java
│
├── repository/                     # Repositórios JPA
│   ├── AuditRepository.java
│   ├── ClientRepository.java
│   └── OrderRepository.java
│
├── service/                        # Lógica de negócio
│   ├── AuditService.java
│   ├── ClientService.java
│   ├── OrderService.java
│   └── TokenProvider.java
│
├── pattern/                        # Design Patterns
│   └── creational/
│       └── singleton/
│           └── AuditLogger.java
│
└── exception/                      # Tratamento de exceções
    └── ResourceNotFoundException.java
```

---

## 🚀 Instalação e Configuração

### **Pré-requisitos**
- Java 17 ou superior
- MySQL 8.0+
- Maven 3.8+

### **1. Clone o repositório**
```bash
git clone https://github.com/seu-usuario/lavanderia-colonia-api.git
cd lavanderia-colonia-api
```

### **2. Configure o banco de dados**

Crie um banco de dados MySQL:
```sql
CREATE DATABASE lavanderia_colonia;
```

### **3. Configure o `application.properties`**

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/lavanderia_colonia
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
security.jwt.token.secret-key=sua-chave-secreta-super-segura
security.jwt.token.expire-length=3600000

# Server
server.port=8080
```

### **4. Execute o projeto**
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

---

## 🔐 Autenticação

### **Criar usuário administrador**
```bash
POST /api/v1/setup/create-admin
Content-Type: application/json

{
  "name": "admin",
  "password": "senha123",
  "masterPassword": "sua-chave-secreta-super-segura"
}
```

### **Fazer login**
```bash
POST /api/v1/auth/signin
Content-Type: application/json

{
  "login": "admin",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### **Usar o token nas requisições**
```bash
Authorization: Bearer {seu-token-jwt}
```

---

## 📡 Endpoints da API

### **🧑‍💼 Clientes**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/clients` | Lista clientes (paginado) |
| `GET` | `/api/v1/clients/{id}/history` | Histórico de pedidos |
| `GET` | `/api/v1/clients/active` | Lista clientes ativos |
| `POST` | `/api/v1/clients` | Criar cliente |
| `PUT` | `/api/v1/clients/{id}` | Atualizar cliente |
| `PUT` | `/api/v1/clients/{id}/toggle-active` | Ativar/desativar |

### **📦 Pedidos**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/orders` | Lista pedidos (paginado) |
| `GET` | `/api/v1/orders/{id}` | Buscar por ID |
| `POST` | `/api/v1/orders` | Criar pedido |
| `PUT` | `/api/v1/orders/{id}` | Atualizar pedido |
| `POST` | `/api/v1/orders/{id}/finish` | Finalizar pedido |
| `POST` | `/api/v1/orders/{id}/cancel` | Cancelar pedido |
| `DELETE` | `/api/v1/orders/{id}` | Deletar pedido |

### **📊 Auditoria**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/audits` | Últimas 10 auditorias |

### **🎨 Produtos e Cores**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/v1/products` | Lista produtos |
| `GET` | `/api/v1/order-item-colors` | Lista cores |
| `GET` | `/api/v1/order-statuses` | Lista status |

---

## 📝 Exemplos de Uso

### **Criar um Cliente**
```bash
POST /api/v1/clients
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "João Silva",
  "telephone": "(11) 98765-4321",
  "street": "Rua das Flores",
  "number": "123",
  "district": "Centro",
  "zipCode": "08780-000",
  "complement": "Apto 45"
}
```

### **Criar um Pedido**
```bash
POST /api/v1/orders
Content-Type: application/json
Authorization: Bearer {token}

{
  "clientId": 1,
  "finishType": "ENTREGA",
  "finishDeadline": "2024-12-15",
  "items": [
    {
      "productId": 1,
      "colorId": 2,
      "quantity": 3,
      "unitPrice": 25.00,
      "brand": "Nike",
      "observation": "Cuidado com a cor"
    }
  ]
}
```

### **Buscar Pedidos**
```bash
# Listar todos
GET /api/v1/orders?page=0&size=10

# Buscar por ID ou nome do cliente
GET /api/v1/orders?search=João&page=0&size=10
```

---

## 🔍 Sistema de Auditoria

Todas as operações críticas são automaticamente registradas no banco de dados:

### **Ações Auditadas:**
- ✅ Criação, atualização e exclusão de pedidos
- ✅ Criação e atualização de clientes
- ✅ Finalização e cancelamento de pedidos
- ✅ Login de usuários
- ✅ Mudanças de status

### **Exemplo de Registro:**
```
Pedido criado - Pedido #123 - Cliente: João Silva | Itens: 3
Pedido finalizado - Pedido #123 - Status: Em Aberto → Pago | Cliente: João Silva
Cliente criado - Cliente #45 (Maria Santos)
```

### **Consultar Auditoria:**
```bash
GET /api/v1/audits
```

**Resposta:**
```json
[
  {
    "id": 1,
    "description": "Pedido criado - Pedido #123 - Cliente: João Silva | Itens: 3",
    "changeDate": "2024-12-01",
    "createdAt": "2024-12-01",
    "updatedAt": "2024-12-01"
  }
]
```

---

## 🎯 Funcionalidades Principais

### **1. Gestão de Clientes**
- Cadastro completo com endereço
- Histórico de pedidos por cliente
- Ativação/desativação de clientes
- Busca por nome

### **2. Gestão de Pedidos**
- Criação de pedidos com múltiplos itens
- Diferentes tipos (Entrega ou Retirada)
- Controle de status (Em Aberto, Pago, Cancelado)
- Atualização dinâmica de itens
- Busca por ID ou cliente

### **3. Segurança**
- Autenticação JWT
- Proteção de rotas
- Níveis de acesso (USER, ADMIN)
- Senhas criptografadas com BCrypt

### **4. Auditoria**
- Rastreamento automático de operações
- Registro de usuário e timestamp
- Histórico completo de mudanças
- Padrão Singleton thread-safe

---

## 🧪 Testes

Para executar os testes:
```bash
mvn test
```

---

## 📊 Modelo de Dados

### **Principais Entidades:**

#### **Client**
- id, name, telephone
- street, number, district, zipCode, complement
- active, createdAt, updatedAt

#### **Order**
- id, status, finishType, finishDeadline
- createdAt, updatedAt, deliveryDate
- client (ManyToOne)
- orderItems (OneToMany)

#### **OrderItem**
- id, brand, quantity, price, observation
- order (ManyToOne)
- product (ManyToOne)
- color (ManyToOne)

#### **Audit**
- id, description, changeDate
- createdAt, updatedAt

---

## 🤝 Contribuindo

Este é um projeto acadêmico, mas contribuições são bem-vindas!

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos na FATEC Mogi das Cruzes.

---

## 👥 Autores


- Bianca Nunes Codo - RA: 1840482412013
- Diogo Santana de Almeida - RA: 1840482412001
- Felipe Kenji Oizumi - RA: 1840482412024
- João Paulo Akira Sigue - RA: 1840482412005
- Luciano Akihiro Tokuno - RA: 1840482412017
- Luana Mika Maruyama - RA: 1840482412016
- Marcos Guilherme Tasato - RA: 1840482412006

**Projeto desenvolvido por alunos do curso de Análise e Desenvolvimento de Sistemas**  
Faculdade de Tecnologia de Mogi das Cruzes - FATEC

---


## 🎓 Aprendizados

Este projeto permitiu aplicar na prática conceitos fundamentais de Engenharia de Software:

- ✅ Padrões de Projeto (Singleton, Abstract Factory)
- ✅ Princípios SOLID
- ✅ Arquitetura em Camadas
- ✅ RESTful API Design
- ✅ Autenticação e Autorização
- ✅ Persistência de Dados
- ✅ Tratamento de Exceções
- ✅ Versionamento com Git

---

**Desenvolvido com ☕ na FATEC Mogi das Cruzes**
