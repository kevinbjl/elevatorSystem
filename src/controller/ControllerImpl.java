package controller;

import building.BuildingInterface;
import java.util.Random;
import scanerzus.Request;
import view.SwingViewImpl;
import view.ViewInterface;

/**
 * This is the implementation for the controller of the building.
 * It handles operations like adding requests, stepping the simulation, starting the building, etc.
 */
public class ControllerImpl implements ControllerInterface {
  private final ViewInterface view;
  private final BuildingInterface model;

  /**
   * Constructs a new Swing-based building controller with the specified view and model.
   *
   * @param view the view component of the building
   * @param model the model component of the building
   */
  public ControllerImpl(SwingViewImpl view, BuildingInterface model) {
    this.view = view;
    this.model = model;
    view.addFeatures(this);
  }

  @Override
  public void addRequest(int startFloor, int endFloor) {
    try {
      Request request = new Request(startFloor, endFloor);
      view.clearStartString();
      view.clearEndString();
      model.addRequest(request);
      view.update(model);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Invalid request: " + e.getMessage());
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void startBuilding() {
    try {
      model.startElevatorSystem();
      view.update(model);
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void stepSimulation() {
    try {
      model.stepForward();
      view.update(model);
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
      System.exit(1);
    }
  }

  @Override
  public void quitSimulation() {
    System.exit(0);
  }

  @Override
  public void haltBuilding() {
    try {
      model.stopElevatorSystem();
      view.update(model);
    } catch (IllegalStateException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  @Override
  public void generateRandomRequest(int numRequests) {
    Random random = new Random();
    for (int i = 0; i < numRequests; i++) {
      int startFloor = random.nextInt(model.getElevatorSystemStatus().getNumFloors());
      // Avoid generating requests that start and end on the same floor
      int endFloor;
      do {
        endFloor = random.nextInt(model.getElevatorSystemStatus().getNumFloors());
      } while (startFloor == endFloor);
      model.addRequest(new Request(startFloor, endFloor));
    }
    view.update(model);
  }
}
