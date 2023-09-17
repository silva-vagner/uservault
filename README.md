# UserVault - Aplicação de Registro, Login e Gerenciamento de Usuários com Auditoria


UserVault é um projeto Java com Spring que oferece funcionalidades de autenticação e gerenciamento de usuários, juntamente com recursos de auditoria. Esta aplicação permite o acesso com três níveis de privilégio: usuário comum, administrador e auditor.

## Tópicos
- [Tecnologias e Recursos Destacados](#tecnologias-e-recursos-destacados)
- [Principais Funcionalidades](#funcionalidades-principais)
- [Roles de Usuário](#roles-de-usuario)
- [Pré-requisitos](#pre-requisitos)
- [Como Iniciar](#como-iniciar)
- [Uso com o Swagger](#uso-com-o-swagger)
- [Uso com o Postman](#uso-com-o-postman)
- [Acesso à Implantação e Deploy da Aplicação](#acesso-e-deploy)
- [Usuários de Teste](#usuarios-de-teste)
##
### Tecnologias e Recursos Destacados <a name="tecnologias-e-recursos-destacados"></a>

- **Spring Security**: Controle de acesso e autenticação.

- **JWT (JSON Web Tokens)**: Autenticação segura.
- **Spring Data**: Implementação de auditoria.
- **JPA (Java Persistence API)**: Consultas personalizadas.
- **Flyway**: Controle de versionamento de migrações de banco de dados.
- **Manipulação de Exceptions Customizadas**: Melhor gerenciamento de erros.
- **Testes Unitários com JUnit e Mockito**: Garantia de qualidade de código.
- **Docker/Docker Compose**: Implantação em contêineres.
- **Documentação Detalhada com Postman e Swagger**: Facilidade de uso e teste.<br>
- **Banco de Dados PostgreSQL**: Armazenamento robusto de dados.

##
### Principais Funcionalidades <a name="funcionalidades-principais"></a>

- **Registro de Usuário**: Os usuários podem se cadastrar fornecendo informações básicas, como nome de usuário, senha e email.

- **Login**: Os usuários podem fazer login com suas credenciais registradas para acessar suas contas.

- **Gestão de Usuários**: Exclusivo para administradores, esta funcionalidade oferece um controle completo sobre a administração de contas de usuário. Os administradores têm a capacidade de criar novas contas de usuário, realizar edições em informações existentes e, quando necessário, efetuar a exclusão de contas. A estrutura de permissões garante que cada tipo de usuário tenha acesso somente aos recursos relevantes ao seu papel, mantendo a integridade e a segurança dos dados do sistema.

- **Auditoria**: A aplicação registra diversas as ações críticas, como modificações de perfil e atividades de administrador, permitindo uma trilha de auditoria das principais operações.
##
### Roles de Usuário <a name="roles-de-usuario"></a>

O Projeto suporta três roles de usuário:

1. **Usuário (User)**: Usuários comuns que podem se registrar, fazer login e acessar suas contas. Eles têm acesso limitado às funcionalidades do sistema.

2. **Administrador (Admin)**: Administradores têm controle total sobre a gestão de usuários e podem realizar operações privilegiadas.

3. **Auditor (Auditor)**: Auditores podem selecionar entre informações de usuários ou administradores para visualizar registros detalhados de atividades no sistema.
##
### Pré-requisitos <a name="pre-requisitos"></a>

- Docker: Certifique-se de ter o Docker instalado em sua máquina. Acesse o link para baixá-lo e instalá-lo [site oficial do Docker](https://www.docker.com/get-started/). 
##
### Como Iniciar <a name="como-iniciar"></a>

1. Clone este repositório:

   ```shell
   git clone https://github.com/silva-vagner/UserVault.git
   ``````

2. Navegue até o diretório do projeto:

   ```shell
   cd UserVault
   ```

3. Execute o Docker Compose para iniciar o aplicativo e o banco de dados:

    ```
    docker-compose up -d
    ```
##
### Uso com o Swagger<a name="uso-com-o-swagger"></a>
Após a inicialização bem-sucedida dos contêineres, acesse a interface do Swagger em seu navegador:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

Para acessar os endpoints protegidos, obtenha um token JWT válido:

- Use a solicitação de autenticação no Swagger, "POST /auth/login", para obter um token JWT.

- Insira o token JWT no Swagger clicando em "Authorize" (Autorizar) no canto superior direito e preenchendo o campo.

- Use a interface do Swagger para explorar e interagir com os endpoints da API, experimente as funcionalidades e teste os recursos disponíveis.
##
### Uso com o Postman <a name="uso-com-o-postman"></a>
Para facilitar ainda mais o teste da API, você pode importar a coleção do Postman que inclui solicitações pré-configuradas para todas as funcionalidades. Siga estas etapas:

- Baixe a coleção do Postman aqui: UserVault API Collection.
[Download da Coleção do Postman](https://github.com/silva-vagner/uservault/blob/6ca8316a52972b8c13f4a7a9167b1627e78b67ab/postman/UserVault.postman_collection.json)


- Abra o Postman e clique em "Import" (Importar) na parte superior esquerda.

- Selecione o arquivo da coleção que foi baixado.

- Execute a solicitação na pasta de autenticação para obter um token JWT, inserindo os detalhes de autenticação (nome de usuário e senha).

Não é necessário atualizar as requisições com o Token JWT retornado, o arquivo possui um script que automatiza esse processo.
##

### Acesso à Implantação e Deploy da Aplicação <a name="acesso-e-deploy"></a>

A versão mais recente deste projeto foi implantada e está pronta para uso. Você pode acessar o deploy da aplicação no seguinte link:

[UserVault - Acesse o Deploy da Aplicação](https://user-vault-production.up.railway.app/swagger-ui/index.html)
##
### Usuários de Teste <a name="usuarios-de-teste"></a>
Os seguintes usuários de teste estão disponíveis para facilitar o teste das funcionalidades do sistema sem a necessidade de registro, eles são carregados no banco de dados quando a aplicação faz a primeira execução. Lembre-se que o usuário admin pode criar qualquer tipo de usuário.

- Usuário Comum (User)
```bash
Email: user@email
Senha: password
```

- Administrador (Admin)
```
Email: admin@email
Senha: password
```

- Auditor (Auditor)
```
Email: auditor@email
Senha: password
```


