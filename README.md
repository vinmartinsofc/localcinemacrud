# LocalCinemaCRUD

Aplicação de linha de comando em Java para gerenciar um acervo pessoal de filmes, séries e documentários, com controle do que já foi assistido (avaliação e comentário).

## Funcionalidades

- Cadastro de **Filmes**, **Séries** e **Documentários** (herdam de uma classe base `Title`)
- Listagem de todos os títulos
- Busca por id
- Busca por nome
- Remoção de título
- Registro de títulos assistidos (`WatchedTitle`) com data, nota e comentário
- Listagem e remoção de itens assistidos

## Tecnologias

- Java 17
- Maven
- PostgreSQL (via JDBC puro, sem ORM)

## Estrutura do projeto

```
com.localcinemacrud
├── model        # Title, Movie, Series, Documentary, WatchedTitle
├── repository   # TitleRepository, WatchedTitleRepository (acesso via JDBC)
└── service      # TitleService, WatchedService (regras/menu)
```

## Configuração

A conexão com o banco é feita via variáveis de ambiente:

| Variável      | Descrição                          |
|---------------|-------------------------------------|
| `DB_URL`      | URL de conexão JDBC do PostgreSQL   |
| `DB_USER`     | Usuário do banco                    |
| `DB_PASSWORD` | Senha do banco                      |

Exemplo:
```bash
export DB_URL=jdbc:postgresql://localhost:5432/localcinema
export DB_USER=postgres
export DB_PASSWORD=postgres
```

### Esquema do banco

```sql
CREATE TABLE titles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    release_date INTEGER,
    category VARCHAR(50),
    genre VARCHAR(100),
    director VARCHAR(100),
    duration INTEGER,
    creator VARCHAR(100),
    new_seasons BOOLEAN
);

CREATE TABLE watched_titles (
    id SERIAL PRIMARY KEY,
    title_id INTEGER REFERENCES titles(id),
    watched_date DATE NOT NULL,
    rating NUMERIC(3,1),
    comment TEXT
);
```


## Status

Projeto de estudo, com algumas partes (como a lógica de update e a camada de conexão JDBC) ainda em desenvolvimento/simplificadas.
