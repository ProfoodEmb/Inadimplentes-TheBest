# 📦 Guia de Instalação - Projeto Inadimplência The Best

## 🎯 Sobre o Projeto

Este é um projeto Java que roda **dentro do Sankhya ERP** como uma **Ação Agendada**. Ele busca títulos vencidos de um cliente específico (The Best) e envia relatórios por e-mail automaticamente.

## ⚠️ Pré-requisitos

### 1. **Sankhya ERP**
- ✅ Você precisa ter o **Sankhya ERP instalado e configurado**
- ✅ Acesso ao banco de dados Oracle do Sankhya
- ✅ Permissões para criar Ações Agendadas no sistema

### 2. **Ferramentas de Desenvolvimento** (apenas para compilar)
- Java JDK 8 ou superior
- Maven ou Gradle (para gerenciar dependências)
- IDE Java (Eclipse, IntelliJ IDEA, NetBeans)

### 3. **Dependências do Sankhya**
Este projeto usa bibliotecas proprietárias do Sankhya:
- `jape.jar` - Framework de persistência do Sankhya
- `sankhya-core.jar` - Core do Sankhya
- `cuckoo.jar` - Framework de agendamento

**Nota:** Essas bibliotecas geralmente estão no diretório de instalação do Sankhya.

## 📥 Instalação

### Passo 1: Clonar o Projeto
```bash
git clone <url-do-repositorio>
cd <nome-do-projeto>
```

### Passo 2: Configurar Dependências

Você precisa criar um arquivo `pom.xml` (Maven) ou `build.gradle` (Gradle) para gerenciar as dependências.

#### Exemplo de `pom.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>br.inadThebest</groupId>
    <artifactId>inadimplencia-thebest</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Dependências do Sankhya (instalar localmente) -->
        <dependency>
            <groupId>br.com.sankhya</groupId>
            <artifactId>jape</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${sankhya.home}/lib/jape.jar</systemPath>
        </dependency>

        <dependency>
            <groupId>br.com.sankhya</groupId>
            <artifactId>sankhya-core</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${sankhya.home}/lib/sankhya-core.jar</systemPath>
        </dependency>

        <dependency>
            <groupId>org.cuckoo</groupId>
            <artifactId>cuckoo-core</artifactId>
            <version>1.0</version>
            <scope>system</scope>
            <systemPath>${sankhya.home}/lib/cuckoo.jar</systemPath>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
            </plugin>
        </plugins>
    </build>
</project>
```

### Passo 3: Compilar o Projeto

```bash
# Com Maven
mvn clean package

# Com Gradle
gradle build
```

Isso gerará um arquivo `.jar` na pasta `target/` ou `build/libs/`.

### Passo 4: Instalar no Sankhya

1. **Copiar o JAR** para o diretório de customizações do Sankhya:
   ```
   <SANKHYA_HOME>/repositorio/
   ```

2. **Acessar o Sankhya** e ir em:
   ```
   Menu > Administração > Ações Agendadas
   ```

3. **Criar Nova Ação Agendada:**
   - **Nome:** Inadimplência The Best
   - **Classe:** `br.inadThebest.AcAgNotiInadTheBest`
   - **Periodicidade:** Diária (ou conforme necessário)
   - **Horário:** Defina o horário de execução

4. **Ativar a Ação** e testar

## 🗄️ Banco de Dados

### Tabelas Utilizadas (Padrão Sankhya)
- **TGFFIN** - Financeiro (Títulos a Receber/Pagar)
- **TGFPAR** - Parceiros (Clientes/Fornecedores)
- **MSDFilaMensagem** - Fila de E-mails

**Importante:** Este projeto **NÃO cria tabelas**. Ele usa as tabelas padrão do Sankhya.

### Script SQL
Consulte o arquivo `database_schema.sql` para ver a consulta SQL utilizada.

## ⚙️ Configuração

### Alterar Cliente Monitorado
No arquivo `AcAgNotiInadTheBest.java`, linha 58:
```java
AND MAT.codparc = 3930  // Altere para o código do seu cliente
```

### Alterar E-mails de Destino
No arquivo `AcAgNotiInadTheBest.java`, linhas 88-89:
```java
String emailDestino = "gisele@profood.com.br";
String emailsEmCopia = "deyse@tuicial.com.br,comercial@profood.com.br,...";
```

### Alterar Webhook (Notificações)
No arquivo `WebhookService.java`, linha 9:
```java
private static final String WEBHOOK_URL = "https://broad-dusk-64.webhook.cool";
```

## 🧪 Testar a Instalação

1. Execute a ação agendada manualmente no Sankhya
2. Verifique os logs do sistema
3. Confirme o recebimento do e-mail
4. Verifique o webhook (se configurado)

## 📧 E-mails Enviados

O sistema envia e-mails com:
- Lista de títulos vencidos
- Dias de atraso
- Valores
- Informações do cliente

## 🔧 Troubleshooting

### Erro: ClassNotFoundException
- Verifique se o JAR foi copiado corretamente
- Reinicie o servidor Sankhya

### Erro: SQLException
- Verifique a conexão com o banco de dados
- Confirme que as tabelas existem

### E-mails não são enviados
- Verifique a configuração SMTP no Sankhya
- Confirme que a tabela `MSDFilaMensagem` está sendo populada

## 📞 Suporte

Para dúvidas sobre:
- **Sankhya ERP:** Consulte a documentação oficial
- **Este projeto:** Abra uma issue no repositório

## 📄 Licença

[Adicione a licença do projeto aqui]
