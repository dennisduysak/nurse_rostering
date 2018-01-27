package model.sa;

import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import model.Individual;
import model.operators.IOperator;
import model.schedule.SchedulingPeriod;

public class SimulatedAnnealing {

    /**
     * Maximum number of iterations.
     */
    private double startingTemperature;

    /**
     * Maximum number of TabuList.
     */
    private double coolingRate;

    /**
     * Maximum of iteration
     */
    private int maxIterations;

    /**
     * Holds the mating selection operator instance.
     */
    private IOperator swappingOperator = ClassLoaderHelper.getInstance().getSwappingOperator();

    /**
     * The linear cooling is used, if true.
     */
    private boolean isLinear = ConfigurationHelper.getInstance().getPropertyBoolean("sa.UseLinear");

    /**
     * Holds the initializing individual (for benchmarking purposes against last solutions).
     */
    private Individual initIndividual;

    public SimulatedAnnealing(double startingTemperature, double coolingRate, int maxIterations, Individual initIndividual) {
        this.startingTemperature = startingTemperature;
        this.coolingRate = coolingRate;
        this.maxIterations = maxIterations;
        this.initIndividual = initIndividual;
    }

    public Individual optimize(SchedulingPeriod period) {
        initIndividual.getFitness(true);
        if (isLinear){
            return optimizeLinear(period);
        } else {
            return optimizeGeometric(period);
        }
    }

    private Individual optimizeLinear(SchedulingPeriod period) {
        //get initialsolution
        //RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
        //initIndividual = randomConstructionHeuristic.getIndividual(period);
        //initIndividual.getFitness(true);

        Individual oldSolution = Individual.copy(initIndividual);
        Individual bestSolution = oldSolution;

        for (int i = 0; i < maxIterations; i++) {
            double score = oldSolution.getFitness();
            Individual newSolution = swappingOperator.mutate(oldSolution);
            double newScore = newSolution.getFitness();
            //System.out.println("\t" + (i + 1) + ".Iteration - Score: " + newSolution.getFitness());
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

    public Individual optimizeGeometric(SchedulingPeriod period) {
        //get initialsolution
        //RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
        //initIndividual = randomConstructionHeuristic.getIndividual(period);
        //initIndividual.getFitness(true);

        Individual oldSolution = Individual.copy(initIndividual);
        double temperatur = startingTemperature;
        Individual bestSolution = oldSolution;

        for (int i = 0; i < maxIterations; i++) {
            double score = oldSolution.getFitness();
            temperatur = temperatur * coolingRate;
            Individual newSolution = swappingOperator.mutate(oldSolution);
            double newScore = newSolution.getFitness();
            //System.out.println("\t" + (i + 1) + ".Iteration - Score: " + newSolution.getFitness());
            if (newScore > score) {
                double scoreDiff = score - newScore;
                double e = Math.exp(scoreDiff / temperatur);
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
}
