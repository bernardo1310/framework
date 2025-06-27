package criadorbanco.interfaces.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import criadorbanco.interfaces.Mantivel;

public class BancoDados implements Mantivel {
    private String nome;
    private List<Tabela> tabelas;

    public BancoDados(String nome) {
        this.nome = nome;
        this.tabelas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void adicionarTabela(Tabela tabela) {
        this.tabelas.add(tabela);
    }

    public void removerTabela(Tabela tabela) {
        this.tabelas.remove(tabela);
    }

    public List<Tabela> obterTabelas() {
        return Collections.unmodifiableList(tabelas);
    }

    @Override
    public String manter() {
        return "-- Operação de manutenção para o banco de dados: " + nome + " --";
    }

    public String gerarScriptCriacao() {
        return "CREATE DATABASE IF NOT EXISTS " + nome + ";";
    }

    public String gerarScriptCompleto() {
        StringBuilder scriptCompleto = new StringBuilder();

        // Criação do banco de dados
        scriptCompleto.append(gerarScriptCriacao()).append("\n\n");

        // Seleciona o banco de dados
        scriptCompleto.append("USE ").append(nome).append(";\n\n");

        // --- OPCIONAL: Drop das tabelas antes de recriá-las ---
        // apagar todas as tabelas existentes antes de criar novamente ( para testes),
        // IMPORTANTE: Isso apagará todos os dados das tabelas!
        scriptCompleto.append("-- Dropando tabelas existentes (ordem reversa por causa das FKs) --\n");
        for (int i = tabelas.size() - 1; i >= 0; i--) {
            scriptCompleto.append("DROP TABLE IF EXISTS ").append(tabelas.get(i).getNome()).append(";\n");
        }
        scriptCompleto.append("\n");

        // Criação das tabelas
        for (Tabela tabela : tabelas) {
            scriptCompleto.append(tabela.gerarScriptCriacao()).append("\n\n");
        }

        // Adição de chaves estrangeiras (executadas após as tabelas existirem)
        for (Tabela tabela : tabelas) {
            for (ChaveEstrangeira fk : tabela.getChavesEstrangeiras()) {
                scriptCompleto.append("ALTER TABLE ").append(tabela.getNome())
                              .append(" ADD CONSTRAINT fk_").append(tabela.getNome()).append("_")
                              .append(fk.getCampoOrigem().getNome())
                              .append(" FOREIGN KEY (").append(fk.getCampoOrigem().getNome()).append(")")
                              .append(" REFERENCES ").append(fk.getTabelaDestino().getNome())
                              .append("(").append(fk.getCampoDestino().getNome()).append(");\n");
            }
        }

        return scriptCompleto.toString();
    }
}
