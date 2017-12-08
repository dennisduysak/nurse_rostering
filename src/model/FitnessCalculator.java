package model;

import model.schedule.Attribute;
import model.schedule.Employee;
import model.schedule.SchedulingPeriod;

/**
 * Implements the default fitness calculator which uses algorithms for each
 * soft constraint to calculate the overall fitness.
 */
@SuppressWarnings("unused")
public class FitnessCalculator {
    /**
     * Holds the current individual instance.
     */
    private Individual individual;

    /**
     * Holds the scheduling period instance.
     */
    private SchedulingPeriod period;

    public float calculate(Individual individual, SchedulingPeriod period) {
        float fitness = 0;
        this.individual = individual;
        this.period = period;

        // calculate each deviations
        fitness += getDeviationNumAssignments();
        fitness += getDeviationNumConsecutiveDays();
        fitness += getDeviationWorkingWeekends();
        fitness += getDeviationUnwantedPatterns();
        fitness += getDeviationOffRequests();

        return fitness;
    }

    /**
     * Calculates the deviation for Min/MaxNumAssignments for each employee.
     * @return Deviation
     */
    private float getDeviationNumAssignments() {
       float deviation = 0;

       // calculate for each employee
       for (Employee employee: period.getEmployees()) {
           Attribute minNumAssignments = employee.getContract().getMinNumAssignments();
           Attribute maxNumAssignments = employee.getContract().getMaxNumAssignments();
           int weightMin = minNumAssignments.getWeight();
           int valueMin = minNumAssignments.getValueInt();
           int weightMax = maxNumAssignments.getWeight();
           int valueMax = maxNumAssignments.getValueInt();
           int numAssignments = individual.getNumAssignments(employee);

           // if soft-constraints are unsatisfied, add deviation
           if (numAssignments < valueMin) {
               deviation += (valueMin - numAssignments) * weightMin;
           }
           if (numAssignments > valueMax) {
               deviation += (numAssignments - valueMax) * weightMax;
           }
       }

       return deviation;
    }

    /**
     * Calculates the deviations for Min/MaxNumConsecutive(Free)Days for each employee.
     * @return Deviation
     */
    private float getDeviationNumConsecutiveDays() {
        float deviation = 0;

        // calculate for each employee
        for (Employee employee: period.getEmployees()) {
            /*Attribute minNumConsecutiveWorkDays = employee.getContract().getMinConsecutiveWorkingDays();
            Attribute maxNumConsecutiveWorkDays = employee.getContract().getMaxConsecutiveWorkingDays();
            Attribute minNumConsecutiveFreeDays = employee.getContract().getMinConsecutiveFreeDays();
            Attribute maxNumConsecutiveFreeDays = employee.getContract().getMaxConsecutiveFreeDays();

            int weightMinWork = minNumConsecutiveWorkDays.getWeight();
            int weightMaxWork = maxNumConsecutiveWorkDays.getWeight();
            int weightMinFree = minNumConsecutiveFreeDays.getWeight();
            int weightMaxFree = maxNumConsecutiveFreeDays.getWeight();

            int valueMinWork = minNumConsecutiveWorkDays.getValueInt();
            int valueMaxWork = maxNumConsecutiveWorkDays.getValueInt();
            int valueMinFree = minNumConsecutiveFreeDays.getValueInt();
            int valueMaxFree = maxNumConsecutiveFreeDays.getValueInt();

            int numMinConsecutiveWork = individual.getNumMinConsecutiveWork(employee);
            int numMaxConsecutiveWork = individual.getNumMaxConsecutiveWork(employee);
            int numMinConsecutiveFree = individual.getNumMinConsecutiveFree(employee);
            int numMaxConsecutiveFree = individual.getNumMaxConsecutiveFree(employee);

            // if soft-constraints are unsatisfied, add deviation
            if (numMinConsecutiveWork < valueMinWork) {
                deviation += (valueMinWork - numMinConsecutiveWork) * weightMinWork;
            }
            if (numMaxConsecutiveWork > valueMaxWork) {
                deviation += (numMaxConsecutiveWork - valueMaxWork) * weightMaxWork;
            }
            if (numMinConsecutiveFree < valueMinFree) {
                deviation += (valueMinFree - numMinConsecutiveFree) * weightMinFree;
            }
            if (numMaxConsecutiveFree > valueMaxFree) {
                deviation += (numMaxConsecutiveFree - valueMaxFree) * weightMaxFree;
            }*/
            deviation += individual.getNumMinConsecutiveWork(employee);
            deviation += individual.getNumMaxConsecutiveWork(employee);
            deviation += individual.getNumMinConsecutiveFree(employee);
            deviation += individual.getNumMaxConsecutiveFree(employee);
        }

        return deviation;
    }

    /**
     * Calculates the deviations for (consecutive) working weekends for each employee.
     * @return Deviation
     */
    private float getDeviationWorkingWeekends() {
        float deviation = 0;

        // calculate for each employee
        for (Employee employee: period.getEmployees()) {
            Attribute maxWorkingWeekendsTotal = employee.getContract().getMaxWorkingWeekendsInFourWeeks();
            Attribute minNumConsecutiveWorkWeekends = employee.getContract().getMinConsecutiveWorkingWeekends();
            Attribute maxNumConsecutiveWorkWeekends = employee.getContract().getMaxConsecutiveWorkingWeekends();
            Attribute completeWeekends = employee.getContract().getCompleteWeekends();
            Attribute identicalShiftTypes = employee.getContract().getIdenticalShiftTypesDuringWeekend();
            Attribute noNightShiftsBeforeWeekends = employee.getContract().getNoNightShiftBeforeFreeWeekend();

            int weightMaxWorkingWeekendsTotal = maxWorkingWeekendsTotal.getWeight();
            int weightMinConsecutiveWork = minNumConsecutiveWorkWeekends.getWeight();
            int weightMaxConsecutiveWork = maxNumConsecutiveWorkWeekends.getWeight();
            int weightCompleteWeekends = completeWeekends.getWeight();
            int weightIdenticalShiftTypes = identicalShiftTypes.getWeight();
            int weightNoNightShiftsBeforeWeekends = noNightShiftsBeforeWeekends.getWeight();

            int valueMaxWorkingWeekendsTotal = maxWorkingWeekendsTotal.getValueInt();
            int valueMinConsecutiveWork = minNumConsecutiveWorkWeekends.getValueInt();
            int valueMaxConsecutiveWork = maxNumConsecutiveWorkWeekends.getValueInt();

            boolean valueCompleteWeekends = completeWeekends.getValueBoolean();
            boolean valueIdenticalShiftTypes = identicalShiftTypes.getValueBoolean();
            boolean valueNoNightShiftsBeforeWeekends = noNightShiftsBeforeWeekends.getValueBoolean();

            int numWeekendsWorkTotal = individual.getNumTotalWeekendsWork(employee);
            int numMinConsecutiveWeekendsWork = individual.getNumMinConsecutiveWeekendsWork(employee);
            int numMaxConsecutiveWeekendsWork = individual.getNumMaxConsecutiveWeekendsWork(employee);

            // if soft-constraints are unsatisfied, add deviation
            if (numWeekendsWorkTotal > valueMaxWorkingWeekendsTotal) {
                deviation += (numWeekendsWorkTotal - valueMaxWorkingWeekendsTotal);// * weightMaxWorkingWeekendsTotal;
            }
            if (numMinConsecutiveWeekendsWork < valueMinConsecutiveWork) {
                deviation += (valueMinConsecutiveWork - numMinConsecutiveWeekendsWork);// * weightMinConsecutiveWork;
            }
            if (numMaxConsecutiveWeekendsWork > valueMaxConsecutiveWork) {
                deviation += (numMaxConsecutiveWeekendsWork - valueMaxConsecutiveWork);// * weightMaxConsecutiveWork;
            }
            if (valueCompleteWeekends != individual.isCompleteWeekends(employee)) {
                deviation += weightCompleteWeekends;
            }
            if (valueIdenticalShiftTypes != individual.isIdenticalShiftType(employee)) {
                deviation += weightIdenticalShiftTypes;
            }
            if (valueNoNightShiftsBeforeWeekends != individual.isNoNightShiftsBeforeFreeWeekend(employee)) {
                deviation += weightNoNightShiftsBeforeWeekends;
            }
        }

        return deviation;
    }

    /**
     * Calculates the deviations for unwanted patterns for each employee.
     * @return Deviation
     */
    private float getDeviationUnwantedPatterns() {
        float deviation = 0;

        // calculate deviation for each employee
        for (Employee employee: period.getEmployees()) {
            deviation += individual.getUnwantedPatternDeviation(employee);
        }

        return deviation;
    }

    /**
     * Calculates the deviations for day- and shift-off requests for
     * each employee.
     * @return Deviation
     */
    private float getDeviationOffRequests() {
        float deviation = 0;

        for (Employee employee: period.getEmployees()) {
            deviation += individual.getDayOffRequestsDeviation(employee);
            deviation += individual.getShiftOffRequestsDeviation(employee);
        }

        return deviation;
    }
}
