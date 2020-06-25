# Projeto Weather

Projeto para integração com parceiro para pagamento de débitos de veículos na Zapay

## Getting Started
### Pré-requisitos
- Java 11
- Gradle
- Banco Mysql

### Rodando localmente

- Faça a instalação do banco de dados . Crie um BD e um user. As informações do BD podem ser encontradas no application-local.yml 

- Crie uma variável de ambiente com o nome spring.profiles.active e coloque no valor da variável a palavra `local`. Ou coloque essa variável de ambiente como argumento na JVM: -Dspring.profiles.active=local

- Faça a build do projeto: 
    - Utilizando Gradle Wrapper (no terminal), na pasta principal do projeto: 
    ```bash
    ./gradlew clean build
    ```  
    - Utilizando o Gradle instalado localmente:
     ```bash
     gradle clean build
     ```  
    - Utilizando a IDE de sua preferência (no Intellij, vá em Run... Edit Configurations)
    
- Inicialize o projeto com `gradle bootRun` ou usando a IDE de sua preferência

- O projeto é somente backend. Para fazer testes nos endpoints utilize uma ferramenta externa como Postman ou Insonnia
 
## Dúvidas do projeto

* [Adão Amaral](adao@exactaworks.com.br) - adao@exactaworks.com.br

### Algumas tecnologias utilizadas
- Java 11
- Flyway
- Webflux
- Spring boot
- JDBI

## Endpoint mapeado

-> Exibe previsão do tempo para os próximos 4 dias para uma cidade específica
- GET - http://localhost:9090/weather/weather/{codCidade}
- Exemplo = http://localhost:9090/weather/weather/255
- Sem restrições de autorização

## Api pública

INPE - Previsões de tempo - http://servicos.cptec.inpe.br/XML/

## Atividades

##### Atividade 1

Criar um novo endpoint para ver previsão dos próximos 7 dias. Salvar no banco como ocorre com o de 4 dias.

Detalhes desse endpoint estão no link da api pública do INPE.
   
##### Atividade 2

Alterar os endpoints para permitir que seja passado o nome da cidade ao invés do código

Detalhes desse endpoint estão no link da api pública do INPE.

Validar o nome da cidade passado. Usar melhores práticas. Colocar como query string? Ou mudar o método para POST? 
Que validações colocar? Onde? Que erro retornar?

##### Atividade 3

Existe um endpoint do INPE para ver condições de previsão de ondas para as cidades litorâneas (dia atual, manhã, tarde e noite)

Criar um endpoint para ver essas previsões de ondas. Detalhes desse endpoint estão no link da api pública do INPE.

Esse endpoint deve seguir o modelo dos outros endpoints, inclusive salvando no banco.

##### Atividade 4

Criar um CRUD para permitir que nós mesmo cadastremos ou atualizamos informações metereológicas no banco.
- findAllWeather
- findOneWeather
- postWeather
- putWeather
- deleteWeather

##### Atividade 5

Hoje o endpoint weather/{codCidade} funciona com o seguinte fluxo:

   2. Chama-se o endpoint.
   2. A aplicação recebe a cidade, e bate no parceiro INPE para obter as previsões de tempo
   2. O sistema transforma os dados recebidos em uma Entidade
   2. O sistema salva a entidade
   2. O sistema transforma o retorno do save em uma Resposta
   2. O sistema devolve a resposta com a previsão para os próximos 4 dias a partir da data atual
   
Dessa forma, se uma nova busca for realizada, essa previsão será salva novamente. Os dados são iguais. O ideal seria que, 
se existe previsão para aquela data em específico, o sistema  nem chamasse o parceiro. Ficaria então da seguinte forma:

   3. Chama-se o endpoint /weather/{codCidade}
   3. Verificamos se existe registro no banco para a data dos próximos 4 dias
   3. Se todas as datas encontram-se no banco, já retornar os dados encontrados para o controller. 
   3. Se uma ou mais datas não estão no banco, bate no parceiro INPE para obter as previsões de tempo para as datas que faltam
   3. O sistema transforma os dados recebidos em uma Entidade
   3. O sistema salva as entidades não encontradas no banco
   3. O sistema transforma todas as datas para os próximos 4 dias em uma Resposta
   3. O sistema devolve a resposta com a previsão para os próximos 4 dias a partir da data atual
      
##### Atividade 6

Preencher o openapi.yaml desse projeto. Ele fica localizado na pasta /docs do projeto

##### Atividade 7

No endpoint findAllWeather, criar um filtro pra fazer a busca por data, temperatura minima, temperatura máxima ou qualquer
combinação dessas. No endpoint de deleteWeather usar soft Delete.

##### Atividade 8

Criar os campos createdAt e updatedAt na entidade de Weather. O primeiro campo deve ser atualizado sempre que um registro
nessa tabela é inserido. O segundo campo deve ser atualizado sempre que um registro na tabela é atualizado.

#####Atividade 9

Criar testes unitários usando JUNIT e/ou Mockito.

Exemplo de um teste simples de um fluxo com webflux:

```java
when(repository.save(any())).thenReturn(Flux.just(mockedEntity()));
StepVerifier.create(famousService.save(mockedEntity()))
        .expectComplete()
        .verify(); 
```

#### Observações
Criar mensagens de erro adequadas

Criar exceções quando necessário

Usar classes de mapper sempre que possível pra reduzir a complexidade do código

Usar o analyze e o sonarLint do intellij para ajudar com boa estruturação de código

