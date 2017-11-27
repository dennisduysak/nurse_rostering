package model.sa;

import helper.ConfigurationHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.ea.Population;
import model.operators.SwappingNursesMutation;
import model.schedule.DayRoster;
import model.schedule.SchedulingPeriod;

import javax.naming.InitialContext;
import java.util.List;

public class SimulatedAnnealing {
    /**
     * Holds the initializing individual (for benchmarking purposes against last solutions).
     */
    private Individual initIndividual = null;

    public Individual optimize(SchedulingPeriod period) {
        initIndividual = generateInitializationPopulation(period);

        Individual oldSolution = Individual.copy(initIndividual);
        double startingTemperature = ConfigurationHelper.getInstance().getPropertyDouble("sa.StartTemperature", 100);
        double coolingRate = ConfigurationHelper.getInstance().getPropertyDouble("sa.CoolingRate", 0.1);
        int numberOfIterations = (int) (startingTemperature / coolingRate);
        Individual bestSolution = oldSolution;

        for (int i = 0; i < numberOfIterations; i++) {
            float score = oldSolution.getFitness();
            SwappingNursesMutation swap = new SwappingNursesMutation();
            Individual newSolution = swap.mutate(oldSolution);
            //System.out.println("\t" + i + 1 + ".Iteration - Score: " + newSolution.getScore());
            double newScore = newSolution.getFitness();
            if (newScore > score) {
                double scoreDiff = score - newScore;
                double temperature = startingTemperature - (i * coolingRate);
                double e = Math.exp(scoreDiff / temperature);
                if (e > Math.random()) {
                    oldSolution = newSolution;
                    if (oldSolution.getFitness() < bestSolution.getFitness()) {
                        bestSolution = oldSolution;
                    }
                }
            } else {
                oldSolution = newSolution;
                if (oldSolution.getFitness() < bestSolution.getFitness()) {
                    bestSolution = oldSolution;
                }
            }
        }

        return bestSolution;
    }

    public Individual getInitIndividual() {
        return initIndividual;
    }

    /**
     * Returns a generated initialization individual for a specific scheduling period.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    private Individual generateInitializationPopulation(SchedulingPeriod period) {
            RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
            return randomConstructionHeuristic.getIndividual(period);
    }
}
