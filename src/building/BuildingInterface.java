package building;

import building.enums.ElevatorSystemStatus;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.List;
import scanerzus.Request;

/**
 * This interface is used to represent a building.
 */
public interface BuildingInterface {
  /**
   * This method is used to add a request to the building.
   *
   * @param request the request to be added.
   * @return true whatsoever.
   * @throws IllegalStateException if the system is not running.
   * @throws IllegalArgumentException if the request is null, or if the request has the same start
   *     and end floor, or if the start and end floors are out of bounds.
   */
  boolean addRequest(Request request) throws IllegalStateException, IllegalArgumentException;

  /**
   * This method is used to start the elevator system.
   * If the system is already running, it will do nothing.
   * If the system is stopped, it will throw an exception.
   * If the system is out of service, it will start the system.
   *
   * @return true if the system is started, false otherwise.
   * @throws IllegalStateException if the system is stopped.
   */
  boolean startElevatorSystem() throws IllegalStateException;

  /**
   * This method is used to stop the elevator system.
   * If the system is already stopped or out of service, it will throw an exception.
   * If the system is running, it will stop the system and purge the requests.
   *
   * @throws IllegalStateException if the system is already stopped or out of service.
   */
  void stopElevatorSystem() throws IllegalStateException;

  /**
   * This method is used to return an array of elevator reports.
   *
   * @return an array of elevator reports.
   */
  ElevatorReport[] getElevatorReports();

  /**
   * This method is used to return the up requests for the building.
   *
   * @return the up requests for the building.
   */
  List<Request> getUpRequests();

  /**
   * This method is used to return the down requests for the building.
   *
   * @return the down requests for the building.
   */
  List<Request> getDownRequests();

  /**
   * This method is used to return the status of the building.
   *
   * @return the status of the building.
   */
  BuildingReport getElevatorSystemStatus();

  /**
   * This method is used to return the status of the elevator system.
   *
   * @return the status of the elevator system.
   */
  ElevatorSystemStatus getSystemStatus();

  /**
   * This method is used to return the elevators in the building.
   *
   * @return the elevators in the building.
   */
  ElevatorInterface[] getElevators();

  /**
   * This method is used to step all the elevators in the building.
   */
  void stepForward();
}
