package model.ts;

import helper.RandomHelper;
import model.Individual;
import model.operators.SwappingNursesMutation;

import java.util.ArrayList;
import java.util.List;

public class TabuSearch {

    /**
     * Maximum number of iterations.
     */
    private int maxIterations;

    /**
     * Maximum number of iterations.
     */
    private int neighbourSize;

    /**
     * Maximum number of TabuList.
     */
    private int tabuListSize;

    /**
     * Holds the initializing individual (for benchmarking purposes against last solutions).
     */
    private Individual initIndividual;

    public TabuSearch(int maxIterations, int neighbourSize, int tabuListSize, Individual initIndividual) {
        this.maxIterations = maxIterations;
        this.neighbourSize = neighbourSize;
        this.tabuListSize = tabuListSize;
        this.initIndividual = initIndividual;
    }

    public Individual optimize() {
        //get initialsolution
        //RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
        //initIndividual = randomConstructionHeuristic.getIndividual(period);
        //initIndividual.getFitness(true);

        Individual oldIndividual = Individual.copy(initIndividual);
        Individual bestIndividual = oldIndividual;
        Neighborhood neighborhood = new Neighborhood();
        TabuList tabuList = new TabuList(tabuListSize);

        for (int i = 0; i < maxIterations; i++) {
            // get random day
            int numberOfDays = oldIndividual.getDayRosters().size();
            int randDay = RandomHelper.getInstance().getInt(numberOfDays);

            //get neighbourhood
            SwappingNursesMutation swap = new SwappingNursesMutation();
            List<Individual> neighborList = new ArrayList<>();
            for (int j = 0; j < neighbourSize; j++) {
                neighborList.add(swap.mutate(oldIndividual, randDay));
            }
            neighborhood.addIndividualsToPool(neighborList);
            neighborhood.benchmark();
            Individual newIndividual = neighborhood.getBestIndividual();

            if (!tabuList.contains(randDay)) {
                tabuList.add(randDay);
                oldIndividual = newIndividual;
                if (oldIndividual.getFitness() < bestIndividual.getFitness()) {
                    bestIndividual = oldIndividual;
                }
            } else {
                //check aspiritations criterias
                if (newIndividual.getFitness() < bestIndividual.getFitness()) {
                    oldIndividual = newIndividual;
                    bestIndividual = oldIndividual;
                }
            }
            System.out.println((i+1) + ". von " + maxIterations + " Iteration\t" + oldIndividual.getFitness());
        }
        return bestIndividual;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }
}
