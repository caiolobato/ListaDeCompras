package pa.senac.br.listadecompras.model;

public class Lista {
    private String nomeLista;
    private String quantidade;
    private String dataLista;

    public String getNomeLista() {
        return nomeLista;
    }

    public void setNomeLista(String nomeLista) {
        this.nomeLista = nomeLista;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getDataLista() {
        return dataLista;
    }

    public void setDataLista(String dataLista) {
        this.dataLista = dataLista;
    }

    @Override
    public String toString() {
        return nomeLista + " - " + quantidade + " - " + dataLista;
    }
}

