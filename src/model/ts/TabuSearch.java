package model.ts;

import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.schedule.SchedulingPeriod;

public class TabuSearch {
    /**
     * Holds the initializing individual (for benchmarking purposes against last solutions).
     */
    private Individual initIndividual = null;

    public Individual optimize(SchedulingPeriod period) {
        //get initialsolution
        RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
        initIndividual = randomConstructionHeuristic.getIndividual(period);
        initIndividual.getFitness(true);

        return null;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }
}
