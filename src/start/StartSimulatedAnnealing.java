package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.sa.SimulatedAnnealing;
import model.schedule.SchedulingPeriod;

public class StartSimulatedAnnealing extends Basis {
    /**
     * Singleton instance.
     */
    private final static StartSimulatedAnnealing instance = new StartSimulatedAnnealing();

    /**
     * Returns the singleton instance.
     * @return Singleton instance
     */
    public static StartSimulatedAnnealing getInstance() {
        return StartSimulatedAnnealing.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private StartSimulatedAnnealing() {}

    /**
     * Default main method.
     */
    public static void main(String[] args) {
        // read scheduling period information
        String fileName = ConfigurationHelper.getInstance().getProperty("file");
        SchedulingPeriod period = getInstance().parseSchedulingPeriod(fileName);

        // create and run the simulated annealing
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
        Individual best = simulatedAnnealing.optimize(period);
        Individual init = simulatedAnnealing.getInitIndividual();

        System.out.println("InitScore: " + init.getFitness());
        System.out.println("BestScore: " + best.getFitness());
        System.out.println("Difference: " + (init.getFitness() - best.getFitness()));
    }
}
