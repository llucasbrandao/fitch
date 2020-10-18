# API de Pedidos

O cliente pode escolher entre lanches já montados, ou informar quais ingredientes deseja.

## Detalhes

Tecnologias utilizadas:

```bash
    * Spring Boot 2.4.0;
    * Java/OpenJDK 8;
    * MariaDB 10.5.6;
    * Swagger 2.9.2;
    * Docker;
    * Docker-Compose;
```

## Como executar

* A primeira coisa a ser feita, é configurar o arquivo ```"/fitch/env/config.env" ``` com as informações do ambiente de execução (banco de dados e JWT).
* A pasta /teste/env/ tem o arquivo `"config.env.example"`. Basta renomeá-lo para `config.env` e preencher seus campos.
* Criar a rede que ligará a API ao banco de dados: `docker network create backend`.

Depois, basta acessar a pasta /teste e rodar o comando `docker-compose up -d`. Para isso, você deve ter o Docker-Compose instalado em sua máquina.

**Caso a configuração automática do docker-compose falhe, execute as instruções abaixo, como se você não tivesse o docker-compose instalado na máquina.**
**<br>São muitas dependências e o docker precisar baixar todas, podendo ocorrer algum timeout no processo. <br>Se isso acontecer, você ainda pode dar build manual na imagem, e ativar o parâmetro "image" no arquivo docker-compose.yml (é importante remover o parâmetro "context", se você fizer isso).**
**<br>Por fim, é só rodar o comando `docker-compose up -d` novamente.** 

### Se o docker-compose não estiver disponível, você deve:

1. Acessar a pasta raiz do projeto (`"/fitch/"`) e executar o comando `docker build -t api-restaurant-image . ` (o ponto final é importante! Ele diz ao docker a localização do Dockerfile - no caso, o diretório `"/fitch/"`);
1. Iniciar um container com a imagem criada acima: `docker run -d --name api-restaurant --env_file=/env/config.env -p 8000:8000 api-restaurant-image` (ainda dentro da pasta raiz `/fitch/`);
1. Criar um container do MariaDB, usando as configs do arquivo env: `docker run -d --name db --env_file=/env/config.env mariadb:10.5.6`;
1. Atualizar o arquivo `"/fitch/env/config.env"` com os dados do banco de dados criado;

### Importante!
- **Lembre-se de criar a rede "backend": `docker network create backend`.**<br>
- **Pode ser que as configurações de rede da máquina não liberem acesso pelo localhost. Assim, é necessário que você inspecione o container e tente acessar pelo IP dele.**<br>
- **A porta de execução padrão é a 8080.**

## IDE's

Se quiser executar o sistema a partir de alguma IDE, certifique-se de que as informações do arquivo `"/fitch/env/config.env"` estão corretas.<br>
Feito isso, é só usar a IDE de sua preferência e **um banco de dados MariaDB/MySQL.**

## Fake Data
O sistema cria dados fake para testes. São criados dois usuários, sendo um deles admin:

```json
    {
        "email": "user@user.com",
        "password": 123456
    },
    {
        "email": "admin@admin.com",
        "password": 123456
    }
```

A senha é criptografada, no banco, seguindo o padrão da indústria.

Além disso, são gerados lanches e ingredientes:

```json
    INGREDIENTES:
        ● Alface: R$ 0.40
        ● Bacon: R$ 2,00
        ● Hambúrguer: R$ 3,00
        ● Ovo: R$ 0,80
        ● Queijo: R$ 1,50

    LANCHES:
        ● X-Bacon: Bacon, hambúrguer de carne e queijo
        ● X-Burger: Hambúrguer de carne e queijo
        ● X-Egg: Ovo, hambúrguer de carne e queijo
        ● X-Egg Bacon: Ovo, bacon, hambúrguer de carne e queijo
```

**Se você não quiser os dados falsos, comente o conteúdo da classe DBService.java:**

```bash
    /fitch/src/main/java/com/lucasbrandao/restaurantapi/services/DBService.java
```

**e remova os registros falsos do banco, na tabela users.**

## Swagger, Postman, Login & Endpoints
O Swagger pode ser acessado em [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

Você precisa ter um token de usuário ou administrador para buscar/inserir informações nos endpoints.

O token deve ser obtido fazendo uma requisição **POST** para [http://localhost:8080/login](http://localhost:8080/login).

O body deve ter o seguinte formato:

```json
    {
        "email": "email@email.com",
        "password": "password"
    }
```
**Se os dados forem válidos, o token será devolvido no header "Authorization" da response.**

Este processo pode ser feito pelo [Postman](https://postman.com).

Depois, é só clicar no botão "Authorize" no Swagger, e inserir o token no campo disponibilizado.

Todas as requisições para os endpoints mostrados no Swagger também podem ser feitas pelo Postman. <br>
Logo, você também deve mandar o token JWT no header "Authorization" da requisição:

```json
    Bearer: token
```

#### Endpoints & Login
- **Os Endpoints da aplicação estão disponíveis no Swagger.**<br>
- **Todos os endpoints relacionados a adição, edição e remoção de ingredientes requerem um token de administrador.**<br>

Caso você tente executar alguma operação e receba o erro:

```json
    Access denied. User is either not logged or is trying to perform a not allowed action
```

significa que o JWT enviado é de um usuário comum, e não de administrador.

<br>Ao tentar acessar qualquer endpoint **(exceto o Swagger-UI)** sem enviar o JWT no header da requisição, você receberá o erro:

```json
    Full authentication is required to access this resource
```

**Este erro também ocorrerá se, ao tentar fazer login, o usuário ou senha estiverem incorretos.**