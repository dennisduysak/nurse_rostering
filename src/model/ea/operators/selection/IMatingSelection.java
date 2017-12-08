package model.ea.operators.selection;

import model.ea.Population;

import java.util.List;

/**
 * Defines the general interface for a mating selection operator.
 */
public interface IMatingSelection {
    /**
     * Get a mating selection for a specific population.
     */
    Population select(Population population);
}
