package model.ea.operators.selection;

import helper.ConfigurationHelper;
import model.ea.Population;

import java.util.Random;

@SuppressWarnings("unused")
public class RouletteWheelMatingSelection implements IMatingSelection {
	private int numberOfParents = ConfigurationHelper.getInstance().getPropertyInteger("ea.NumberOfParents");

	@Override
	public Population select(Population population) {

		int populationSize = population.getPool().size();
		float[] cumulatedFitness = new float[populationSize];
		cumulatedFitness[0] = population.getPool().get(0).getFitness();

		// We have to calculated the fitness of the individual before the loop, otherwise we get an
		// ArrayOutOfBounds exception. Therefore we begin with i = 1.
		for (int i = 1; i < populationSize ; i++) {
			cumulatedFitness[i] = cumulatedFitness[i-1] + population.getPool().get(i).getFitness(); 
		}

        Population selection = new Population();
		for (int i = 0; i < numberOfParents; i++) {
			int j = 0;
			int u = new Random().nextInt((int) cumulatedFitness[populationSize-1] + 1);
			while (cumulatedFitness[j] < u) {
				j ++;
			    selection.addIndividualToPool(population.getPool().get(j));
			}
		}

		return selection;
	}
}
