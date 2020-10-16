# Teste Prático API de Pedidos

Teste prático consistindo em uma API de pedidos.

O cliente pode escolher entre lanches já montados, ou informar quais ingredientes deseja.

## Detalhes

Tecnologias utilizadas:

```bash
    * Spring Boot 2.4.0;
    * MariaDB 10.5.6;
    * Swagger 2.9.2;
    * Docker;
    * Docker-Compose;
```

## Como executar

* Arquivo env:
    * A primeira coisa a ser feita, é configurar o arquivo /env/config.env" com as informações do ambiente de execução (banco de dados e JWT).
    * A pasta /teste/env/ tem o arquivo "config.env.example. Basta renomeá-lo para config.env e preencher seus campos.
    * Criar a rede que ligará a API ao banco de dados: `docker network create backend`.

Depois, basta acessar a pasta /teste e rodar o comando `docker-compose up -d`. Para isso, você deve ter o Docker-Compose instalado em sua máquina.

**Caso a configuração automática do docker-compose falhe, execute as instruções abaixo, como se você não tivesse o docker-compose instalado na máquina.**
**<br>São muitas dependências e o docker precisar baixar todas, podendo ocorrer algum timeout no processo. <br>Se isso acontecer, você ainda pode dar build manual na imagem, e ativar o parâmetro "image" no arquivo docker-compose.yml (é importante remover o parâmetro "context", se você fizer isso).**
**<br>Por fim, é só rodar o comando `docker-compose up -d` novamente.** 

### Se o docker-compose não estiver disponível, você deve:

1. Acesar a pasta /teste e executar o comando `docker build api-restaurant-image . ` (o ponto final é importante! Ele diz ao docker a localização do Dockerfile - no caso, o diretório /teste);
1. Iniciar um container com a imagem criada acima: `docker run -d --name api-restaurant --env_file=/env/config.env -p 8000:8000 api-restaurant-image` (ainda dentro da pasta /teste).
1. Criar um container do MariaDB, usando as configs do arquivo env: `docker run -d --name db --env_file=/env/config.env mariadb:10.5.6`;

### Importante!
**Lembre-se de criar a rede "backend": `docker network create backend`.**