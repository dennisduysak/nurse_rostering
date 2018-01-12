package model.ea.operators.mutation;

import helper.RandomHelper;
import model.Individual;
import model.ea.Population;
import model.schedule.Employee;
import model.schedule.ShiftType;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unusued")
public class SwappingNursesMutation implements IMutation {
    /**
     * For every individual of the selection swap the first nurse of shift x on a random day
     * with the first nurse of shift x on another random day.
     *
     * @param selection: either the new created children or, in the case without recombination, mutate the selected parents
     * @return selection: the by mutation changed selection
     */
    @Override
    public Population mutate(Population selection) {

        for (Individual individual : selection.getPool()) {

            do {
                //get random employee of random workingday
                int numberOfDays = individual.getDayRosters().size();
                int randDay = RandomHelper.getInstance().getInt(numberOfDays);
                List<Map<ShiftType, Employee>> dayRoster = individual.getDayRosters().get(randDay).getDayRoster();
                int randShift = RandomHelper.getInstance().getInt(dayRoster.size());
                Map<ShiftType, Employee> nurse1map = dayRoster.get(randShift);
                Employee nurse1 = nurse1map.entrySet().iterator().next().getValue();

                //get second random employee from scheduleperiod
                Employee nurse2 = individual.getPeriod().getRandomEmployee();

                while (nurse1.getId() == nurse2.getId()) {
                    nurse2 = individual.getPeriod().getRandomEmployee();
                }

                //work nurse2 on random day?
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

            } while (!individual.isFeasible());

            //update fitness
            individual.getFitness(true);
        }

        return selection;
    }
}
