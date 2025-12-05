package paa.knapsack.domain.algorithms;

import paa.knapsack.domain.Item;
import paa.knapsack.domain.KnapsackInstance;
import paa.knapsack.domain.KnapsackResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Algoritmo Guloso (Greedy) para o Problema da Mochila 0/1.
 *
 * ALGORITMO APROXIMADO: Não garante solução ótima, mas produz boas soluções
 * na prática com excelente desempenho.
 *
 * Estratégia:
 * 1. Ordena os itens por razão valor/peso (densidade) em ordem decrescente
 * 2. Seleciona itens na ordem até a mochila ficar cheia
 *
 * Complexidade:
 * - Tempo: O(n log n) devido à ordenação
 * - Espaço: O(n) para armazenar a lista ordenada
 *
 * Qualidade da Solução:
 * - Na prática, frequentemente encontra soluções próximas do ótimo (90-99%)
 * - Pior caso teórico: pode ser arbitrariamente ruim
 * - Melhor caso: encontra a solução ótima quando os itens têm densidades distintas
 *
 * Vantagens:
 * ✅ MUITO rápido: O(n log n) vs O(n²) dos algoritmos exatos
 * ✅ Usa pouca memória: O(n)
 * ✅ Simples de implementar e entender
 * ✅ Boas soluções na prática
 * ✅ Escalável para problemas enormes (n > 1.000.000)
 *
 * Desvantagens:
 * ❌ Não garante solução ótima
 * ❌ Sem garantias teóricas de aproximação (não é uma FPTAS)
 *
 */
public class KnapsackGreedy {

    /**
     * Resolve o problema da mochila usando estratégia gulosa por densidade.
     *
     * @param instance Instância do problema contendo itens e capacidade
     * @return Resultado contendo uma solução aproximada
     */
    public static KnapsackResult solve(KnapsackInstance instance) {
        long startTime = System.currentTimeMillis();
        List<Item> items = new ArrayList<>(instance.getItems());
        int L = instance.getCapacidade();

        // Ordena itens por razão valor/peso (densidade) em ordem DECRESCENTE
        // Itens com maior "bang for the buck" são escolhidos primeiro
        items.sort((a, b) -> {
            double razaoA = a.getRazaoValorPeso();
            double razaoB = b.getRazaoValorPeso();
            // Ordem decrescente: maior razão primeiro
            return Double.compare(razaoB, razaoA);
        });

        // Estratégia gulosa: pega itens na ordem até encher a mochila
        List<Integer> idsItensSelecionados = new ArrayList<>();
        int valorTotal = 0;
        int pesoTotal = 0;

        for (Item item : items) {
            // Se o item cabe na capacidade restante, pega ele
            if (pesoTotal + item.getPeso() <= L) {
                idsItensSelecionados.add(item.getId());
                valorTotal += item.getValor();
                pesoTotal += item.getPeso();
            }
            // Senão, ignora e continua (não pode usar frações - problema 0/1)
        }

        // Ordena os IDs para facilitar comparação
        Collections.sort(idsItensSelecionados);
        long tempoMs = System.currentTimeMillis() - startTime;

        // Memória: lista de itens ordenada (n itens)
        long memoriaBytes = (long) items.size() * 64; // Estimativa: 64 bytes por item
        long memoriaKb = memoriaBytes / 1024;

        return new KnapsackResult.Builder()
            .valorTotal(valorTotal)
            .pesoTotal(pesoTotal)
            .idsItensSelecionados(idsItensSelecionados)
            .nomeAlgoritmo("Greedy (Aproximado)")
            .tempoExecutacaoMs(tempoMs)
            .memoriaUsadaKb(memoriaKb)
            .descricao("O(n log n) tempo - Heurística por densidade")
            .build();
    }
}
