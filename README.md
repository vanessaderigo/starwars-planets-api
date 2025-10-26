<h1 align="center">
  StarWars Planets API
</h1>

## Projeto

Serviço web que disponibiliza informações sobre os planetas do universo Star Wars.
Elaborado durante o curso [Testes automatizados na prática com Spring Boot](https://www.udemy.com/course/testes-automatizados-na-pratica-com-spring-boot/?referralCode=7F6C5AA14AE558497FE0), com foco na criação de testes automatizados.


## Tecnologias Utilizadas

- Java
- Spring Boot
- Maven
- MySQL
- Docker & Docker Compose
- Prometheus
- JUnit 5, Mockito, AssertJ, Hamcrest
- Jacoco (cobertura de testes)
- Pitest (testes de mutação)

## Configuração

Antes de iniciar, é necessário possuir instalado:
- [Docker](https://docs.docker.com/get-started/get-docker/)

A aplicação utiliza variáveis de ambiente para definir as credenciais do banco de dados.
Crie um arquivo .env na raiz do projeto com o seguinte conteúdo:
```
MYSQL_ROOT_PASSWORD=xxxxx
MYSQL_USER_PASSWORD=xxxxx
```
Essas variáveis são utilizadas pelo `docker-compose.yml` para configurar os containers de banco e aplicação.

## Executando com Docker

Para iniciar toda a stack (API + MySQL + Prometheus), execute:
```
docker-compose up --build
```
Isso fará com que:
- O MySQL suba com a base starwars já criada;
- A API seja construída e inicializada na porta 8080;
- O Prometheus seja iniciado na porta 9090, coletando métricas da aplicação.

Após o build:
- API disponível em: http://localhost:8080
- Prometheus disponível em: http://localhost:9090

## Monitoramento com Prometheus

A aplicação expõe métricas compatíveis com o Prometheus no endpoint: 
```
http://localhost:8080/actuator/prometheus
```
Essas métricas podem ser visualizadas e consultadas através da interface web do Prometheus em `http://localhost:9090`.

## Testes
Para rodar os testes localmente (sem Docker), execute:
```
./mvnw clean verify
```
