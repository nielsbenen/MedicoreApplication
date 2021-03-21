public class Employee {

    private String name;
    private TransportMethod method;
    private int distance; //the distance one way
    private double workdays;

    public Employee(String name, TransportMethod method, int distance, double workdays) {
        this.name = name;
        this.method = method;
        this.distance = distance;
        this.workdays = workdays;
    }

    public String getName() {
        return name;
    }

    public double getWorkdays() {
        return workdays;
    }

    public TransportMethod getTransportMethod() {
        return method;
    }

    public int getDistance() {
        return distance;
    }
}
