package model.ea;


import helper.ClassLoaderHelper;
import helper.ConfigurationHelper;
import model.Individual;
import model.construction.RandomConstructionHeuristic;
import model.ea.operators.enviromentSelection.IEnvironmentSelection;
import model.ea.operators.mutation.IMutation;
import model.ea.operators.recombination.IRecombination;
import model.ea.operators.selection.IMatingSelection;
import model.schedule.SchedulingPeriod;

/**
 * This class implements an evolutionary cycle for the evolutionary algorithm.
 */
public class EvolutionaryCycle {
    /**
     * Current iteration.
     */
    private int iteration;

    /**
     * Maximum number of iterations.
     */
    private int maxIterations;

    /**
     * The recombination operator is used, if true.
     */
    private boolean useRecombination;

    /**
     * The mutation operator is used, if true.
     */
    private boolean useMutation;

    /**
     * Holds the mating selection operator instance.
     */
    private IMatingSelection matingSelectionOperator;

    /**
     * Holds the recombination operator instance.
     */
    private IRecombination recombinationOperator;

    /**
     * Holds the mutation operator instance.
     */
    private IMutation mutationOperator;

    /**
     * Holds the environment selection operator instance.
     */
    private IEnvironmentSelection environmentSelectionOperator;

    /**
     * Holds the initializing population (for benchmarking purposes against last solutions).
     */
    private Population initPopulation;

    public EvolutionaryCycle() {
        this.iteration = 0;
        this.maxIterations = ConfigurationHelper.getInstance().getPropertyInteger("ea.MaxIterations");
        this.useRecombination  = ConfigurationHelper.getInstance().getPropertyBoolean("ea.UseRecombination");
        this.useMutation = ConfigurationHelper.getInstance().getPropertyBoolean("ea.UseMutation");
        this.matingSelectionOperator = ClassLoaderHelper.getInstance().getMatingSelectionOperator();
        this.recombinationOperator = ClassLoaderHelper.getInstance().getRecombinationOperator();
        this.mutationOperator = ClassLoaderHelper.getInstance().getMutationOperator();
        this.environmentSelectionOperator = ClassLoaderHelper.getInstance().getEnvironmentSelectionOperator();
        this.initPopulation = null;
    }

    /**
     * Returns the initializing population.
     * @return Initialization population instance
     */
    public Population getInitPopulation() {
        return initPopulation;
    }

    /**
     * Runs the evolutionary cycle.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    public Population evolutionize(SchedulingPeriod period) {
        initPopulation = generateInitializationPopulation(period);
        initPopulation.benchmark();

        Population population = Population.copy(initPopulation);
        Population children;

        // evolutionize cycle, while termination condition is not met
        while (!isTerminationCondition()) {
            // get selection of mating individuals
            Population parents = matingSelectionOperator.select(population);
            children = new Population();

            // recombine individuals (if used)
            if (useRecombination) {
                 children = recombinationOperator.recombine(parents);
            }

            // mutate individuals (if used)
            if (useMutation) {
            	// if recombination was used, mutate the new created children
            	if (useRecombination) {
            		children = mutationOperator.mutate(children);
            	} else {
                    // if not, mutate the selected parents
                    children = mutationOperator.mutate(parents);
            	}
            }

            // benchmark new generation
            population.addIndividualsToPool(children.getPool());
            population.benchmark();

            // get environmental selection from new generation
            environmentSelectionOperator.select(population);
            population.benchmark();
        }

        // cycle is terminated, return the latest solution or initialized
        // population, of solution is not better
        return population.getBestIndividual().getFitness()
                <= initPopulation.getBestIndividual().getFitness()
            ? population
            : initPopulation;
    }

    /**
     * Returns true, if the termination condition is met.
     * @return True, if termination condition is met
     */
    private boolean isTerminationCondition() {
        System.out.println(++iteration + ". Iteration");
        return iteration > maxIterations;
    }

    /**
     * Returns a generated initialization population for a specific scheduling period.
     * @param period SchedulingPeriod instance
     * @return Population instance
     */
    private Population generateInitializationPopulation(SchedulingPeriod period) {
        Population population = new Population();

        int individualsPerPopulation = ConfigurationHelper.getInstance().getPropertyInteger("ea.IndividualsPerPopulation");
        for (int i = 0; i < individualsPerPopulation; i++) {
            RandomConstructionHeuristic randomConstructionHeuristic = new RandomConstructionHeuristic();
            Individual individual = randomConstructionHeuristic.getIndividual(period);
            population.addIndividualToPool(individual);
        }

        return population;
    }
}
