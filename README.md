# Criador de Banco de Dados Automatizado

## Sobre o projeto

Esse projeto foi desenvolvido para a disciplina de POO 2, com foco no conceito de Frameworks.  
A ideia principal foi criar um sistema que me permitisse definir tabelas, campos e relacionamentos diretamente no código,  
e então gerar automaticamente os scripts SQL para criar toda a estrutura no banco de dados real.

Com isso, automatizei o processo que normalmente seria feito manualmente, aplicando conceitos importantes de orientação a objetos como encapsulamento, composição e uso de interfaces.

---

## Como funciona o projeto

1. **Estrutura básica**  
   O sistema é composto por classes que representam o banco de dados, tabelas, campos e chaves estrangeiras.  
   Uma interface chamada `Mantivel` padroniza o método `manter()`, que é implementado pelas principais classes para geração ou atualização dos scripts SQL.

2. **Geração automática de scripts SQL**  
   O sistema gera passo a passo os comandos SQL para criação do banco, tabelas, chaves primárias e estrangeiras, com base nos dados das classes.

3. **Configuração de conexão**  
   As configurações de conexão com o banco são abstraídas em classes específicas (como `ConfiguracaoMySQL`), facilitando alterar a configuração usando o padrão *Strategy*.

4. **Execução dos scripts**  
   Os scripts SQL gerados são executados diretamente no banco real usando JDBC, através do componente `ExecutorDeScript`.

5. **Exemplo prático no Main**  
   A classe `Main` demonstra um exemplo completo, mostrando como criar bancos, montar tabelas com relacionamentos, gerar scripts e executar no banco MySQL.

6. **Diagrama de classes**  
   O projeto possui um diagrama organizado que representa as relações entre as classes, utilizando ligações padrão de associação, composição e herança para facilitar o entendimento.

---

## Planejamento das classes principais

### BancoDados  
Representa o banco como um todo, armazenando uma lista de tabelas. Implementa a interface `Mantivel` para gerar os scripts SQL do banco.

### Tabela  
Representa uma tabela do banco, contendo listas de campos (`Campo`) e chaves estrangeiras (`ChaveEstrangeira`). Também implementa a interface `Mantivel`.

### Campo  
Representa uma coluna da tabela, com atributos como nome, tipo, se é chave primária e se aceita valor nulo.

### ChaveEstrangeira  
Modela o relacionamento entre tabelas, conectando um campo de uma tabela a um campo de outra, simulando as relações reais do banco.

### GeradorDeScript  
Classe responsável por construir os comandos SQL a partir das informações dos objetos do sistema.(Após sugestoes em sala, foi realizado a ramificação da classe para as classes necessarias)

### ExecutorDeScript  
Executa os scripts SQL no banco real usando JDBC e a configuração de conexão definida.

### ConfiguracaoConexao  
Centraliza os dados de conexão ao banco, como host, porta, usuário, senha e nome do banco.

### Interface Mantivel  
Padroniza o método `manter()`, implementado por `BancoDados`, `Tabela` e `Campo`, para facilitar operações de manutenção como criação e alteração.

---

## Padrões de projeto aplicados

- **Interface:** A interface `Mantivel` garante consistência e polimorfismo nas operações de manutenção.  
- **Strategy:** A abstração da configuração da conexão permite múltiplas implementações para diferentes bancos.  
- **Composition:** O banco contém tabelas, que contêm campos e chaves, refletindo a estrutura natural do sistema.  
- **Dependency Injection:** A configuração de conexão é injetada no executor via construtor, promovendo baixo acoplamento.  
- **Facade (implícito):** A classe `Main` organiza a execução do sistema de forma simples, escondendo complexidade do usuário.

---

## Diagrama de Classes (PlantUML)

```plantuml
@startuml
hide circle
skinparam classAttributeIconSize 0

interface Mantivel {
    +manter(): String
}

class BancoDados {
    -nome: String
    -tabelas: List<Tabela>
    +adicionarTabela(t: Tabela)
    +gerarScriptCompleto(): String
    +manter(): String
}

class Tabela {
    -nome: String
    -campos: List<Campo>
    -camposChavePrimaria: List<Campo>
    -chavesEstrangeiras: List<ChaveEstrangeira>
    +adicionarCampo(c: Campo)
    +setCamposChavePrimaria(lista: List<Campo>)
    +adicionarChaveEstrangeira(fk: ChaveEstrangeira)
    +gerarScriptCriacao(): String
    +manter(): String
}

class Campo {
    -nome: String
    -tipo: String
    -ehChavePrimaria: boolean
    -ehUnico: boolean
    +gerarScriptDefinicao(): String
    +manter(): String
}

class ChaveEstrangeira {
    -campoOrigem: Campo
    -tabelaDestino: Tabela
    -campoDestino: Campo
    +getCampoOrigem(): Campo
    +getTabelaDestino(): Tabela
    +getCampoDestino(): Campo
}

class ConfiguracaoConexao {
    -host: String
    -porta: int
    -usuario: String
    -senha: String
    -nomeBanco: String
    +getUrl(): String
}

class ConfiguracaoMySQL {
    +getUrl(): String
}

class ExecutorDeScript {
    -config: ConfiguracaoConexao
    +executar(sql: String): void
}

'Todas as implementações e heranças
Tabela ..|> Mantivel
Campo ..|> Mantivel
BancoDados ..|> Mantivel
ConfiguracaoMySQL --|> ConfiguracaoConexao

'Regras de associação e composição
BancoDados "1" o-- "*" Tabela : contém
Tabela "1" o-- "*" Campo : possui
Tabela "1" o-- "*" ChaveEstrangeira : define
ChaveEstrangeira "1" --> "1" Campo : campoOrigem
ChaveEstrangeira "1" --> "1" Campo : campoDestino
ChaveEstrangeira "1" --> "1" Tabela : tabelaDestino
ExecutorDeScript --> ConfiguracaoConexao : usa
@enduml
