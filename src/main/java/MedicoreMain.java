import java.util.ArrayList;
import java.util.List;

public class MedicoreMain {

    private static List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {
        employees.add(new Employee("Paul", TransportMethod.CAR, 60, 5));
        employees.add(new Employee("Martin", TransportMethod.BUS, 8, 4));
        employees.add(new Employee("Jeroen", TransportMethod.BIKE, 9, 5));
        employees.add(new Employee("Tineke", TransportMethod.BIKE, 4, 3));
        employees.add(new Employee("Arnout", TransportMethod.TRAIN, 23, 5));
        employees.add(new Employee("Matthijs", TransportMethod.BIKE, 11, 4.5));
        employees.add(new Employee("Rens", TransportMethod.CAR, 12, 5));

        Utils.generateCSV(employees);
    }
}
