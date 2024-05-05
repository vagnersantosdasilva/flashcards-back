## Memobeam - Flashcard App with Spaced Repetition

**Memobeam** é uma ferramenta de aprendizagem que auxilia você a estudar usando flashcards e revisões espaçadas. 
Ela permite que você crie flashcards, categorize-os e revise-os de forma otimizada, ajudando na memorização a longo prazo.

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
**3. Build da aplicação**
```bash
mvn package
```
**4. Inicie a aplicação**
```bash
mvn spring-boot:run

```
- Observação importante: Repare que nos arquivos de proprieade 'application.. .yml' existem diversas variáveis de ambiente 
- Para que a aplicação funcione corretamente você deve definir esses valores no S.O que servirá de base ou no ambiente 
de desenvolvimento que você utiliza para fazer edições.

**5. Acesso à API**

A API estará disponível na porta `8082` por padrão. Você pode utilizar ferramentas como Postman para testar os endpoints da API. 
A documentação da API estará disponível em `http://localhost:8082/swagger-ui/index.html`.


### Licença

**MIT**

## Próximos passos

* Consulte a documentação da API para saber como interagir com o sistema.
* Explore o código fonte para entender a implementação da funcionalidade.

Este README.md fornece uma visão geral do Memobeam. Para mais detalhes, consulte a documentação da API e o código fonte do projeto.
