package criadorbanco;

import java.util.Arrays;

import criadorbanco.interfaces.model.BancoDados;
import criadorbanco.interfaces.model.Campo;
import criadorbanco.interfaces.model.ChaveEstrangeira;
import criadorbanco.interfaces.model.ConfiguracaoConexao;
import criadorbanco.interfaces.model.ConfiguracaoMySQL;
import criadorbanco.interfaces.model.Tabela;
import criadorbanco.service.ExecutorDeScript;

public class Main {
    public static void main(String[] args) {

        // 1. Conexão COM banco "mysql" para criar os bancos (banco padrão do MySQL)
        ConfiguracaoConexao conexaoServidor = new ConfiguracaoMySQL("localhost", 3306, "root", "", "mysql");
        ExecutorDeScript executorServidor = new ExecutorDeScript(conexaoServidor);

        System.out.println("--- Criando bancos no servidor MySQL (se não existirem) ---");
        executorServidor.executar("CREATE DATABASE IF NOT EXISTS minhaloja_mysql;");
        executorServidor.executar("CREATE DATABASE IF NOT EXISTS minhaloja_fk;");

        // 2. Conexão COM banco para criar as tabelas - Cenário 1
        ConfiguracaoConexao conexaoBanco1 = new ConfiguracaoMySQL("localhost", 3306, "root", "", "minhaloja_mysql");
        ExecutorDeScript executorBanco1 = new ExecutorDeScript(conexaoBanco1);

        System.out.println("\n--- Cenário 1: MySQL - Banco de Dados e Tabelas Simples ---");
        BancoDados meuBancoMySQL = new BancoDados("minhaloja_mysql");

        Campo idProduto = new Campo("id", "INT", true, false);
        Campo nomeProduto = new Campo("nome", "VARCHAR(255)", false, false);
        Campo precoProduto = new Campo("preco", "DECIMAL(10,2)", false, false);
        Tabela produtos = new Tabela("Produtos");
        produtos.adicionarCampo(idProduto);
        produtos.adicionarCampo(nomeProduto);
        produtos.adicionarCampo(precoProduto);
        produtos.setCamposChavePrimaria(Arrays.asList(idProduto));
        meuBancoMySQL.adicionarTabela(produtos);

        Campo idCliente = new Campo("id", "INT", true, false);
        Campo nomeCliente = new Campo("nome", "VARCHAR(255)", false, false);
        Campo emailCliente = new Campo("email", "VARCHAR(255)", false, true);
        Tabela clientes = new Tabela("Clientes");
        clientes.adicionarCampo(idCliente);
        clientes.adicionarCampo(nomeCliente);
        clientes.adicionarCampo(emailCliente);
        clientes.setCamposChavePrimaria(Arrays.asList(idCliente));
        meuBancoMySQL.adicionarTabela(clientes);

        String scriptMySQL = meuBancoMySQL.gerarScriptCompleto();
        System.out.println("\nScript SQL Gerado para MySQL:\n" + scriptMySQL);
        executorBanco1.executar(scriptMySQL);

        // 3. Conexão COM banco para criar as tabelas - Cenário 2 (chaves estrangeiras)
        ConfiguracaoConexao conexaoBanco2 = new ConfiguracaoMySQL("localhost", 3306, "root", "", "minhaloja_fk");
        ExecutorDeScript executorBanco2 = new ExecutorDeScript(conexaoBanco2);

        System.out.println("\n--- Cenário 2: MySQL - Chave Estrangeira e Associativas ---");
        BancoDados meuBancoMySQLFK = new BancoDados("minhaloja_fk");

        Campo mysqlIdProdutoFK = new Campo("id", "INT", true, false);
        Campo mysqlNomeProdutoFK = new Campo("nome", "VARCHAR(255)", false, false);
        Tabela mysqlProdutosFK = new Tabela("ProdutosFK");
        mysqlProdutosFK.adicionarCampo(mysqlIdProdutoFK);
        mysqlProdutosFK.adicionarCampo(mysqlNomeProdutoFK);
        mysqlProdutosFK.setCamposChavePrimaria(Arrays.asList(mysqlIdProdutoFK));
        meuBancoMySQLFK.adicionarTabela(mysqlProdutosFK);

        Campo mysqlIdClienteFK = new Campo("id", "INT", true, false);
        Campo mysqlNomeClienteFK = new Campo("nome", "VARCHAR(255)", false, false);
        Tabela mysqlClientesFK = new Tabela("ClientesFK");
        mysqlClientesFK.adicionarCampo(mysqlIdClienteFK);
        mysqlClientesFK.adicionarCampo(mysqlNomeClienteFK);
        mysqlClientesFK.setCamposChavePrimaria(Arrays.asList(mysqlIdClienteFK));
        meuBancoMySQLFK.adicionarTabela(mysqlClientesFK);

        Campo mysqlIdPedido = new Campo("id", "INT", true, false);
        Campo mysqlDataPedido = new Campo("data_pedido", "DATE", false, false);
        Campo mysqlIdClientePedidoFK = new Campo("cliente_id", "INT", false, false);
        Tabela mysqlPedidos = new Tabela("PedidosFK");
        mysqlPedidos.adicionarCampo(mysqlIdPedido);
        mysqlPedidos.adicionarCampo(mysqlDataPedido);
        mysqlPedidos.adicionarCampo(mysqlIdClientePedidoFK);
        mysqlPedidos.setCamposChavePrimaria(Arrays.asList(mysqlIdPedido));

        ChaveEstrangeira fkClientePedido = new ChaveEstrangeira(mysqlIdClientePedidoFK, mysqlClientesFK, mysqlIdClienteFK);
        mysqlPedidos.adicionarChaveEstrangeira(fkClientePedido);
        meuBancoMySQLFK.adicionarTabela(mysqlPedidos);

        Campo ppPedidoId = new Campo("pedido_id", "INT", false, false);
        Campo ppProdutoId = new Campo("produto_id", "INT", false, false);
        Campo ppQuantidade = new Campo("quantidade", "INT", false, false);
        Tabela pedidosProdutos = new Tabela("Pedidos_ProdutosFK");
        pedidosProdutos.adicionarCampo(ppPedidoId);
        pedidosProdutos.adicionarCampo(ppProdutoId);
        pedidosProdutos.adicionarCampo(ppQuantidade);
        pedidosProdutos.setCamposChavePrimaria(Arrays.asList(ppPedidoId, ppProdutoId));

        ChaveEstrangeira fkPpPedido = new ChaveEstrangeira(ppPedidoId, mysqlPedidos, mysqlIdPedido);
        pedidosProdutos.adicionarChaveEstrangeira(fkPpPedido);

        ChaveEstrangeira fkPpProduto = new ChaveEstrangeira(ppProdutoId, mysqlProdutosFK, mysqlIdProdutoFK);
        pedidosProdutos.adicionarChaveEstrangeira(fkPpProduto);
        meuBancoMySQLFK.adicionarTabela(pedidosProdutos);

        String scriptMySQLFK = meuBancoMySQLFK.gerarScriptCompleto();
        System.out.println("\nScript SQL Gerado para MySQL FK:\n" + scriptMySQLFK);
        executorBanco2.executar(scriptMySQLFK);

        // --- Exemplo de Manutenção (simulado) ---
        System.out.println("\n--- Exemplo de Manutenção (simulado) ---");
        meuBancoMySQL.manter();
        produtos.manter();
        idProduto.manter();
    }
}
