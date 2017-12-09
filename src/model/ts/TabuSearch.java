package model.ts;

import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.sa.operators.Swap;
import model.schedule.SchedulingPeriod;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.List;

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

        Individual bestIndividual = Individual.copy(initIndividual);
        Neighborhood neighborhood = new Neighborhood();
        List<Individual> tabuList = new ArrayList<>();

        boolean stopCondition = true;


        while (stopCondition) {
            for (int i = 0; i < 1; i++) {
                List<Individual> neighborList = new ArrayList<>();
                //schleife ganz viele swaps
                for (int j = 0; j < 10; j++) {
                    Swap swap = new Swap();
                    //TODO define neighborhood; swaps on same day for minimal change?
                    neighborList.add(swap.mutate(bestIndividual));
                }
                neighborhood.addIndividualsToPool(neighborList);

                Individual newBestIndividual = neighborhood.getBestIndividual();

                if (!tabuList.contains(newBestIndividual)) {
                    bestIndividual = newBestIndividual;
                    tabuList.add(newBestIndividual);
                } else {
                    //keine verbesserung
                }
            }
        }

        return null;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }
}
