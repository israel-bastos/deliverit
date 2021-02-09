# Rest API usando Springboot.
Aguardando defininição.

## Code Techniques
    . DDD
    . TDD
    . SOLID
    . CLEAN CODE
    . API REST

### Prerequisites
Springboot 2.3.8.RELEASE
Java 11
Eclipse Oxygen
Maven
JPA 	(Associado ao parent do Springboot starter)
MySQL 	(Associado ao parent do Springboot starter)
JUnit 5
Docker

## Running the tests
    . Repository: você ira precisar que o docker esteja rodando.
    . Controller, Service: você consegue executar sem a necessidade do docker está iniciado.
    . Integration: Você vai precisar estar com o docker rodando e digitar o comando no terminal mvn-test OU executar por dentro do eclipse.

### Break down into end to end tests
    . Repository: Testando a camada de persistência que acessa o banco de dados para realizar as operações CRUD básicos.
    . Controller: Testando os acessos aos endpoints da aplicação.
    . Service: Testando as regras de negócio da aplicação.
    . Integration: Testando a integração de todo o sistema.

## Authors
* **Israel Bastos** - *[israel-bastos]* --> (https://github.com/israel-bastos)

## License
GNU Lesser General Public License v2.1  - see the [LICENSE.md](LICENSE) file for details
