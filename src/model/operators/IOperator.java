package model.operators;

import model.Individual;

public interface IOperator {
    /**
     * Mutates selected individuals.
     */
    Individual mutate(Individual individual);
}
