package criadorbanco.interfaces.model;

public abstract class ConfiguracaoConexao {
    protected String servidor;
    protected int porta;
    protected String usuario;
    protected String senha;

    public ConfiguracaoConexao(String servidor, int porta, String usuario, String senha) {
        this.servidor = servidor;
        this.porta = porta;
        this.usuario = usuario;
        this.senha = senha;
    }

    public String getServidor() {
        return servidor;
    }

    public int getPorta() {
        return porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public abstract String obterStringConexao();
}


