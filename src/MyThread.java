//Author: Sayeed Gulmahamad
import java.util.Random;
import java.util.Vector;

/**
 * Parent for Car and Passenger
 */
public class MyThread extends Thread {
    public static final long time = System.currentTimeMillis();
    private final int id;
    private final String className;
    protected final Vector<Passenger> passengersQueue;
    protected final Vector<Car> carsQueue;
    protected static final Random random = new Random();

    /**
     * log message
     * @param m
     */
    public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ":" + m);
    }

    /**
     * constructor
     * @param id id
     * @param className className
     * @param passengersQueue queue of passengers
     * @param carsQueue queue of cars
     */
    public MyThread(int id, String className, Vector<Passenger> passengersQueue, Vector<Car> carsQueue) {
        this.id = id;
        this.className = className;
        this.passengersQueue = passengersQueue;
        this.carsQueue = carsQueue;

        setName(className + "-" + id);
    }

    @Override
    public String toString() {
        return getName();
    }
}

