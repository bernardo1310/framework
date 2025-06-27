package criadorbanco.interfaces.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import criadorbanco.interfaces.Mantivel;

public class Tabela implements Mantivel {
    private String nome;
    private List<Campo> campos;
    private List<Campo> camposChavePrimaria;
    private List<ChaveEstrangeira> chavesEstrangeiras;

    public Tabela(String nome) {
        this.nome = nome;
        this.campos = new ArrayList<>();
        this.camposChavePrimaria = new ArrayList<>();
        this.chavesEstrangeiras = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void adicionarCampo(Campo campo) {
        this.campos.add(campo);
    }

    public void removerCampo(Campo campo) {
        this.campos.remove(campo);
        this.camposChavePrimaria.remove(campo);
    }

    public List<Campo> obterCampos() {
        return Collections.unmodifiableList(campos);
    }

    public void adicionarChaveEstrangeira(ChaveEstrangeira chave) {
        this.chavesEstrangeiras.add(chave);
    }

    public void setCamposChavePrimaria(List<Campo> camposChavePrimaria) {
        this.camposChavePrimaria = new ArrayList<>(camposChavePrimaria);
    }

    public List<Campo> getCamposChavePrimaria() {
        return Collections.unmodifiableList(camposChavePrimaria);
    }

    public List<ChaveEstrangeira> getChavesEstrangeiras() {
        return Collections.unmodifiableList(chavesEstrangeiras);
    }

    @Override
    public String manter() {
        // Lógica para manter a tabela (criar, alterar, excluir) - a ser implementada
        return "-- Operação de manutenção para a tabela: " + nome + " --";
    }

    public String gerarScriptCriacao() {
        StringBuilder script = new StringBuilder();
        script.append("CREATE TABLE IF NOT EXISTS ").append(nome).append(" (\n");

        List<String> definicoes = new ArrayList<>();
        for (Campo campo : campos) {
            definicoes.add("    " + campo.gerarScriptDefinicao());
        }

        if (!camposChavePrimaria.isEmpty()) {
            String pkNames = camposChavePrimaria.stream()
                                                .map(Campo::getNome)
                                                .collect(Collectors.joining(", "));
            definicoes.add("    PRIMARY KEY (" + pkNames + ")");
        }

        // ⚠️ NÃO incluir as FKs aqui

        script.append(String.join(",\n", definicoes));
        script.append("\n);");
        return script.toString();
    }

}


