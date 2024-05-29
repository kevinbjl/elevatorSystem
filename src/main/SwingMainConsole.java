package main;

import building.Building;
import building.BuildingInterface;
import controller.ControllerImpl;
import controller.ControllerInterface;
import view.SwingViewImpl;

/**
 * This is the main class that runs the elevator system.
 */
public class SwingMainConsole {
  /**
   * This is the main method that runs the elevator system.
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    if (args.length == 0) {
      BuildingInterface model = new Building(10, 5,
          10);
      SwingViewImpl view = new SwingViewImpl("Elevator System", 10, 5,
          10);
      ControllerInterface controller = new ControllerImpl(view, model);
      controller.startBuilding();
    } else {
      if (args.length != 3) {
        System.err.println("Usage: java -jar ElevatorSystem.jar <numFloors> "
            + "<numElevators> <capacity>");
        System.exit(1);
      }
      try {
        int numFloors = Integer.parseInt(args[0]);
        int numElevators = Integer.parseInt(args[1]);
        int capacity = Integer.parseInt(args[2]);

        // Initialize the model (building)
        BuildingInterface model = new Building(numFloors, numElevators, capacity);

        // Initialize the view
        SwingViewImpl view = new SwingViewImpl("Elevator System", numFloors, numElevators,
            capacity);

        // Initialize the controller and connect it to the model and view
        ControllerInterface controller = new ControllerImpl(view, model);

        controller.startBuilding();
      } catch (NumberFormatException e) {
        System.err.println("Invalid input. Argument must be integers.");
        System.exit(1);
      } catch (IllegalArgumentException e) {
        System.err.println("Invalid input. Number of floors, number of elevators or capacity "
            + "out of bounds.");
        System.exit(1);
      }
    }
  }
}
