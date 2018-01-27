package start;

import helper.ConfigurationHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.sa.SimulatedAnnealing;
import model.schedule.SchedulingPeriod;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class StartSimulatedAnnealing extends Basis {
    /**
     * Singleton instance.
     */
    private final static StartSimulatedAnnealing instance = new StartSimulatedAnnealing();

    /**
     * Returns the singleton instance.
     *
     * @return Singleton instance
     */
    public static StartSimulatedAnnealing getInstance() {
        return StartSimulatedAnnealing.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private StartSimulatedAnnealing() {
    }

    public static void main(String[] args) throws Exception {
        PrintWriter writer = getPrintWriter();
        Boolean useLinear = ConfigurationHelper.getInstance().getPropertyBoolean("sa.UseLinear");
        int[] maxIterationArray = ConfigurationHelper.getInstance().getPropertyIntArray("sa.MaxIteration");

        // read scheduling period information
        String[] fileNameArray = ConfigurationHelper.getInstance().getPropertyArray("file");
        writer.println("Dateiname;" +
                "StartTemperatur;" +
                "CoolingRate;" +
                "IterationsAnzahl;" +
                "InitScore;" +
                "BestScore;" +
                "Differenz;" +
                "Laufzeit");

        for (String fileName : fileNameArray) {
            SchedulingPeriod period = getInstance().parseSchedulingPeriod(fileName);
            RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
            Individual initIndividual = randomConstructionHeuristic.getIndividual(period);

            //parameter
            double[] startingTemperatureArray = ConfigurationHelper.getInstance().getPropertyDoubleArray(
                    "sa.StartTemperature");
            double[] coolingRateArray = ConfigurationHelper.getInstance().getPropertyDoubleArray("sa.CoolingRate");
            if (startingTemperatureArray.length != coolingRateArray.length) {
                throw new Exception("Field has different length");
            }
            for (int i = 0; i < startingTemperatureArray.length; i++) {
                double startingTemperature = startingTemperatureArray[i];
                double coolingRate = coolingRateArray[i];
                long timeStart = System.currentTimeMillis();

                // create and run the simulated annealing
                int numberOfIterations = useLinear ? (int) (startingTemperature / coolingRate) : maxIterationArray[i];

                SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(startingTemperature, coolingRate,
                        numberOfIterations, initIndividual);
                float avg = 0;
                int repeat = 1;
                for (int k = 0; k < repeat; k++) {
                    Individual sol = simulatedAnnealing.optimize(period);
                    avg += sol.getFitness();
                }
                avg = avg / repeat;
                Individual init = simulatedAnnealing.getInitIndividual();
                long timeEnd = System.currentTimeMillis();

                String output = fileName + ";" +
                        startingTemperature + ";" +
                        coolingRate + ";" +
                        numberOfIterations + ";" +
                        init.getFitness() + ";" +
                        //best.getFitness() + ";" +
                        avg + ";" +
                        (init.getFitness() - avg) + ";" +
                        (timeEnd - timeStart) / repeat;
                writer.println(output);
            }
        }
        writer.close();
    }

    private static PrintWriter getPrintWriter() {
        String fileName = ConfigurationHelper.getInstance().getProperty("fileName");
        String alg = "sa";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("./output/" + alg + "/" + fileName + ".csv", "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return writer;
    }
}

