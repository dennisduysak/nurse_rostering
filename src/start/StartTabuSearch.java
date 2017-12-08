package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.sa.SimulatedAnnealing;
import model.schedule.SchedulingPeriod;
import model.ts.TabuSearch;

public class StartTabuSearch extends Basis {

    /**
     * Singleton instance.
     */
    private final static StartTabuSearch instance = new StartTabuSearch();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static StartTabuSearch getInstance() {
        return StartTabuSearch.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private StartTabuSearch() {
    }

    public static void main(String[] args) {
        // read scheduling period information
        String fileName = ConfigurationHelper.getInstance().getProperty("file");
        SchedulingPeriod period = getInstance().parseSchedulingPeriod(fileName);

        // create and run the simulated annealing
        TabuSearch tabuSearch = new TabuSearch();

        Individual best = tabuSearch.optimize(period);
        Individual init = tabuSearch.getInitIndividual();

        System.out.println("InitScore: " + init.getFitness());
        System.out.println("BestScore: " + best.getFitness());
        System.out.println("Difference: " + (init.getFitness() - best.getFitness()));
    }
}
