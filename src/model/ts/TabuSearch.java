package model.ts;

import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.sa.operators.Swap;
import model.schedule.SchedulingPeriod;

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

        Individual oldIndividual = Individual.copy(initIndividual);
        Individual bestIndividual = oldIndividual;
        Neighborhood neighborhood = new Neighborhood();
        TabuList tabuList = new TabuList();

        for (int i = 0; i < 300; i++) {
            List<Individual> neighborList = new ArrayList<>();
            //schleife ganz viele swaps
            System.out.print((i + 1) + ". Iteration \t");
            for (int j = 0; j < 10; j++) {
                Swap swap = new Swap();
                //TODO define neighborhood; swaps on same day for minimal change?
                neighborList.add(swap.mutate(oldIndividual));
            }
            System.out.print("neighbor\t");

            neighborhood.addIndividualsToPool(neighborList);

            Individual newIndividual = neighborhood.getBestIndividual();

            if (!tabuList.contains(newIndividual)) {
                oldIndividual = newIndividual;
                tabuList.add(oldIndividual);
                if (oldIndividual.getFitness() < bestIndividual.getFitness()) {
                    bestIndividual = oldIndividual;
                }
            } else {
                //TODO define asperiations criterias
                if (newIndividual.getFitness() < bestIndividual.getFitness()) {
                    oldIndividual = newIndividual;
                    bestIndividual = oldIndividual;
                }
                //check aspiritations criterias
            }
            System.out.println("end");
        }
        return bestIndividual;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }
}
