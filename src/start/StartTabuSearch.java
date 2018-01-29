package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.schedule.SchedulingPeriod;
import model.ts.TabuSearch;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

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

    public static void main(String[] args) throws Exception {
        PrintWriter writer = getPrintWriter();
        writer.println("Dateiname;" +
                "TabuListSize;" +
                "NeighbourSize;" +
                "IterationsAnzahl;" +
                "InitScore;" +
                "BestScore;" +
                "Differenz;" +
                "Laufzeit (ms)");

        // read scheduling period information
        String[] fileNameArray = ConfigurationHelper.getInstance().getPropertyArray("file");

        for (String fileName : fileNameArray) {
            SchedulingPeriod period = getInstance().parseSchedulingPeriod(fileName);
            RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
            Individual initIndividual = randomConstructionHeuristic.getIndividual(period);
            initIndividual.getFitness(true);

            //parameter
            int[] tabuListSize = ConfigurationHelper.getInstance().getPropertyIntArray("ts.TabuListSize");
            int[] neighbourSize = ConfigurationHelper.getInstance().getPropertyIntArray("ts.NeighbourSize");
            int[] maxIterations = ConfigurationHelper.getInstance().getPropertyIntArray("ts.MaxIterations");

            if ((tabuListSize.length != neighbourSize.length) && (neighbourSize.length!= maxIterations.length)) {
                throw new Exception("Field has different length");
            }

            for (int i = 0; i < tabuListSize.length; i++) {
                long timeStart = System.currentTimeMillis();
                // create and run the simulated annealing
                TabuSearch tabuSearch = new TabuSearch(maxIterations[i], neighbourSize[i], tabuListSize[i], initIndividual);
                Individual best = tabuSearch.optimize();
                long timeEnd = System.currentTimeMillis();

                writer.println(fileName + ";" +
                        tabuListSize[i] + ";" +
                        neighbourSize[i] + ";" +
                        maxIterations[i] + ";" +
                        initIndividual.getFitness() + ";" +
                        best.getFitness() + ";" +
                        (initIndividual.getFitness() - best.getFitness()) + ";" +
                        (timeEnd - timeStart));
            }
        }
        writer.close();
    }

    private static PrintWriter getPrintWriter() {
        String fileName = ConfigurationHelper.getInstance().getProperty("fileName");
        String alg = "ts";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("./output/" + alg + "/" + fileName + ".csv", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return writer;
    }
}
