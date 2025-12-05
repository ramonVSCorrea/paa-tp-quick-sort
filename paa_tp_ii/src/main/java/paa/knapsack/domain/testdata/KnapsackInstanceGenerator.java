package paa.knapsack.domain.testdata;

import paa.knapsack.domain.Item;
import paa.knapsack.domain.KnapsackInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Gerador de instâncias aleatórias do Problema da Mochila.
 */
public class KnapsackInstanceGenerator {

    public static KnapsackInstance generate(
            int numItems, int pesoMin, int pesoMax, int valorMin, int valorMax,
            int capacidade, long seed) {

        Random random = (seed == -1) ? new Random() : new Random(seed);
        List<Item> items = new ArrayList<>();

        for (int i = 0; i < numItems; i++) {
            int peso = pesoMin + random.nextInt(pesoMax - pesoMin + 1);
            int valor = valorMin + random.nextInt(valorMax - valorMin + 1);
            items.add(new Item(i, peso, valor));
        }

        return new KnapsackInstance(items, capacidade);
    }

    public static KnapsackInstance generateSimple(int numItems, int capacidade, long seed) {
        return generate(numItems, 1, 20, 1, 100, capacidade, seed);
    }

    public static KnapsackInstance generateBenchmark(int numItems, long seed) {
        Random random = new Random(seed);
        List<Item> items = new ArrayList<>();
        int pesoTotal = 0;

        for (int i = 0; i < numItems; i++) {
            int peso = 5 + random.nextInt(25);
            int valor = 10 + random.nextInt(90);
            items.add(new Item(i, peso, valor));
            pesoTotal += peso;
        }

        int capacidade = Math.max(10, (int) (pesoTotal * 0.4));
        return new KnapsackInstance(items, capacidade);
    }
}

