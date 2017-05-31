//Author: Sayeed Gulmahamad
import java.util.Vector;

/**
 * Car class
 */
public class Car extends MyThread {
    /**
     * set capacity of car
     * @param capacity capacity
     */
    public static void setCapacity(int capacity) {
        Car.capacity = capacity;
    }

    /**
     * gets cars amount
     * @return cars amount
     */
    public static int getCarsAmount() {
        return carsAmount;
    }

    /**
     * set cars amount
     * @param carsAmount cars amount
     */
    public static void setCarsAmount(int carsAmount) {
        Car.carsAmount = carsAmount;
    }

    private static int carsAmount = 3;
    private static int capacity = 4;
    private Vector<Passenger> passengers = new Vector<>();

    /**
     * constructor
     * @param id id
     * @param passengersQueue queue of passengers
     * @param carsQueue queue of cars
     */
    public Car(int id, Vector<Passenger> passengersQueue, Vector<Car> carsQueue) {
        super(id, Car.class.getSimpleName(), passengersQueue, carsQueue);

        start();
    }

    /**
     * adds passenger to car
     * @param passenger passenger
     */
    public synchronized void addPassenger(Passenger passenger) {
        if(!isFull()) {
            passengers.add(passenger);
        }
    }

    /**
     * checks whether car is full
     * @return true if is full, false otherwise
     */
    public synchronized boolean isFull() {
        return passengers.size() >= capacity;
    }

    private boolean isRunning = true;

    /**
     * start tour
     */
    private void tour() {
        // the car started so let's remove from queue
        if (carsQueue.remove(this)) {
            msg("tour");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            msg("arrived");
            // the car arrived back so let's add it to tail
            carsQueue.add(this);

            for (Passenger p : passengers) {
                if(!p.isInterrupted())
                    p.interrupt();
            }

            passengers.clear();
        }
    }

    /**
     * checks if it is very last ride
     * @return true if very last ride, false otherwise
     */
    public boolean isVeryLastRide() {
        return Main.getNpass() - Passenger.getFinishedPassengers().size() == passengers.size() && !passengers.isEmpty();
    }

    /**
     * starting the thread causes the object's run method to be called in that separately executing thread
     */
    @Override
    public void run() {
        while (isRunning()) {
            // check whether this car is the first in line
            if ((isFull() || isVeryLastRide()) && carsQueue.indexOf(this) == 0) {
                tour();
            }
        }

        msg("terminate");
    }

    /**
     * Is it alive?
     * @return true if so, false otherwise
     */
    private synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * terminate thread if running is false
     * @param running
     */
    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
