package paa.knapsack.domain;

/**
 * Representa um item a ser considerado no problema da mochila.
 *
 * Cada item possui um identificador único, peso e valor.
 * Imutável após criação para garantir consistência.
 */
public class Item {
    private final int id;
    private final int peso;
    private final int valor;

    /**
     * Construtor do item.
     *
     * @param id     Identificador único do item
     * @param peso   Peso do item (deve ser positivo)
     * @param valor  Valor do item (deve ser não-negativo)
     * @throws IllegalArgumentException se peso <= 0 ou valor < 0
     */
    public Item(int id, int peso, int valor) {
        if (peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser positivo: " + peso);
        }
        if (valor < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo: " + valor);
        }
        this.id = id;
        this.peso = peso;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public int getPeso() {
        return peso;
    }

    public int getValor() {
        return valor;
    }

    /**
     * Calcula a razão valor/peso do item.
     * Útil para algoritmos gulosos.
     *
     * @return razão valor/peso como double
     */
    public double getRazaoValorPeso() {
        return (double) valor / peso;
    }

    @Override
    public String toString() {
        return String.format("Item{id=%d, peso=%d, valor=%d, razao=%.3f}",
            id, peso, valor, getRazaoValorPeso());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && peso == item.peso && valor == item.valor;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, peso, valor);
    }
}

