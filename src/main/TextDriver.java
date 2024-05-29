package main;

import building.Building;
import scanerzus.Request;

/**
 * This class is an implementation of the TextDriverInterface.
 * It runs the system and shows the output in the console.
 */
public class TextDriver implements TextDriverInterface {
  private Building building;

  @Override
  public void displayIntro(int numFloors, int numElevators, int numPeople) {
    String[] introText = {
        "Welcome to the Elevator System!",
        "This system will simulate the operation of an elevator system.",
        "The system will be initialized with the following parameters:",
        "Number of floors: " + numFloors,
        "Number of elevators: " + numElevators,
        "Number of people: " + numPeople,
        "The system will then be run and the results will be displayed.",
    };

    for (String line : introText) {
      System.out.println(line);
    }
  }

  @Override
  public void startElevatorSystem(int numFloors, int numElevators, int numPeople) {
    this.building = new Building(numFloors, numElevators, numPeople);

    System.out.println();
    System.out.println("Started the system.\n");
    building.startElevatorSystem();
  }

  @Override
  public void addRequestsToSystem(Request... requests) {
    for (Request request : requests) {
      building.addRequest(request);
    }
  }

  @Override
  public void stepSystem(int n) {
    for (int i = 0; i < n; i++) {
      building.stepForward();
      System.out.println(building.getElevatorSystemStatus());
    }
  }

  @Override
  public void stopElevatorSystem() {
    building.stopElevatorSystem();
  }
}