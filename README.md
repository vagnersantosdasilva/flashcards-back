## Memobeam - Flashcard App with Spaced Repetition

**Memobeam** é uma ferramenta de aprendizagem que auxilia você a estudar usando flashcards e revisões espaçadas. Ele permite que você crie flashcards, categorize-os e revise-os de forma otimizada, ajudando na memorização a longo prazo.

### Tecnologias utilizadas

* **Backend:** Spring Boot
* **Banco de dados:** MySQL (com suporte a Flyway para migrações)
* **Segurança:** Spring Security
* **Documentação:** Springdoc OpenAPI
* **Outros:** Lombok (opcional), Java JWT, Hibernate

### Pré-requisitos

* Java 17+
* MySQL

### Instalação

**1. Clone o repositório**

```bash
git clone https://github.com/vagnersantosdasilva/flashcards-back.git memobeam
cd memobeam
```

**2. Instale as dependências**

```bash
mvn clean install
```

**3. Configure o banco de dados**

Você precisará criar um arquivo `application.properties` na raiz do projeto e configurar as credenciais de acesso ao seu banco de dados MySQL. Veja um exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/memobeam
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

**4. Inicie a aplicação**

```bash
mvn spring-boot:run
```

**5. Acesso à API**

A API estará disponível na porta `8080` por padrão. Você pode utilizar ferramentas como Postman para testar os endpoints da API. A documentação da API estará disponível em `http://localhost:8080/swagger-ui/`.

### Contribuindo

Sinta-se à vontade para contribuir com o projeto! Abra um pull request com suas alterações e melhorias.

### Licença

**MIT**

## Próximos passos

* Consulte a documentação da API para saber como interagir com o sistema.
* Explore o código fonte para entender a implementação da funcionalidade.

Este README.md fornece uma visão geral do Memobeam. Para mais detalhes, consulte a documentação da API e o código fonte do projeto.
