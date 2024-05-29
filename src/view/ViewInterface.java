package view;

import building.BuildingInterface;
import controller.ControllerInterface;

/**
 * This is the interface for the view.
 * The view will be a GUI.
 */
public interface ViewInterface {
  /**
   * This method will be called by the controller to update the view.
   *
   * @param building The building to update the view with.
   */
  void update(BuildingInterface building);

  /**
   * This method will be called by the controller to display an error message.
   *
   * @param message The error message to display.
   */
  void displayErrorMessage(String message);

  /**
   * This method is used to add features to the GUI.
   * @param controller the controller to add features to the GUI
   */
  void addFeatures(ControllerInterface controller);

  /**
   * This method is used to clear the start string of the GUI.
   */
  void clearStartString();

  /**
   * This method is used to clear the end string of the GUI.
   */
  void clearEndString();
}
