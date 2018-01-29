package model.ea.operators.selection;

import helper.ConfigurationHelper;
import model.ea.Population;

public class BestIndividualSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("ea.NumberOfParents");
	 
	/**
	 * Takes the population after Recombination and Mutation selects
	 * the number of needed best individuals from parents and children.
	 * @param population Population instance
	 */
	@Override
	public Population select(Population population) {
		population.sortByFitness();
		int poolSize = population.getPool().size()-1;
		Population selection = new Population();
		for (int i = 0; i < numberOfParents; i++) {
			selection.addIndividualToPool(population.getPool().get(i));
		}
		return selection;
	}
}
