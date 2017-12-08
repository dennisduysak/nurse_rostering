package model.ea.operators.mutation;

import model.ea.Population;

/**
 * Defines the general interface for a mutation operator.
 */
public interface IMutation {
    /**
     * Mutates selected individuals.
     */
    Population mutate(Population selection);
}
