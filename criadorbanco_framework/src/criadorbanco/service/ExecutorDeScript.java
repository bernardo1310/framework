package criadorbanco.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import criadorbanco.interfaces.model.ConfiguracaoConexao;

public class ExecutorDeScript {
    private ConfiguracaoConexao configuracao;

    public ExecutorDeScript(ConfiguracaoConexao configuracao) {
        this.configuracao = configuracao;
        // O carregamento do driver JDBC deve ser feito pelo usuário ou por um mecanismo de injeção de dependência
        // ou pode ser tratado de forma mais genérica aqui, dependendo do tipo de banco.
        // Por exemplo, para MySQL, seria Class.forName("com.mysql.cj.jdbc.Driver");
        // No contexto deste framework, assumimos que o driver apropriado está no classpath.
    }

    public void executar(String script) {
        Connection conexao = null;
        Statement stmt = null;
        try {
            String url = configuracao.obterStringConexao();

            String tempUrl = url;
            if (script.trim().toUpperCase().startsWith("CREATE DATABASE")) {
                int lastSlash = url.lastIndexOf('/');
                int lastQuestion = url.lastIndexOf('?');
                if (lastSlash != -1 && (lastQuestion == -1 || lastQuestion < lastSlash)) {
                    tempUrl = url.substring(0, lastSlash + 1);
                } else if (lastQuestion != -1) {
                    tempUrl = url.substring(0, lastQuestion);
                }
            }

            conexao = DriverManager.getConnection(tempUrl, configuracao.getUsuario(), configuracao.getSenha());
            stmt = conexao.createStatement();

            // Quebrar o script em comandos separados
            String[] comandos = script.split(";");
            for (String comando : comandos) {
                comando = comando.trim();
                if (!comando.isEmpty()) {
                    System.out.println("Executando comando: " + comando);
                    stmt.execute(comando);
                }
            }

            System.out.println("Script SQL executado com sucesso!\n");
        } catch (SQLException e) {
            System.err.println("Erro ao executar script SQL: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos JDBC: " + e.getMessage());
            }
        }
    }

}


