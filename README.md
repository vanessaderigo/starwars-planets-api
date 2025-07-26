<h1 align="center">
  StarWars Planets API
</h1>

<p align="center">
  <a href="#-project">Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-technologies">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-configuration">Configuração</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-developing">Construir e Executar</a>
</p>

<br>

## Projeto

Serviço web que disponibiliza informações sobre os planetas do universo Star Wars.

Projeto foi elaborado durante o curso [Testes automatizados na prática com Spring Boot](https://www.udemy.com/course/testes-automatizados-na-pratica-com-spring-boot/?referralCode=7F6C5AA14AE558497FE0), em que o foco foi a criação de testes automatizados.


## Tecnologias Utilizadas

- MySQL
- Java
- Maven
- Spring Boot
- Spring Testing
- JUnit 5
- Mockito
- AssertJ
- Hamcrest
- Jacoco
- Pitest

## Configuração

O projeto requer um banco de dados Mysql, então é necessário criar uma base de dados com os seguintes comandos:

```
$ sudo mysql

CREATE USER 'user'@'%' IDENTIFIED BY '1234567';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%' WITH GRANT OPTION;

exit

$ mysql -u user -p

CREATE DATABASE starwars;

exit
```

Durante os testes, as tabelas de banco já serão criadas automaticamente no banco de dados.

## Como executar

Execute o comando:

```sh
$ ./mvnw clean verify
```
