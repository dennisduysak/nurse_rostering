package model.ea.operators.mutation;

import helper.RandomHelper;
import model.Individual;
import model.ea.Population;
import model.schedule.Employee;
import model.schedule.ShiftType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unusued")
public class SwappingDaysMutation implements IMutation {
    /**
     * For every individual of the selection swap the first nurse of shift x on a random day
     * with the first nurse of shift x on another random day.
     *
     * @param selection: either the new created children or, in the case without recombination, mutate the selected parents
     * @return selection: the by mutation changed selection
     */
    @Override
    public Population mutate(Population selection) {

        //for (Individual individual : selection.getPool()) {
        for (int i = 0; i < selection.getPool().size(); i++) {

            Individual mutatedInd = null;
            do {
                mutatedInd = Individual.copy(selection.getPool().get(i));
                int numberOfDays = mutatedInd.getDayRosters().size();

                // random number between zero and the number of days in a schedule
                int randDay1 = RandomHelper.getInstance().getInt(numberOfDays);
                int randDay2;
                do {
                    randDay2 = RandomHelper.getInstance().getInt(numberOfDays);
                } while (!mutatedInd.getDayRosters().get(randDay1).getDay().equals(mutatedInd.getDayRosters().get(randDay2).getDay()) ||
                        randDay1 == randDay2);

                List<Map<ShiftType, Employee>> dayRoster1 = mutatedInd.getDayRosters().get(randDay1).getDayRoster();
                List<Map<ShiftType, Employee>> dayRoster2 = mutatedInd.getDayRosters().get(randDay2).getDayRoster();


                mutatedInd.getDayRosters().get(randDay1).setRoster(dayRoster2);
                mutatedInd.getDayRosters().get(randDay2).setRoster(dayRoster1);
            } while (!mutatedInd.isFeasible());

            //update fitness
            mutatedInd.getFitness(true);
            selection.getPool().set(i, mutatedInd);

        }

        return selection;
    }
}
