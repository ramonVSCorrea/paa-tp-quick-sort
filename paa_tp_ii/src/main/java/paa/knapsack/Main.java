package paa.knapsack;

import paa.knapsack.application.KnapsackStudyStarter;

/**
 * Ponto de entrada do programa.
 */
public class Main {
    public static void main(String[] args) {
        KnapsackStudyStarter study = new KnapsackStudyStarter();
        study.executeCompleteStudy();
        study.executeCustomStudy();
    }
}

