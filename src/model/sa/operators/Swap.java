package model.sa.operators;

import helper.RandomHelper;
import model.Individual;
import model.ea.Population;
import model.schedule.Employee;
import model.schedule.ShiftType;

import java.util.List;
import java.util.Map;

public class Swap {
    public Individual mutate(Individual individual) {
        Individual mutatedInd = Individual.copy(individual);

        int numberOfDays = mutatedInd.getDayRosters().size();

            boolean feasible = true;
            do {
                feasible = true;
                // random number between zero and the number of days in a schedule
                int randDay1;
                int randDay2;
                do {
                    randDay1 = RandomHelper.getInstance().getInt(numberOfDays);
                    randDay2 = RandomHelper.getInstance().getInt(numberOfDays);
                }
                while (randDay1 == randDay2);

                List<Map<ShiftType, Employee>> dayRoster1 = mutatedInd.getDayRosters().get(randDay1).getDayRoster();
                List<Map<ShiftType, Employee>> dayRoster2 = mutatedInd.getDayRosters().get(randDay2).getDayRoster();

                // get number of (maximum) shifts
                int numberOfShifts = dayRoster1.size() <= dayRoster2.size() ? dayRoster1.size() : dayRoster2.size();

                // random number between zero and the number of shifts in a day roster
                int randShift = RandomHelper.getInstance().getInt(numberOfShifts);

                // the nurse of a random shift on a random day
                Employee nurse1 = dayRoster1.get(randShift).entrySet().iterator().next().getValue();
                // the nurse of the same shift on another random day
                Employee nurse2 = dayRoster2.get(randShift).entrySet().iterator().next().getValue();

                ShiftType st = mutatedInd.getDayRosters().get(randDay1).getShiftTypeForEmployee(nurse1);

                // swap the nurses
                dayRoster1.get(randShift).replace(st, nurse2);
                dayRoster2.get(randShift).replace(st, nurse1);

                // swap back if solution isn't feasible anymore
                if (!mutatedInd.isFeasible()) {
                    dayRoster1.get(randShift).replace(st, nurse1);
                    dayRoster2.get(randShift).replace(st, nurse2);
                    feasible = false;
                }
            } while (!feasible);

        mutatedInd.getFitness(true);
        return mutatedInd;
    }
}
