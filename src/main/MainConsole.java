package main;

import building.Building;
import scanerzus.Request;

/**
 * The driver for the elevator system.
 * This class will create the elevator system and run it.
 * this is for testing the elevator system.
 * <p>
 * It provides a user interface to the elevator system.
 */
public class MainConsole {

  /**
   * The main method for the elevator system.
   * This method creates the elevator system and runs it.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args) {

    TextDriverInterface driver = new TextDriver();

    int numFloors = 11;
    int numElevators = 8;
    int numPeople = 3;

    driver.displayIntro(numFloors, numElevators, numPeople);
    driver.startElevatorSystem(numFloors, numElevators, numPeople);

    System.out.println("Added two up requests 1->5, 2->4.\n");
    driver.addRequestsToSystem(new Request(1, 5), new Request(2, 4));

    System.out.println("Step the system forward 5 times.\n");
    driver.stepSystem(5);

    System.out.println("Added two down requests: 6->3, 7->2, and an up request: 3->8.\n");
    driver.addRequestsToSystem(new Request(6, 3), new Request(7, 2), new Request(3, 8));

    System.out.println("Step the system forward 20 times.\n");
    driver.stepSystem(20);

    System.out.println("Stopping the system.\n");
    driver.stopElevatorSystem();

    System.out.println("Step the system forward 10 times.\n");
    driver.stepSystem(10);

    System.out.println("Simulation ended.");
  }
}