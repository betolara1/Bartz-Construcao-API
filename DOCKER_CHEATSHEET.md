# 🐳 Guia de Sobrevivência Docker - Site Construtor API

Este guia resume os principais comandos e conceitos para gerenciar sua aplicação com Docker.

---

## 🛠️ Conceitos Rápidos

*   **Dockerfile:** É a "receita" para empacotar sua API Java em uma imagem linux isolada.
*   **Docker Compose:** É o "maestro" que coordena a API e o Banco de Dados PostgreSQL para que trabalhem juntos.
*   **Imagem:** O pacote estático (o "instalador").
*   **Container:** A imagem em execução (o "processo").

---

## 🚀 Comandos Essenciais (Docker Compose)

Sempre execute esses comandos na raiz do projeto (onde está o arquivo `docker-compose.yml`).

### 1. Iniciar o Sistema
```bash
docker-compose up -d
```
*   **Uso:** Primeira vez que for rodar ou após ligar o PC.
*   **O que faz:** Cria as redes, o banco de dados e constrói a imagem da sua API.
*   **Flag `-d`:** Roda em segundo plano (libera o terminal).

### 2. Atualizar após Alterações no Código
```bash
docker-compose up -d --build
```
*   **Uso:** Sempre que você mudar o código Java (`src/`) ou o `pom.xml`.
*   **Por que?** Força o Docker a ler seu Dockerfile de novo e gerar uma nova versão da imagem da API.

### 3. Verificar Logs (O console da aplicação)
```bash
# Ver logs da API
docker-compose logs -f api

# Ver logs de todos os serviços (API + Banco)
docker-compose logs -f
```
*   **Uso:** Para debugar erros de inicialização ou ver os prints do Spring Boot.

### 4. Parar a Aplicação
```bash
# Apenas para (mantém os containers)
docker-compose stop

# Para e remove (limpa os containers da lista)
docker-compose down
```

### 5. Reset Total (CUIDADO ⚠️)
```bash
docker-compose down -v
```
*   **Uso:** Quando você quer apagar todos os dados do banco de dados e começar do zero.
*   **O que faz:** Remove containers, redes e **apaga o volume de dados**.

---

## 📝 Ciclo de Trabalho Diário

1.  Abra o **Docker Desktop**.
2.  Ligue tudo: `docker-compose up -d`.
3.  Confira se subiu ok: `docker-compose logs -f api`.
4.  Codifique seu Java na IDE.
5.  Quer testar no Docker? `docker-compose up -d --build`.
6.  Terminou o dia? `docker-compose down`.

---

## 💡 Dicas de Ouro

*   **Arquivo .dockerignore:** Garante que o build seja rápido, ignorando pastas inúteis como `target/` e `.git`.
*   **Rede Interna:** No Docker, a API conversa com o banco usando o host `db` (nome definido no compose), e não `localhost`.
*   **Persistência:** O banco de dados salva as informações na pasta de volumes do Docker, então elas não somem ao dar `stop` ou `down` (a menos que use o `-v`).
