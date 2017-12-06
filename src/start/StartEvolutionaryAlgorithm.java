package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.ea.EvolutionaryCycle;
import model.ea.Population;
import model.schedule.SchedulingPeriod;

/**
 * StartEvolutionaryAlgorithm class to initialize the application.
 */
public class StartEvolutionaryAlgorithm extends Basis {
    /**
     * Singleton instance.
     */
    private final static StartEvolutionaryAlgorithm instance = new StartEvolutionaryAlgorithm();

    /**
     * Returns the singleton instance.
     * @return Singleton instance
     */
    public static StartEvolutionaryAlgorithm getInstance() {
        return StartEvolutionaryAlgorithm.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private StartEvolutionaryAlgorithm() {}

    /**
     * Default main method.
     */
    public static void main(String[] args) {
        // read scheduling period information
        String fileName = ConfigurationHelper.getInstance().getProperty("file");
        SchedulingPeriod period = getInstance().parseSchedulingPeriod(fileName);

        // create and run the evolutionary cycle
        EvolutionaryCycle evolutionaryCycle = new EvolutionaryCycle();
        Population evolutionizedPopulation = evolutionaryCycle.evolutionize(period);

        // retrieve best individual from initialized and evolutionized populations
        Individual bestInitialized = evolutionaryCycle.getInitPopulation().getBestIndividual();
        Individual best = evolutionizedPopulation.getBestIndividual();

 /*       TuiHelper.getInstance().showEAResult(best, bestInitialized);
        System.out.println(StartEvolutionaryAlgorithm.getInstance().writeSolutionFile(best));*/
    }

    /**
     * Writes a solution file.
     * @param individual Individual instance
     * @return Full path of written file
     */
 //   private String writeSolutionFile(Individual individual) {
   /*     // check, if we need to write a solution file
        if (!ConfigurationHelper.getInstance().getPropertyBoolean("SolutionWrite")) {
            return "\"SolutionWrite\" in config.properties is set to \"false\", no solution file written.";
        }

        IWriter writer = ClassLoaderHelper.getInstance().getWriter();
        try {
            return "Solution file written to: " + writer.writeFile(individual);
        } catch (Exception e) {
            return "Error writing solution file: " + e.getMessage();
        }
    }*/


}
