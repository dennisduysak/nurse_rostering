package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.ea.EvolutionaryCycle;
import model.ea.Population;
import model.schedule.SchedulingPeriod;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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
     *
     * @return Singleton instance
     */
    public static StartEvolutionaryAlgorithm getInstance() {
        return StartEvolutionaryAlgorithm.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private StartEvolutionaryAlgorithm() {
    }

    /**
     * Default main method.
     */
    public static void main(String[] args) throws Exception {
        PrintWriter writer = getPrintWriter();
        writer.println("Dateiname;" +
                "Populationsgröße;" +
                "Eltern;" +
                "Rekombination;" +
                "Mutation;" +
                "PaarungsSelektionsop.;" +
                "Mutationsop.;" +
                "Rekombinationsop.;" +
                "PopulationsSelektionsop.;" +
                "Iteration;" +
                "InitScore;" +
                "BestScore;" +
                "Differenz;" +
                "Laufzeit (ms)");
        // read scheduling period information
        String[] fileName = ConfigurationHelper.getInstance().getPropertyArray("file");

        int lenght = ConfigurationHelper.getInstance().getPropertyArray("ea.UseMutationArray").length;

        for (String file : fileName) {
            for (int i = 0; i < lenght; i++) {
                setParameters(i);
                ConfigurationHelper.getInstance().setProperty("ea.NumberOfParents", "4");

                SchedulingPeriod period = getInstance().parseSchedulingPeriod(file);
                // create and run the evolutionary cycle
                long timeStart = System.currentTimeMillis();
                EvolutionaryCycle evolutionaryCycle = new EvolutionaryCycle();
                Population evolutionizedPopulation = evolutionaryCycle.evolutionize(period);

                // retrieve best individual from initialized and evolutionized populations
                Individual bestInitialized = evolutionaryCycle.getInitPopulation().getBestIndividual();
                Individual best = evolutionizedPopulation.getBestIndividual();
                long timeEnd = System.currentTimeMillis();


                String output = file + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.IndividualsPerPopulation") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.NumberOfParents") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.UseRecombination") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.UseMutation") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.MatingSelectionOperator") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.MutationOperator") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.RecombinationOperator") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.EnvironmentSelectionOperator") + ";" +
                        ConfigurationHelper.getInstance().getProperty("ea.MaxIterations") + ";" +
                        bestInitialized.getFitness() + ";" +
                        best.getFitness() + ";" +
                        (bestInitialized.getFitness() - best.getFitness()) + ";" +
                        (timeEnd - timeStart);
                writer.println(output);
            }
        }
        writer.close();
    }

    private static void setParameters(int i) throws Exception {
        String[] individualsPerPopulationArray = ConfigurationHelper.getInstance().getPropertyArray("ea.IndividualsPerPopulationArray");
        String[] numberOfParentsArray = ConfigurationHelper.getInstance().getPropertyArray("ea.NumberOfParentsArray");
        String[] useRecombinationArray = ConfigurationHelper.getInstance().getPropertyArray("ea.UseRecombinationArray");
        String[] useMutationArray = ConfigurationHelper.getInstance().getPropertyArray("ea.UseMutationArray");
        String[] matingSelectionOperatorArray = ConfigurationHelper.getInstance().getPropertyArray("ea.MatingSelectionOperatorArray");
        String[] mutationOperatorArray = ConfigurationHelper.getInstance().getPropertyArray("ea.MutationOperatorArray");
        String[] recombinationOperatorArray = ConfigurationHelper.getInstance().getPropertyArray("ea.RecombinationOperatorArray");
        String[] environmentSelectionOperatorArray = ConfigurationHelper.getInstance().getPropertyArray("ea.EnvironmentSelectionOperatorArray");
        String[] maxIterationsArray = ConfigurationHelper.getInstance().getPropertyArray("ea.MaxIterationsArray");

        if ((individualsPerPopulationArray.length != numberOfParentsArray.length) &&
                (numberOfParentsArray.length != useRecombinationArray.length) &&
                (useRecombinationArray.length != useMutationArray.length) &&
                (useMutationArray.length != matingSelectionOperatorArray.length) &&
                (matingSelectionOperatorArray.length != mutationOperatorArray.length) &&
                (mutationOperatorArray.length != recombinationOperatorArray.length) &&
                (recombinationOperatorArray.length != environmentSelectionOperatorArray.length) &&
                (environmentSelectionOperatorArray.length != maxIterationsArray.length)) {
            throw new Exception("Field has not the same lenght");
        }


        ConfigurationHelper.getInstance().setProperty("ea.IndividualsPerPopulation", individualsPerPopulationArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.NumberOfParents", numberOfParentsArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.UseRecombination", useRecombinationArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.UseMutation", useMutationArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.MatingSelectionOperator", matingSelectionOperatorArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.MutationOperator", mutationOperatorArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.RecombinationOperator", recombinationOperatorArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.EnvironmentSelectionOperator", environmentSelectionOperatorArray[i]);
        ConfigurationHelper.getInstance().setProperty("ea.MaxIterations", maxIterationsArray[i]);


    }

    private static PrintWriter getPrintWriter() {
        String fileName = ConfigurationHelper.getInstance().getProperty("fileName");
        String alg = "ea";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("./output/" + alg + "/" + fileName + ".csv", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return writer;
    }


}
