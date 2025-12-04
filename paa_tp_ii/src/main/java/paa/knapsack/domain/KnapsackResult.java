package paa.knapsack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Resultado da resolução de uma instância do problema da mochila.
 */
public class KnapsackResult {
    private final int valorTotal;
    private final int pesoTotal;
    private final List<Integer> idsItensSelecionados;
    private final long tempoExecutacaoMs;
    private final long memoriaUsadaKb;
    private final String nomeAlgoritmo;
    private final String descricao;

    private KnapsackResult(Builder builder) {
        this.valorTotal = builder.valorTotal;
        this.pesoTotal = builder.pesoTotal;
        this.idsItensSelecionados = Collections.unmodifiableList(
            new ArrayList<>(builder.idsItensSelecionados)
        );
        this.tempoExecutacaoMs = builder.tempoExecutacaoMs;
        this.memoriaUsadaKb = builder.memoriaUsadaKb;
        this.nomeAlgoritmo = builder.nomeAlgoritmo;
        this.descricao = builder.descricao;
    }

    public int getValorTotal() { return valorTotal; }
    public int getPesoTotal() { return pesoTotal; }
    public List<Integer> getIdsItensSelecionados() { return idsItensSelecionados; }
    public int getNumItensSelecionados() { return idsItensSelecionados.size(); }
    public long getTempoExecutacaoMs() { return tempoExecutacaoMs; }
    public long getMemoriaUsadaKb() { return memoriaUsadaKb; }
    public String getNomeAlgoritmo() { return nomeAlgoritmo; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return String.format(
            "%s{valor=%d, peso=%d, itens=%d, tempo=%dms, mem=%dKB}",
            nomeAlgoritmo, valorTotal, pesoTotal,
            idsItensSelecionados.size(), tempoExecutacaoMs, memoriaUsadaKb
        );
    }

    public static class Builder {
        private int valorTotal = 0;
        private int pesoTotal = 0;
        private List<Integer> idsItensSelecionados = new ArrayList<>();
        private long tempoExecutacaoMs = 0;
        private long memoriaUsadaKb = 0;
        private String nomeAlgoritmo = "UNKNOWN";
        private String descricao = "";

        public Builder valorTotal(int valor) {
            this.valorTotal = valor;
            return this;
        }

        public Builder pesoTotal(int peso) {
            this.pesoTotal = peso;
            return this;
        }

        public Builder idsItensSelecionados(List<Integer> ids) {
            this.idsItensSelecionados = new ArrayList<>(ids);
            return this;
        }

        public Builder adicionarItem(int id) {
            this.idsItensSelecionados.add(id);
            return this;
        }

        public Builder tempoExecutacaoMs(long tempo) {
            this.tempoExecutacaoMs = tempo;
            return this;
        }

        public Builder memoriaUsadaKb(long memoria) {
            this.memoriaUsadaKb = memoria;
            return this;
        }

        public Builder nomeAlgoritmo(String nome) {
            this.nomeAlgoritmo = nome;
            return this;
        }

        public Builder descricao(String desc) {
            this.descricao = desc;
            return this;
        }

        public KnapsackResult build() {
            return new KnapsackResult(this);
        }
    }
}

