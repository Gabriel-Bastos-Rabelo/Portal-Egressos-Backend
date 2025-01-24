# Portal Egressos Backend

Projeto desenvolvido para a disciplina de Laboratório de Programação.

---

## Pré-requisitos

- Docker e Docker Compose instalados

---

## Rodando a aplicação com Docker

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/Gabriel-Bastos-Rabelo/Portal-Egressos-Backend.git
   cd Portal-Egressos-Backend
   ```

2. **Configure o arquivo `.env`:**

   Crie um arquivo `.env` na raiz com as variáveis necessárias, por exemplo:

   ```env
   DATABASE_URL=jdbc:mysql://localhost:3306/mydb
   DATABASE_USERNAME=root
   DATABASE_PASSWORD=password123
   JWT_SECRET=your-secret-key
   ```

3. **Suba os containers:**

   ```bash
   docker-compose up --build
   ```

4. **Acesse a aplicação:**

   A aplicação estará disponível em:
   ```
   http://localhost:8080
   ```

5. **Parar a aplicação:**

   ```bash
   docker-compose down
   ```

---
  