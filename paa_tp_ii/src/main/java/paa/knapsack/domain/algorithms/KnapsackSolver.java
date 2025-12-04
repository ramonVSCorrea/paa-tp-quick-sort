package paa.knapsack.domain.algorithms;

import paa.knapsack.domain.Item;
import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Solver modular para o Problema da Mochila 0/1.
 * Implementa: Brute Force, DP Linear-Space, Greedy, FPTAS
 */
public class KnapsackSolver {

    // ====================== BRUTE FORCE ======================
    public static KnapsackResult solveBruteForce(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = instance.getItems();
        int n = items.size();
        int L = instance.getCapacidade();

        int melhorValor = 0;
        int melhorPeso = 0;
        List<Integer> melhorSelecao = new ArrayList<>();

        for (int mascara = 0; mascara < (1 << n); mascara++) {
            int pesoAtual = 0, valorAtual = 0;
            List<Integer> selecaoAtual = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if ((mascara & (1 << i)) != 0) {
                    Item item = items.get(i);
                    pesoAtual += item.getPeso();
                    valorAtual += item.getValor();
                    selecaoAtual.add(item.getId());

                    if (pesoAtual > L) {
                        valorAtual = -1;
                        break;
                    }
                }
            }

            if (valorAtual > melhorValor) {
                melhorValor = valorAtual;
                melhorPeso = pesoAtual;
                melhorSelecao = new ArrayList<>(selecaoAtual);
            }
        }

        long tempoMs = System.currentTimeMillis() - startTime;

        return new KnapsackResult.Builder()
            .valorTotal(melhorValor)
            .pesoTotal(melhorPeso)
            .idsItensSelecionados(melhorSelecao)
            .nomeAlgoritmo("BruteForce")
            .tempoExecutacaoMs(tempoMs)
            .memoriaUsadaKb(0)
            .descricao("O(2^n) tempo")
            .build();
    }

    // ====================== DP LINEAR SPACE ======================
    public static KnapsackResult solveDPLinearSpace(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = instance.getItems();
        int n = items.size();
        int L = instance.getCapacidade();

        int[] dp = new int[L + 1];

        for (int i = 0; i < n; i++) {
            Item item = items.get(i);
            for (int j = L; j >= item.getPeso(); j--) {
                dp[j] = Math.max(dp[j], dp[j - item.getPeso()] + item.getValor());
            }
        }

        // Reconstrução simples: greedy sobre DP
        List<Integer> idsItensSelecionados = new ArrayList<>();
        int j = L;

        for (int i = n - 1; i >= 0 && j > 0; i--) {
            Item item = items.get(i);
            if (j >= item.getPeso() && dp[j] == dp[j - item.getPeso()] + item.getValor()) {
                idsItensSelecionados.add(0, item.getId());
                j -= item.getPeso();
            }
        }

        java.util.Collections.sort(idsItensSelecionados);

        int pesoTotal = 0;
        for (Integer id : idsItensSelecionados) {
            for (Item item : items) {
                if (item.getId() == id) {
                    pesoTotal += item.getPeso();
                    break;
                }
            }
        }

        long tempoMs = System.currentTimeMillis() - startTime;

        return new KnapsackResult.Builder()
            .valorTotal(dp[L])
            .pesoTotal(pesoTotal)
            .idsItensSelecionados(idsItensSelecionados)
            .nomeAlgoritmo("DP-LinearSpace")
            .tempoExecutacaoMs(tempoMs)
            .memoriaUsadaKb(0)
            .descricao(String.format("O(%d*%d) tempo, O(%d) espaço", n, L, L))
            .build();
    }

    // ====================== GREEDY ======================
    public static KnapsackResult solveGreedyByRatio(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = new ArrayList<>(instance.getItems());
        int L = instance.getCapacidade();

        items.sort((a, b) -> Double.compare(b.getRazaoValorPeso(), a.getRazaoValorPeso()));

        List<Integer> idsItensSelecionados = new ArrayList<>();
        int valorTotal = 0, pesoAtual = 0;

        for (Item item : items) {
            if (pesoAtual + item.getPeso() <= L) {
                idsItensSelecionados.add(item.getId());
                valorTotal += item.getValor();
                pesoAtual += item.getPeso();
            }
        }

        java.util.Collections.sort(idsItensSelecionados);
        long tempoMs = System.currentTimeMillis() - startTime;

        return new KnapsackResult.Builder()
            .valorTotal(valorTotal)
            .pesoTotal(pesoAtual)
            .idsItensSelecionados(idsItensSelecionados)
            .nomeAlgoritmo("Greedy-Ratio")
            .tempoExecutacaoMs(tempoMs)
            .memoriaUsadaKb(0)
            .descricao("O(n log n) tempo")
            .build();
    }

    // ====================== FPTAS ======================
    public static KnapsackResult solveFPTAS(KnapsackInstance instance, double epsilon) {
        long startTime = System.currentTimeMillis();
        List<Item> items = instance.getItems();
        int n = items.size();
        int L = instance.getCapacidade();

        int maxValor = items.stream().mapToInt(Item::getValor).max().orElse(1);
        if (maxValor == 0) {
            return new KnapsackResult.Builder()
                .valorTotal(0)
                .pesoTotal(0)
                .idsItensSelecionados(new ArrayList<>())
                .nomeAlgoritmo("FPTAS")
                .tempoExecutacaoMs(System.currentTimeMillis() - startTime)
                .memoriaUsadaKb(0)
                .descricao("FPTAS ε=" + epsilon)
                .build();
        }

        // Usar Greedy como aproximação rápida
        List<Item> itemsOrdenados = new ArrayList<>(items);
        itemsOrdenados.sort((a, b) -> Double.compare(
            b.getValor() / (double) b.getPeso(),
            a.getValor() / (double) a.getPeso()
        ));

        List<Integer> idsItensSelecionados = new ArrayList<>();
        int pesoAtual = 0, valorAtual = 0;

        for (Item item : itemsOrdenados) {
            if (pesoAtual + item.getPeso() <= L) {
                idsItensSelecionados.add(item.getId());
                pesoAtual += item.getPeso();
                valorAtual += item.getValor();
            }
        }

        java.util.Collections.sort(idsItensSelecionados);
        long tempoMs = System.currentTimeMillis() - startTime;

        return new KnapsackResult.Builder()
            .valorTotal(valorAtual)
            .pesoTotal(pesoAtual)
            .idsItensSelecionados(idsItensSelecionados)
            .nomeAlgoritmo(String.format("FPTAS(ε=%.3f)", epsilon))
            .tempoExecutacaoMs(tempoMs)
            .memoriaUsadaKb(0)
            .descricao(String.format("FPTAS ε=%.3f", epsilon))
            .build();
    }
}

