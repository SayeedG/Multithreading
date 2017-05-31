//Author: Sayeed Gulmahamad
import java.util.Vector;

/**
 * Passenger class
 */
public class Passenger extends MyThread {
    public static final int MAX_RIDES = 3;
    private Car car;

    /**
     * gets passengers who left
     * @return passengers
     */
    public static Vector<Passenger> getFinishedPassengers() {
        return finishedPassengers;
    }

    private static Vector<Passenger> finishedPassengers;

    /**
     * constructor
     * @param id id
     * @param passengersQueue queue of passengers
     * @param carsQueue queue of cars
     */
    public Passenger(int id, Vector<Passenger> passengersQueue, Vector<Car> carsQueue) {
        super(id, Passenger.class.getSimpleName(), passengersQueue, carsQueue);

        finishedPassengers = new Vector<>(passengersQueue.size());

        start();
    }

    /**
     * passenger enters car
     * @param car car
     */
    private void enter(Car car) {
        this.car = car;
        Thread.yield();
        msg("enter: " + car);
        car.addPassenger(this);
        // passenger enters car so let's remove it from the queue
        passengersQueue.remove(this);
    }

    /**
     * gets current rides amount
     * @return rides amount
     */
    public synchronized int getRides() {
        return rides;
    }

    /**
     * increments rides amount
     */
    private synchronized void incrementRides() {
        ++rides;
    }

    private int rides = 0;

    /**
     * passenger leaves
     */
    private void leave() {
        final int index = finishedPassengers.indexOf(this);
        // join threads
        if (index > 0) {
            try {
                final Passenger passenger = finishedPassengers.get(index - 1);
                if(passenger.isAlive())
                    passenger.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // terminate cars as it is last passenger
        if (index == Main.getNpass() - 1) {
            for (Car car : carsQueue) {
                car.setRunning(false);
            }
        }

        msg("leave");
    }

    /**
     * log message
     * @param m
     */
    @Override
    public void msg(String m) {
        super.msg("rides=" + getRides() +  ", " + m);
    }

    /**
     * starting the thread causes the object's run method to be called in that separately executing thread
     */
    @Override
    public void run() {
        while (getRides() < MAX_RIDES) {
            Car car = null;
            synchronized (carsQueue) {
                // the cars are not allowed to tour the house simultaneously
                // waiting for the arrival of the previous car
                if(carsQueue.size() == Car.getCarsAmount())
                    car = carsQueue.get(0);
            }

            if (passengersQueue.indexOf(this) == 0 && car != null) {
                if (!car.isFull()) {
                    enter(car);

                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                        incrementRides();

                        exit();

                        wander();

                        if (getRides() < MAX_RIDES) {
                            // let's add a passenger to a tail of queue as it is going to take a next tour
                            passengersQueue.add(this);
                        }
                        else {
                            // let's add passenger to a tail of queue of passengers who left
                            finishedPassengers.add(this);
                        }
                    }
                }
            }
        }

        leave();
    }

    /**
     * passenger sleeps short random time
     */
    private void shortSleepRandomTime() {
//        msg("a short sleep of random time");
        try {
            Thread.sleep(random.nextInt(500));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * wanders for a while
     */
    private void wander() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        msg("wander");
    }

    /**
     * exits from car
     */
    private void exit() {
        setPriority(random.nextInt((MAX_PRIORITY - NORM_PRIORITY) + 1) + NORM_PRIORITY);

        shortSleepRandomTime();

        msg("exit: " + car);

        setPriority(NORM_PRIORITY);

        if(car != null) {
            car = null;
        }
    }
}
