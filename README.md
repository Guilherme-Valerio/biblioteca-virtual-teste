# 📚 Biblioteca Virtual

Um sistema de gerenciamento de biblioteca desenvolvido em **Spring Boot**.  
Permite cadastro e autenticação de usuários, além do CRUD completo de livros.

---

## 🚀 Tecnologias
- Java 17+
- Spring Boot 3
- Spring Web
- Spring Security
- Spring Data JPA
- H2 Database (para testes locais)

---

## ⚙️ Como rodar o projeto

### 1. Clonar o repositório
```bash
git clone [https://github.com/seu-usuario/biblioteca-virtual.git](https://github.com/seu-usuario/biblioteca-virtual.git)
cd biblioteca-virtual
```

### 2. Rodar com Maven
Instale o Maven em sua máquina e cheque com o comando:  mvn -v
Após isso rode abaixo:
```
mvn spring-boot:run
```

O servidor subirá em:
👉 http://localhost:8080

## 📌 Endpoints principais

##Usuários
```
POST /usuarios/cadastro → Cadastrar novo usuário
POST /usuarios/login → Login de usuário
GET /usuarios → Listar todos os usuários
PUT /usuarios/{id} → Atualizar usuário
PUT /usuarios/{id}/redefinir-senha → Redefinir senha
DELETE /usuarios/{id} → Remover usuário
```

##Livros
```
POST /livros → Cadastrar livro
GET /livros → Listar livros
GET /livros/{id} → Buscar livro por ID
GET /livros/pesquisa?titulo=NOME_DO_LIVRO_AQUI
PUT /livros/{id} → Atualizar livro
DELETE /livros/{id} → Deletar livro
```

🧪 Testando no Postman
Importe os endpoints acima no Postman.
Se a segurança estiver habilitada, use Basic Auth com email e senha cadastrados.
Caso contrário, os endpoints estarão abertos (permitAll).
