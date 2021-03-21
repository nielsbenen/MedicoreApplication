import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    //constants and formatters
    private static final double BIKECOMPENSATION = 0.5;
    private static final double PUBLICTRANSPORTCOMPENSATION = 0.25;
    private static final double CARCOMPENSATION = 0.1;

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DecimalFormat df = new DecimalFormat("###.00",
            DecimalFormatSymbols.getInstance(Locale.US));

    /**
     * Gets the total traveled distance for an employee in a given month
     * @param e the employee to get the total traveled distance of
     * @param month the number (1-12) of the month to get the total traveled distance of
     * @return the total traveled distance this month by this employee in kilometers
     */
    public static int getTraveledDistanceByMonth(Employee e, int month) {
        //get current date to get year
        LocalDate currentdate = LocalDate.now();

        //get first date of month
        LocalDate d = LocalDate.of(currentdate.getYear(), month, 1);

        //keep track of total workdays for this month
        int totalworkdays = 0;

        //loop through all days of this month
        while(d.getMonth().getValue() == month) {
            //we assume the first x days of the week are workdays
            //if someone works only half a day they will still get the compensation, so we round up
            if (d.getDayOfWeek().getValue() <= Math.ceil(e.getWorkdays())) {
                totalworkdays++;
            }
            //go to next day
            d = d.plusDays(1);
        }

        return totalworkdays * 2 * e.getDistance();
    }

    /**
     * Calculates the total compensation an employee is due given the amount of kilometers traveled
     * @param e the employee for who the calculation should be performed
     * @param travelledDistance the total amount traveled during the required timespan
     * @return the compensation based on the amount of time traveled
     */
    public static double getCompensationByEmployee(Employee e, int travelledDistance) {
        //we determine the correct factor by the means of transport
        switch(e.getTransportMethod()) {
            case BIKE:
                //if the distance is between 5 and 10 km, the compensation is doubled
                if (5 <= e.getDistance() && e.getDistance() <= 10) {
                    return travelledDistance * BIKECOMPENSATION * 2;
                } else {
                    return travelledDistance * BIKECOMPENSATION;
                }
            case CAR:
                return travelledDistance * CARCOMPENSATION;
            default:
                return travelledDistance * PUBLICTRANSPORTCOMPENSATION;
        }

    }

    /**
     * Returns the first monday of the next month, where the given month is in the current year
     * @param month the number (1-12) of the month before the month of interest
     * @return the LocalDate object of the first monday in the next month
     */
    public static LocalDate getFirstMondayOfNextMonth(int month) {
        //get current date to get year
        LocalDate currentdate = LocalDate.now();

        //get first date of month
        //if the month is December, we move to the next year
        LocalDate d = (month == 12 ? LocalDate.of(currentdate.getYear() + 1, 1, 1) :
                                     LocalDate.of(currentdate.getYear(), month + 1, 1));

        //loop through all days of this month
        while(d.getDayOfWeek() != DayOfWeek.MONDAY) {
            //go to next day
            d = d.plusDays(1);
        }

        return d;
    }

    /**
     * Generates a .csv file with information about travel compensation for the current year, given a list of employees
     * @param employees the complete list of employees the csv file should include
     */
    public static void generateCSV(List<Employee> employees) {
        //generate/prepare the output file
        File csvOutputFile = new File("Travel-Compensation.csv");

        //write to this file
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            //first generate the headers
            pw.println("Employee name,Transport Method,Traveled Distance,Compensation,Payment Date");

            //loop through all months and employees
            for (int i = 1; i <= 12; i++) {
                for (Employee e : employees) {
                    //first get the amount of distance traveled this month for calculation purposes
                    int traveledDistance = getTraveledDistanceByMonth(e, i);

                    //create csv entry
                    pw.println(e.getName() + ","
                            + e.getTransportMethod() + ","
                            + traveledDistance + ","
                            + df.format(getCompensationByEmployee(e, traveledDistance)) + ","
                            + getFirstMondayOfNextMonth(i).format(format));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
