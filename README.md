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

Na pasta raiz (`"/simple-restaurant-rest-api/"`), basta rodar o comando `docker-compose up -d`. Para isso, você deve ter o Docker-Compose instalado em sua máquina.

**Caso a configuração automática do docker-compose falhe, execute as instruções abaixo, como se você não tivesse o docker-compose instalado na máquina.**

### Se o docker-compose não estiver disponível, você deve:<br>

#### **\*ATENÇÃO: O PRIMEIRO PASSO DEVE SER EXECUTADO APENAS SE VOCÊ QUISER FAZER NOVO BUILD DA IMAGEM. <br>SENÃO, PULE PARA O SEGUNDO (2º) PASSO.<br><br>**
1. Acessar a pasta raiz do projeto (`"/simple-restaurant-rest-api/"`) e executar o comando `docker build -t api-restaurant-image . ` (o ponto final é importante! Ele diz ao docker a localização do Dockerfile - no caso, o diretório `"/simple-restaurant-rest-api/"`);
1. Iniciar um container com a imagem criada acima, ou com a padrão `llucasbrandao/images:api-restaurant-image`, **ainda dentro da pasta raiz** `/simple-restaurant-rest-api/`: 
```bash
    docker run -d --name api-restaurant --env-file ./env/config.env -p 8000:8000 llucasbrandao/images:api-restaurant-image
``` 

3. Criar um container do MariaDB, usando as configs do arquivo env: `docker run -d --name db --env-file ./env/config.env mariadb:10.5.6`;
4. Atualizar o arquivo `"/simple-restaurant-rest-api/env/config.env"` com os dados do banco de dados criado (novo nome ou IP do banco, por exemplo);
5. Certificar-se de que ambos o DB e a API estão na mesma rede;

### Importante!
- **Lembre-se de criar a rede "backend", ou com o nome de sua escolha: `docker network create backend`, no caso de execução sem o Docker-Compose.**<br>
- **Pode ser que as configurações de rede da máquina não liberem acesso pelo localhost. Assim, é necessário que você inspecione o container e tente acessar pelo IP dele.**<br>
- **A porta de execução padrão é a 8080.**
- **O arquivo "/simple-restaurant-rest-api/env/config.env" está incluído no .gitignore, por questões de segurança. Somente o "config.env.example" deve ser "commitado".**
- **No arquivo config.env, o padrão deve ser CHAVE=VALOR, sem espaços ou aspas.**

## IDE's

Se quiser executar o sistema a partir de alguma IDE, certifique-se de configurar as informações do arquivo `"/simple-restaurant-rest-api/src/main/resources/application.properties"` manualmente.<br>
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

**Se você não quiser os dados falsos, comente ou remova o conteúdo do arquivo "DBService.java":**

```bash
    /simple-restaurant-rest-api/src/main/java/com/lucasbrandao/restaurantapi/services/DBService.java
```

**e remova os registros falsos do banco, nas tabelas users e ingredients.**

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

Depois, é só clicar no botão "Authorize" no Swagger, e inserir o token no campo disponibilizado (lembre-se de adicionar "Bearer" antes do JWT).

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

## Pedidos

Endpoint: `/api/v1/orders/new`;<br>
Para fazer um novo pedido, o corpo da requisição **POST** deve ser o seguinte:

- Com ingredientes escolhidos:

```json
{
    "ingredients": [ // Array de ingredientes
        {
            "id": 1, // ID do ingrediente desejado
            "quantity": 2 // Quantidade
        },
        {
            "id": 2, // ID do ingrediente desejado
            "quantity": 1 // Quantidade
        }
    ]
}
```

- Com lanches prontos:

```json
{
    "snack": "string", // Nome do lanche
    "snack_qnt": 0 // Quantidade
}
```

Quando o pedido for de um lanche, o retorno, em caso de sucesso da operação, será:

```json
{
    "message": "ID do pedido",
    "status": "OK"
}
```

**<br>Os lanches prontos estão hard-coded no arquivo `/simple-restaurant-rest-api/src/main/java/com/lucasbrandao/restaurantapi/services/SnacksService.java`, método `generateDummySnacks()`.**

**Se você fizer o pedido com algum lanche pronto, os ingredientes individuais serão ignorados.**
<br>O cálculo do preço e do desconto será feito com base nos ingredientes, mesmo nos lanches prontos. **<br>O desconto, quando for o caso, poderá ser cumulativo com os descontos que já tenham sido aplicados. Essa configuração é definida no campo "IS_CUMMULATIVE_DISCOUNT=true or false"**, do arquivo `"/simple-restaurant-rest-api/env/config.env"`.
<br>O default desse parâmetro é false.

### Promoções

**Quando uma promoção é aplicada**, o JSON devolvido terá o campo "order_offers":

```json
/api/v1/orders/getByID?id=6

{
    "message": {
        "id": 6,
        "user": {
            "id": 2,
            "role": [
                "ROLE_USER"
            ],
            "firstName": "Usuário",
            "lastName": "User",
            "email": "user@user.com",
            "birthday": 1603063722000
        },
        "total_due": 2.16,
        "discount": 0.24,
        "original_price": 2.4,
        "created_at": 1603072850000,
        "order_ingredients": [
            {
                "quantity": "2",
                "ingredient_name": "Alface",
                "price": "0.4"
            }
        ],
        "order_offers": [
            {
                "Light": "Se tem alface e não tem bacon, 10% de desconto"
            }
        ]
    },
    "status": "OK"
```

**<br>Promoções estão hard-coded no arquivo `/simple-restaurant-rest-api/src/main/java/com/lucasbrandao/restaurantapi/services/OffersService.java`, método `applyDiscount()`.**