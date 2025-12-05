package paa.knapsack.domain.algorithms;

import paa.knapsack.domain.Item;
import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Algoritmo de Programação Dinâmica para o Problema da Mochila 0/1.
 *
 * Implementação baseada no Algoritmo (MochilaInteira-BottomUp)
 * Complexidade de Tempo: O(n * W)
 * Complexidade de Espaço: O(n * W)
 *
 */
public class KnapsackPD {

    /**
     * Resolve o problema da mochila usando Programação Dinâmica Bottom-Up.
     *
     * M[j][X] = valor máximo considerando itens 1..j com capacidade X
     *
     * Recorrência:
     *   Se w[j] > X: M[j][X] = M[j-1][X]  (item não cabe)
     *   Senão: M[j][X] = max(M[j-1][X], v[j] + M[j-1][X - w[j]])
     *
     * @param instance Instância do problema contendo itens e capacidade
     * @return Resultado contendo valor ótimo e lista de itens selecionados
     */
    public static KnapsackResult solve(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = instance.getItems();
        int n = items.size();
        int W = instance.getCapacidade();

        // [H] Seja M[0..n][0..W] uma matriz
        int[][] M = new int[n + 1][W + 1];

        // Linha 2-3: para X = 0 até W, incrementando faça M[0][X] = 0
        for (int X = 0; X <= W; X++) {
            M[0][X] = 0;
        }

        // Linha 4-5: para j = 0 até n, incrementando faça M[j][0] = 0
        for (int j = 0; j <= n; j++) {
            M[j][0] = 0;
        }

        // Linha 6: para j = 1 até n, incrementando faça
        for (int j = 1; j <= n; j++) {
            Item item = items.get(j - 1);
            int wj = item.getPeso();   // peso do item j
            int vj = item.getValor();  // valor do item j

            // Linha 7: para X = 0 até W, incrementando faça
            for (int X = 0; X <= W; X++) {

                // Linha 8-9: se w[j] > X então M[j][X] = M[j-1][X]
                if (wj > X) {
                    M[j][X] = M[j - 1][X];
                }
                // Linha 10-13: senão
                else {
                    int usa = vj + M[j - 1][X - wj];    // Linha 11
                    int naousa = M[j - 1][X];            // Linha 12
                    M[j][X] = Math.max(usa, naousa);     // Linha 13
                }
            }
        }

        // Backtracking para recuperar os itens selecionados
        List<Integer> idsItensSelecionados = new ArrayList<>();
        int pesoTotal = 0;
        int X = W;

        for (int j = n; j >= 1 && X > 0; j--) {
            // Se M[j][X] != M[j-1][X], então o item j foi usado
            if (M[j][X] != M[j - 1][X]) {
                Item item = items.get(j - 1);
                idsItensSelecionados.add(item.getId());
                pesoTotal += item.getPeso();
                X -= item.getPeso();
            }
        }

        Collections.sort(idsItensSelecionados);
        long tempoMs = System.currentTimeMillis() - startTime;

        // Calcula memória: matriz (n+1) x (W+1) de inteiros
        long memoriaBytes = (long) (n + 1) * (W + 1) * Integer.BYTES;
        long memoriaKb = memoriaBytes / 1024;

        // Linha 14: devolve M[n][W]
        return new KnapsackResult.Builder()
                .valorTotal(M[n][W])
                .pesoTotal(pesoTotal)
                .idsItensSelecionados(idsItensSelecionados)
                .nomeAlgoritmo("DP-MatrizCompleta (Letra C)")
                .tempoExecutacaoMs(tempoMs)
                .memoriaUsadaKb(memoriaKb)
                .descricao(String.format("O(%d*%d) tempo e espaço", n, W))
                .build();
    }
}