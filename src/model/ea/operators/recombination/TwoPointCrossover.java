package model.ea.operators.recombination;

import model.Individual;
import model.ea.Population;

/**
 * takes the first two individuals from the list of parents
 * and creates children with 40/60 division
 */
@SuppressWarnings("unused")
public class TwoPointCrossover implements IRecombination {
    @Override
    public Population recombine(Population parents) {
        Population children = new Population();

        int numberOfDays = parents.getPool().get(0).getDayRosters().size();
        int half = numberOfDays/2;
        int interface1 = (int) (half * 0.7);
        int interface2 = (int) (half * 1.3);

        for (int j = 0; j < parents.getPool().size()/2; j++) {
            Individual parent1 = parents.getPool().get(j);
            Individual parent2 = parents.getPool().get(parents.getPool().size()-1-j);

            Individual child1 = Individual.copy(parent1);
            for (int i = interface1; i < interface2; i++) {
                child1.getDayRosters().set(i, parent2.getDayRosters().get(i));
            }
            child1.getFitness(true);
            children.addIndividualToPool(child1);

            Individual child2 = Individual.copy(parent2);
            for (int i = interface1; i < interface2; i++) {
                child2.getDayRosters().set(i, parent1.getDayRosters().get(i));
            }
            child2.getFitness(true);
            children.addIndividualToPool(child2);
        }
        return children;
    }
}
