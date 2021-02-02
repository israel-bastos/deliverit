# Java Pleno - Deliverit Springboot App.
Desenvolver um Serviço Rest para tratar as regras de negócio descritas no PDF com o desafio técnico.

## Getting Started
Após configurar um projeto Springboot obedecendo a versão descrita nos pré requisitos, você irá instalar o docker na sua máquina e fazer o download de uma imagem do banco de dados utilizando o comando: docker run <nome da imagem>. 

Procurar o arquivo docker-compose.yml e executar o comando: docker-compose up no caminho onde seu arquivo está salvo.  

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
Postman Community

## Running the API Rest tests
	. Path: api/v1/contas
	
	Métodos HTTP:
	. GET: api/v1/contas/ (Trazer todas as contas)
	. GET: api/v1/contas/ {id} (Trazer uma conta específica)
	. GET:	 api/v1/contas/search? (Trazer uma conta específica por número da conta)
	. POST: api/v1/contas/(Body Json)
	. PUT: api/v1/contas (Body Json)
	. DELETE: api/v1/contas/{id}

## Running the tests
    . Reppository: você ira precisar que o docker esteja rodando.
    . Controller, Service: você consegue executar sem a necessidade do docker está iniciado.
    . Integration: Você vai precisar estar com o docker rodando e digitar o comando no terminal mvn-test OU executar por dentro do eclipse.

### Break down into end to end tests
    . Reppository: Testando a camada de persistência que acessa o banco de dados para realizar as operações CRUD básicos.
    . Controller: Testando os acessos aos endpoints da aplicação.
    . Service: Testando as regras de negócio da aplicação.
    . Integration: Testando a integração de todo o sistema.

## Authors
* **Israel Bastos** - *Deliverit* - [israel-bastos](https://github.com/israel-bastos)

## License
GNU Lesser General Public License v2.1  - see the [LICENSE.md](LICENSE) file for details