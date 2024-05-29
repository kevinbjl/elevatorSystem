package controller;

/**
 * This is the interface for the controller of the building.
 * It supports operations including adding requests, stepping the simulation,
 * starting the building, etc.
 */
public interface ControllerInterface {
  /**
   * This method adds requests to the building.
   *
   * @param startFloor the floor the request starts on
   * @param endFloor the floor the request ends on
   */
  void addRequest(int startFloor, int endFloor);

  /**
   * This method steps the elevators.
   */
  void stepSimulation();

  /**
   * This method starts the elevator system.
   */
  void startBuilding();

  /**
   * This method is used to quit the simulation.
   */
  void quitSimulation();

  /**
   * This method is used to halt the building.
   */
  void haltBuilding();

  /**
   * This method is used to generate random requests.
   *
   * @param numRequests the number of requests to generate
   */
  void generateRandomRequest(int numRequests);
}
