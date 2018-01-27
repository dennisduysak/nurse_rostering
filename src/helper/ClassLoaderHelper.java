package helper;


import model.ea.operators.enviromentSelection.IEnvironmentSelection;
import model.ea.operators.mutation.IMutation;
import model.ea.operators.recombination.IRecombination;
import model.ea.operators.selection.IMatingSelection;
import model.operators.IOperator;

/**
 * Helper methods for loading correct classes.
 */
public class ClassLoaderHelper {
    /**
     * Singleton instance.
     */
    private final static ClassLoaderHelper instance = new ClassLoaderHelper();

    /**
     * Returns the singleton instance.
     * @return Singleton instance
     */
    public static ClassLoaderHelper getInstance() {
        return ClassLoaderHelper.instance;
    }

    /**
     * Private constructor to avoid bypassing singleton.
     */
    private ClassLoaderHelper() {}

    /**
     * Instantiates a new object from a configuration.
     * @param configurationKey Configuration key
     * @param configurationFallback Fallback configuration
     * @param packagePrefix Prefix for package
     * @return Object instance
     */
    private Object getLoadedClass(String configurationKey, String configurationFallback, String packagePrefix) {
        try {
            // try to instantiate the appropriate object
            Class loadingClass = Class.forName(packagePrefix + ConfigurationHelper.getInstance().
                    getProperty(configurationKey, configurationFallback));
            return loadingClass.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

//    /**
//     * Returns the implementing construction heuristic.
//     * @return Instance that implements IConstructionHeuristic
//     */
//    public IConstructionHeuristic getConstructionHeuristic() {
//        Object instance = getLoadedClass("ConstructionHeuristic", "SimpleConstructionHeuristic", "model.ea.construction.");
//        if (instance == null) {
//            return null;
//        }
//
//        return (IConstructionHeuristic) instance;
//    }

    /**
     * Returns the implementing mating selection operator.
     * @return Instance that implements IConstructionHeuristic
     */
    public IMatingSelection getMatingSelectionOperator() {
        Object instance = getLoadedClass("ea.MatingSelectionOperator", "TruncationMatingSelection", "model.ea.operators.selection.");
        if (instance == null) {
            return null;
        }

        return (IMatingSelection) instance;
    }

    /**
     * Returns the implementing recombination operator.
     * @return Instance that implements IRecombinationOperator
     */
    public IRecombination getRecombinationOperator() {
        Object instance = getLoadedClass("ea.RecombinationOperator", "OnePointCrossover", "model.ea.operators.recombination.");
        if (instance == null) {
            return null;
        }

        return (IRecombination) instance;
    }

    /**
     * Returns the implementing mutation operator.
     * @return Instance that implements IMutationOperator
     */
    public IMutation getMutationOperator() {
        Object instance = getLoadedClass("ea.MutationOperator", "ea.SwappingNursesMutation", "model.ea.operators.mutation.");
        if (instance == null) {
            return null;
        }

        return (IMutation) instance;
    }

    /**
     * Returns the implementing environmental selection operator.
     * @return Instance that implements IEnvironmentSelectionOperator
     */
    public IEnvironmentSelection getEnvironmentSelectionOperator() {
        Object instance = getLoadedClass("ea.EnvironmentSelectionOperator", "ea.SimpleEnvironmentSelection", "model.ea.operators.enviromentSelection.");
        if (instance == null) {
            return null;
        }

        return (IEnvironmentSelection) instance;
    }

    /**
     * Returns the implementing mating selection operator.
     * @return Instance that implements IConstructionHeuristic
     */
    public IOperator getSwappingOperator() {
        Object instance = getLoadedClass("sa.Operator", "SwappingNursesMutation", "model.operators.");
        if (instance == null) {
            return null;
        }

        return (IOperator) instance;
    }
}
