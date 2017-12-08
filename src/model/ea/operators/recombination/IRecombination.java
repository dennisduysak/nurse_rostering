package model.ea.operators.recombination;

import model.ea.Population;

/**
 * Defines the general interface for a recombination operator.
 */
public interface IRecombination {
    /**
     * Recombines individuals.
     */
    Population recombine(Population parents);
}
