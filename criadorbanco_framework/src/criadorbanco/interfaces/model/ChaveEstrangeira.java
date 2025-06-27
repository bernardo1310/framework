package criadorbanco.interfaces.model;

public class ChaveEstrangeira {
    private Campo campoOrigem;
    private Tabela tabelaDestino;
    private Campo campoDestino;

    public ChaveEstrangeira(Campo campoOrigem, Tabela tabelaDestino, Campo campoDestino) {
        this.campoOrigem = campoOrigem;
        this.tabelaDestino = tabelaDestino;
        this.campoDestino = campoDestino;
    }

    public Campo getCampoOrigem() {
        return campoOrigem;
    }

    public Tabela getTabelaDestino() {
        return tabelaDestino;
    }

    public Campo getCampoDestino() {
        return campoDestino;
    }
}


