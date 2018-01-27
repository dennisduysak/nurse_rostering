package model.operators;

import helper.RandomHelper;
import model.Individual;
import model.schedule.Employee;
import model.schedule.ShiftType;

import java.util.List;
import java.util.Map;

public class SwappingDayMutation implements IOperator {
    public Individual mutate(Individual individual) {

        Individual mutatedInd = null;
        do {
            mutatedInd = Individual.copy(individual);
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

        mutatedInd.getFitness(true);

        return mutatedInd;
    }
}
