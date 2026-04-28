<div align="center">

# 🏗️ Bartz Construção API

### REST API de Gerenciamento de Produtos para o Site de Construção

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI_3-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://swagger.io/)

</div>

---

## 📌 Sobre o Projeto

A **Bartz Construção API** é o backend do site institucional de uma construtora, responsável por gerenciar todo o catálogo de produtos e materiais exibidos ao cliente. A API fornece endpoints RESTful completos para que o frontend do site consuma e apresente os produtos de forma dinâmica, filtrada e paginada.

Construída com foco em **produção real**, a API incorpora:

- ✅ **Arquitetura em camadas** (Controller → Service → Repository → Model)
- ✅ **Segurança com JWT** via biblioteca customizada reutilizável
- ✅ **Migrações de banco de dados** gerenciadas com Flyway
- ✅ **Containerização completa** com Docker e Docker Compose
- ✅ **Documentação interativa** com Swagger / OpenAPI 3
- ✅ **Suíte de testes robusta** com JUnit 5 e Mockito
- ✅ **Monitoramento de saúde** com Spring Boot Actuator

---

## 🏛️ Arquitetura

```
📦 SiteContrutorAPI
 ├── 🔐 controller/          # Endpoints REST (AuthController, ProductController, ...)
 ├── 🧩 service/             # Regras de negócio e lógica de aplicação
 ├── 🗄️ repository/          # Interfaces JPA para acesso ao banco de dados
 ├── 🏷️ model/               # Entidades JPA mapeadas para o PostgreSQL
 ├── 📤 dto/                 # Data Transfer Objects (request e response)
 ├── ⚠️ exception/           # Tratamento global de erros (GlobalHandlerException)
 ├── ⚙️ config/              # Configurações de Segurança, CORS e Swagger
 └── 🛠️ util/                # Utilitários (paginação, ordenação)
```

### Modelo de Dados

```
LocalToPut (Local)
    └── Product (Produto)
            ├── ModuleFather (Módulo Pai)
            │       └── ModuleChild (Módulo Filho)
            └── Size (Dimensões: altura, largura, profundidade)
```

> Os produtos são organizados hierarquicamente por **local de instalação** e **módulo**, permitindo filtros precisos para a experiência do usuário no site.

---

## 🚀 Endpoints da API

### 🔐 Autenticação
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `POST` | `/auth/login` | Gera token JWT | ❌ Público |

### 📦 Produtos
| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| `GET` | `/products` | Lista todos (paginado) | ✅ |
| `GET` | `/products/{id}` | Busca por ID | ✅ |
| `GET` | `/products?type=` | Filtra por tipo | ✅ |
| `GET` | `/products?name=` | Filtra por nome | ✅ |
| `GET` | `/products?local=` | Filtra por local | ✅ |
| `GET` | `/products?moduleFather=` | Filtra por módulo pai | ✅ |
| `GET` | `/products?moduleChild=` | Filtra por módulo filho | ✅ |
| `GET` | `/products?active=` | Filtra por status ativo | ✅ |
| `GET` | `/products?dateCreated=` | Filtra por data de criação | ✅ |
| `POST` | `/products` | Cria novo produto | ✅ |
| `PUT` | `/products/{id}` | Atualiza produto | ✅ |
| `DELETE` | `/products/{id}` | Remove produto | ✅ |

### 📐 Dimensões (Sizes)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/sizes` | Lista todas as dimensões |
| `GET` | `/sizes/{id}` | Busca por ID |
| `POST` | `/sizes` | Cadastra dimensões |
| `PUT` | `/sizes/{id}` | Atualiza dimensões |
| `DELETE` | `/sizes/{id}` | Remove dimensões |

### 🗂️ Módulos & Locais
> Os mesmos padrões CRUD RESTful são aplicados para:
> - `/locals` — Locais de instalação
> - `/modules-father` — Módulos Pai
> - `/modules-child` — Módulos Filho

---

## 🔐 Segurança

A autenticação é baseada em **JWT (JSON Web Token)** implementada via uma **biblioteca customizada própria** (`jwt-package`), desenvolvida para ser reutilizável em múltiplos projetos do ecossistema.

```
POST /auth/login
Body: { "username": "admin", "password": "sua_senha" }
Response: "eyJhbGciOiJIUzI1NiJ9..."
```

Após obter o token, inclua no header de todas as requisições:
```
Authorization: Bearer <seu_token>
```

**Rotas públicas** (sem token):
- `POST /auth/login`
- `GET /swagger-ui/**`

---

## 🧪 Testes

A API possui cobertura de testes em **duas camadas**:

| Camada | Ferramenta | Classes de Teste |
|--------|------------|-----------------|
| **Service (Unit)** | JUnit 5 + Mockito | `ProductServiceTest`, `SizeServiceTest`, `ModuleFatherServiceTest`, `ModuleChildServiceTest`, `LocalToPutServiceTest` |
| **Controller (Integration)** | `@WebMvcTest` + MockMvc | `ProductControllerTest`, `SizeControllerTest`, `ModuleFatherControllerTest`, `ModuleChildControllerTest`, `LocalToPutControllerTest` |

```bash
# Executar todos os testes
./mvnw test
```

---

## 🐳 Rodando com Docker (Produção)

A forma mais simples de subir a API em qualquer ambiente:

**1. Configure as variáveis de ambiente:**
```bash
# Copie o arquivo de exemplo e preencha com seus dados
cp .env.example .env
```

```env
# .env
POSTGRES_DB=bartz_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha_segura
POSTGRES_PORT=5432
SERVER_PORT=8080

JWT_SECRET_KEY=sua_chave_secreta_com_pelo_menos_32_chars
JWT_EXPIRATION_TIME=86400000

USERNAME_LOGIN=admin
PASSWORD_LOGIN=sua_senha_bcrypt_hash
```

**2. Suba os containers:**
```bash
docker-compose up --build -d
```

**3. Verifique o status:**
```bash
docker-compose ps
docker-compose logs -f api
```

A API estará disponível em: `http://localhost:8080`

**Detalhes do Dockerfile:**
- 🏗️ **Multi-stage build** — imagem final leve com apenas o JRE
- 🔒 **Usuário não-root** — segurança reforçada no container
- 🌎 **Timezone configurado** — `America/Sao_Paulo`
- ⏳ **Health check no banco** — a API só inicia após o PostgreSQL estar saudável

---

## 💻 Rodando Localmente (Desenvolvimento)

**Pré-requisitos:**
- Java 21+
- Maven 3.9+
- PostgreSQL 15+ (ou Docker)

```bash
# Clone o repositório
git clone https://github.com/betolara1/Bartz-Construcao-API.git
cd SiteContrutorAPI

# Configure as variáveis de ambiente no arquivo .env
# (ou exporte no terminal)

# Execute a aplicação
./mvnw spring-boot:run
```

---

## 📖 Documentação Interativa (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Você terá acesso a **todos os endpoints documentados**, com possibilidade de testar diretamente pelo navegador, incluindo autenticação JWT.

---

## 📊 Monitoramento (Actuator)

Endpoints de saúde disponíveis:

| Endpoint | Descrição |
|----------|-----------|
| `GET /actuator/health` | Status da aplicação e do banco de dados |
| `GET /actuator/info` | Informações da aplicação |
| `GET /actuator/metrics` | Métricas de performance |

---

## 🗃️ Banco de Dados

- **Produção:** PostgreSQL 15
- **Testes:** H2 (in-memory)
- **Migrações:** Gerenciadas pelo **Flyway** — toda evolução do schema é versionada e rastreável

```sql
-- Estrutura principal (gerenciada pelo Flyway V1__Create_tables.sql)
local_to_put → module_father → module_child → products → sizes
```

---

## 🛠️ Stack Tecnológica

| Tecnologia | Versão | Finalidade |
|-----------|--------|------------|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.5 | Framework web e IoC |
| Spring Data JPA | — | ORM e persistência |
| Spring Security | — | Controle de acesso |
| JWT Package (custom) | 1.0.3 | Autenticação JWT própria |
| PostgreSQL | 15 | Banco de dados de produção |
| H2 | — | Banco em memória para testes |
| Flyway | — | Migrações de banco de dados |
| Lombok | — | Redução de boilerplate |
| SpringDoc OpenAPI | 2.8 | Documentação Swagger |
| Spring Actuator | — | Monitoramento e health check |
| Docker + Compose | — | Containerização e deploy |
| JUnit 5 + Mockito | — | Testes unitários e de integração |
| Maven | 3.9 | Gerenciamento de build |

---

## 👨‍💻 Autor

Desenvolvido por **Beto Lara** — Backend Developer

[![GitHub](https://img.shields.io/badge/GitHub-betolara1-181717?style=for-the-badge&logo=github)](https://github.com/betolara1)

---

<div align="center">

**Bartz Construção API** — Construído para produção, pensado para escalar.

</div>
