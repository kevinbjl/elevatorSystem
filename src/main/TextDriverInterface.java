package main;

import scanerzus.Request;

/**
 * This is the driver interface for running the elevator system.
 * It defines a set of methods that helps to run the elevator system.
 */
public interface TextDriverInterface {
  /**
   * This method will display the introduction to the elevator system.
   *
   * @param numFloors the number of floors in the building
   * @param numElevators the number of elevators in the building
   * @param numPeople the number of people in the building
   */
  void displayIntro(int numFloors, int numElevators, int numPeople);

  /**
   * This method will start the elevator system.
   *
   * @param numFloors the number of floors in the building
   * @param numElevators the number of elevators in the building
   * @param numPeople the number of people in the building
   */
  void startElevatorSystem(int numFloors, int numElevators, int numPeople);

  /**
   * This method will add requests to the system.
   *
   * @param requests the requests to add to the system
   */
  void addRequestsToSystem(Request... requests);

  /**
   * This method will step the system forward n times.
   * @param n the number of times to step the system forward
   */
  void stepSystem(int n);

  /**
   * This method will stop the elevator system.
   */
  void stopElevatorSystem();
}
