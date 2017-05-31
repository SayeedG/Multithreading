//Author: Sayeed Gulmahamad
import java.util.Vector;

public class Main {

    /**
     * gets passengers amount
     * @return passengers amount
     */
    public static int getNpass() {
        return Npass;
    }

    /**
     * passengers amount
     */
    private static int Npass = 11;
    /**
     * cars amount
     */
    private static int Ncars = 3;
    /**
     * car capacity
     */
    private static int P = 4;

    /**
     * main method
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 3) {
            try {
                Npass = Integer.parseInt(args[0]);
                Ncars = Integer.parseInt(args[1]);
                P = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e){
                e.printStackTrace();
                return;
            }
        } else if (args.length > 0) {
            System.err.println("Provide Npass, Ncars, P arguments. For example: 11 3 4");
            return;
        }

        work();
    }

    /**
     * init passengers and cars
     */
    private static void work() {
        Vector<Passenger> passengers = new Vector<>(Npass);
        Vector<Car> cars = new Vector<>(Ncars);

        for(int i = 0; i < Npass; ++i) {
            passengers.add(new Passenger(i, passengers, cars));
        }

        Car.setCapacity(P);
        Car.setCarsAmount(Ncars);

        for(int i = 0; i < Ncars; ++i) {
            cars.add(new Car(i, passengers, cars));
        }
    }
}
