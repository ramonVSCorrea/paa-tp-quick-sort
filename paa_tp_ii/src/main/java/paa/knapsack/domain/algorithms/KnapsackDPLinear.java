package paa.knapsack.domain.algorithms;

import paa.knapsack.domain.Item;
import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo de Programação Dinâmica com Espaço Linear para o Problema da Mochila 0/1.
 *
 * LETRA B: Implementa um algoritmo com complexidade de espaço linear O(L)
 * capaz de obter a solução ótima para o problema.
 *
 * Observação: Com espaço linear, não é possível reconstruir quais itens
 * foram selecionados. Para isso, utilize o algoritmo da Letra C (matriz completa).
 *
 * @author Yrlan
 */
public class KnapsackDPLinear {

    /**
     * Resolve o problema da mochila usando Programação Dinâmica com espaço
     * linear O(L). Retorna APENAS o valor ótimo, sem reconstrução dos itens.
     *
     * Princípio de funcionamento:
     * - Utiliza um único vetor dp[0..L] onde dp[m] representa o maior valor
     *   alcançável usando capacidade m.
     * - A cada item processado, percorremos as capacidades em ordem DECRESCENTE
     *   (de L até peso do item) para garantir que cada item seja usado no máximo
     *   uma vez (problema 0/1).
     * - Se percorrêssemos em ordem crescente, um mesmo item poderia ser contado
     *   múltiplas vezes, resolvendo o problema da mochila ilimitada.
     *
     * Complexidade de Tempo: O(n * L)
     *   - n = número de itens
     *   - L = capacidade da mochila
     *   - Para cada item, percorremos no máximo L capacidades
     *
     * Complexidade de Espaço: O(L)
     *   - Utilizamos apenas um vetor de tamanho L+1
     *   - Isso representa uma otimização em relação ao algoritmo tradicional
     *     que usa matriz O(n * L)
     *
     * @param instance Instância do problema contendo itens e capacidade
     * @return Resultado contendo o valor ótimo (lista de itens vazia)
     */
    public static KnapsackResult solveDPLinearSpace(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = instance.getItems();
        int n = items.size();
        int L = instance.getCapacidade();

        // Vetor unidimensional: dp[m] = maior valor alcançável usando capacidade m
        // Inicializado com 0 (Java faz isso automaticamente)
        int[] dp = new int[L + 1];

        // Processa cada item
        for (int i = 0; i < n; i++) {
            Item item = items.get(i);
            int peso = item.getPeso();
            int valor = item.getValor();

            // IMPORTANTE: Percorre em ordem DECRESCENTE
            // Isso garante que dp[j - peso] ainda contém o valor da iteração
            // anterior (sem o item i), evitando usar o mesmo item múltiplas vezes
            for (int j = L; j >= peso; j--) {
                // Decide: manter valor atual OU incluir este item
                int valorComItem = dp[j - peso] + valor;
                if (valorComItem > dp[j]) {
                    dp[j] = valorComItem;
                }
            }
        }

        long tempoMs = System.currentTimeMillis() - startTime;

        // Calcula memória utilizada: vetor de (L+1) inteiros
        long memoriaBytes = (long) (L + 1) * Integer.BYTES;
        long memoriaKb = memoriaBytes / 1024;

        return new KnapsackResult.Builder()
                .valorTotal(dp[L])
                .pesoTotal(0) // Não disponível com espaço linear
                .idsItensSelecionados(new ArrayList<>()) // Não disponível com espaço linear
                .nomeAlgoritmo("DP-LinearSpace (Letra B)")
                .tempoExecutacaoMs(tempoMs)
                .memoriaUsadaKb(memoriaKb)
                .descricao(String.format("O(%d*%d) tempo, O(%d) espaço", n, L, L))
                .build();
    }
}