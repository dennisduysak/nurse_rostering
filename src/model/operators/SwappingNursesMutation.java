package model.operators;

import helper.RandomHelper;
import model.Individual;
import model.schedule.Employee;
import model.schedule.ShiftType;

import java.util.List;
import java.util.Map;

//TODO
public class SwappingNursesMutation {

    /**
     * For every individual of the selection swap the first nurse of shift x on a random day
     * with the first nurse of shift x on another random day.
     *
     * @param individual: either the new created children or, in the case without recombination, mutate the selected parents
     * @return selection: the by mutation changed selection
     */
    public Individual mutate(Individual individual) {
        Individual mutatedInd = null;

        do {
            mutatedInd = Individual.copy(individual);

            int numberOfDays = mutatedInd.getDayRosters().size();

            // random number between zero and the number of days in a schedule
            int randDay = RandomHelper.getInstance().getInt(numberOfDays);

            List<Map<ShiftType, Employee>> dayRoster = mutatedInd.getDayRosters().get(randDay).getDayRoster();

            int randShift = RandomHelper.getInstance().getInt(dayRoster.size());

            Map<ShiftType, Employee> nurse1map = dayRoster.get(randShift);
            Employee nurse1 = nurse1map.entrySet().iterator().next().getValue();
            Employee nurse2 = mutatedInd.getPeriod().getRandomEmployee();

            while (nurse1.getId() == nurse2.getId()) {
                nurse2 = mutatedInd.getPeriod().getRandomEmployee();
            }

            //work nurse2 on randDay?
            Map<ShiftType, Employee> nurse2map = null;
            boolean worksOnRandDay = false;
            for (Map<ShiftType, Employee> day : dayRoster) {
                if (day.entrySet().iterator().next().getValue().getId() == nurse2.getId()) {
                    worksOnRandDay = true;
                    nurse2map = day;
                    break;
                }
            }

            if (worksOnRandDay) {
                nurse2map.entrySet().iterator().next().setValue(nurse1);
                nurse1map.entrySet().iterator().next().setValue(nurse2);
            } else {
                dayRoster.get(randShift).entrySet().iterator().next().setValue(nurse2);
            }


        } while (!mutatedInd.isFeasible());
        return mutatedInd;
    }
}
