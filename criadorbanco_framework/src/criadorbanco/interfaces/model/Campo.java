package criadorbanco.interfaces.model;

import criadorbanco.interfaces.Mantivel;

public class Campo implements Mantivel {
    private String nome;
    private String tipo;
    private boolean ehChavePrimaria;
    private boolean ehNulo;

    public Campo(String nome, String tipo, boolean ehChavePrimaria, boolean ehNulo) {
        this.nome = nome;
        this.tipo = tipo;
        this.ehChavePrimaria = ehChavePrimaria;
        this.ehNulo = ehNulo;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isEhChavePrimaria() {
        return ehChavePrimaria;
    }

    public boolean isEhNulo() {
        return ehNulo;
    }

    @Override
    public String manter() {
        // Lógica para manter o campo (criar, alterar, excluir) - a ser implementada
        return "-- Operação de manutenção para o campo: " + nome + " --";
    }

    public String gerarScriptDefinicao() {
        StringBuilder sb = new StringBuilder();
        sb.append(nome).append(" ").append(tipo);
        // m incluir PRIMARYKEY aqui para evitar erro no SQL
        if (!ehNulo) {
            sb.append(" NOT NULL");
        } else {
            sb.append(" NULL");
        }
        return sb.toString();
    }
}
