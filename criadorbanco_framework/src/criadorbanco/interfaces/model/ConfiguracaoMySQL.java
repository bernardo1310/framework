package criadorbanco.interfaces.model;

public class ConfiguracaoMySQL extends ConfiguracaoConexao {
    private String nomeBanco;

    public ConfiguracaoMySQL(String servidor, int porta, String usuario, String senha, String nomeBanco) {
        super(servidor, porta, usuario, senha);
        this.nomeBanco = nomeBanco;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    @Override
    public String obterStringConexao() {
        return String.format("jdbc:mysql://%s:%d/%s?allowMultiQueries=true",
                             servidor, porta, nomeBanco);
    }
}


