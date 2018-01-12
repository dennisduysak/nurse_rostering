package model.ts;

import helper.ConfigurationHelper;
import helper.RandomHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.operators.SwappingNursesMutation;
import model.schedule.SchedulingPeriod;

import java.util.ArrayList;
import java.util.List;

public class TabuSearch {
    /**
     * Current iteration.
     */
    private int iteration = 0;

    /**
     * Maximum number of iterations.
     */
    private int maxIterations = ConfigurationHelper.getInstance().getPropertyInteger("ts.MaxIterations", 1000);

    /**
     * Maximum number of iterations.
     */
    private int neighbourSize = ConfigurationHelper.getInstance().getPropertyInteger("ts.NeighbourSize", 10);

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

        while (!isTerminationCondition()) {
            // get random day
            int numberOfDays = oldIndividual.getDayRosters().size();
            int randDay = RandomHelper.getInstance().getInt(numberOfDays);

            //get neighbourhood
            List<Individual> neighborList = new ArrayList<>();
            for (int i = 0; i < neighbourSize; i++) {
                SwappingNursesMutation swap = new SwappingNursesMutation();
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
            System.out.println(iteration + ". Iteration\t" + oldIndividual.getFitness());
        }
        return bestIndividual;
    }

    /**
     * Returns true, if the termination condition is met.
     *
     * @return True, if termination condition is met
     */
    private boolean isTerminationCondition() {
        return ++iteration > maxIterations;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }
}
