package paa.knapsack.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa uma instância do problema da mochila 0/1.
 *
 * Problema: Maximizar ∑(u_i) sujeito a ∑(p_i) ≤ L
 * onde:
 *   - u_i = valor do item i
 *   - p_i = peso do item i
 *   - L = capacidade da mochila
 *
 * Imutável e validada após criação.
 */
public class KnapsackInstance {
    private final List<Item> items;
    private final int capacidade;

    public KnapsackInstance(List<Item> items, int capacidade) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Lista de items não pode ser nula ou vazia");
        }
        if (capacidade <= 0) {
            throw new IllegalArgumentException("Capacidade deve ser positiva: " + capacidade);
        }

        this.items = Collections.unmodifiableList(new ArrayList<>(items));
        this.capacidade = capacidade;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getNumItems() {
        return items.size();
    }

    public Item getItem(int index) {
        return items.get(index);
    }

    public int calcularPesoTotal(List<Integer> itemIds) {
        return itemIds.stream()
            .mapToInt(id -> items.stream()
                .filter(item -> item.getId() == id)
                .mapToInt(Item::getPeso)
                .sum())
            .sum();
    }

    public int calcularValorTotal(List<Integer> itemIds) {
        return itemIds.stream()
            .mapToInt(id -> items.stream()
                .filter(item -> item.getId() == id)
                .mapToInt(Item::getValor)
                .sum())
            .sum();
    }

    @Override
    public String toString() {
        return String.format("KnapsackInstance{items=%d, capacidade=%d, peso_total_items=%d}",
            items.size(), capacidade, items.stream().mapToInt(Item::getPeso).sum());
    }
}

