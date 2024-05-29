package building;

import building.enums.ElevatorSystemStatus;
import elevator.Elevator;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import scanerzus.Request;

/**
 * This class represents a building.
 * It has a number of floors, elevators, and an elevator capacity.
 * It stores elevators and requests using lists.
 * Its status is stored as an ElevatorSystemStatus enum.
 */
public class Building implements BuildingInterface {
  private final int numberOfFloors;
  private final int numberOfElevators;
  private final int elevatorCapacity;
  private final ElevatorInterface[] elevators;
  private final List<Request> upRequests;
  private final List<Request> downRequests;
  private ElevatorSystemStatus status;

  /**
   * The constructor for the building.
   *
   * @param numberOfFloors    the number of floors in the building.
   * @param numberOfElevators the number of elevators in the building.
   * @param elevatorCapacity  the capacity of the elevators in the building.
   * @throws IllegalArgumentException if the number of floors is not between 3 and 30,
   *     or if number of elevators is negative, or if capacity is not between 3 and 20.
   */
  public Building(int numberOfFloors, int numberOfElevators, int elevatorCapacity) {
    if (!(numberOfFloors >= 3 && numberOfFloors <= 30)) {
      throw new IllegalArgumentException("The number of floors must be between 3 and 30.");
    }
    if (!(numberOfElevators > 0)) {
      throw new IllegalArgumentException("The number of elevators must be greater than 0.");
    }
    if (!(elevatorCapacity >= 3 && elevatorCapacity <= 20)) {
      throw new IllegalArgumentException("The elevator capacity must be between 3 and 20.");
    }
    this.numberOfFloors = numberOfFloors;
    this.numberOfElevators = numberOfElevators;
    this.elevatorCapacity = elevatorCapacity;
    this.elevators = new ElevatorInterface[numberOfElevators];
    this.upRequests = new LinkedList<>();
    this.downRequests = new LinkedList<>();
    this.status = ElevatorSystemStatus.outOfService;

    for (int i = 0; i < numberOfElevators; i++) {
      elevators[i] = new Elevator(numberOfFloors, elevatorCapacity);
    }
  }

  @Override
  public boolean addRequest(Request request)
      throws IllegalStateException, IllegalArgumentException {
    if (status != ElevatorSystemStatus.running) {
      throw new IllegalStateException("The system is not running.");
    }
    if (request == null) {
      throw new IllegalArgumentException("The request cannot be null.");
    }
    if (request.getStartFloor() == request.getEndFloor()) {
      throw new IllegalArgumentException("The start and end floor cannot be the same.");
    }
    if (request.getStartFloor() < 0 || request.getStartFloor() >= numberOfFloors
        || request.getEndFloor() < 0 || request.getEndFloor() >= numberOfFloors) {
      throw new IllegalArgumentException("Start and/or end floors out of bounds.");
    }
    if (request.getEndFloor() > request.getStartFloor()) {
      upRequests.add(request);
    } else {
      downRequests.add(request);
    }
    return true;
  }

  @Override
  public boolean startElevatorSystem() throws IllegalStateException {
    if (status == ElevatorSystemStatus.running) {
      return false;
    } else if (status == ElevatorSystemStatus.stopping) {
      throw new IllegalStateException("The system is stopping.");
    } else {
      status = ElevatorSystemStatus.running;
      for (ElevatorInterface elevator : elevators) {
        elevator.start();
      }
      return true;
    }
  }

  @Override
  public void stopElevatorSystem() {
    if (status != ElevatorSystemStatus.running) {
      throw new IllegalStateException("The system is not running.");
    }
    for (ElevatorInterface elevator : elevators) {
      elevator.takeOutOfService();
    }
    status = ElevatorSystemStatus.stopping;
    removeRequests();
  }

  @Override
  public ElevatorReport[] getElevatorReports() {
    ElevatorReport[] reports = new ElevatorReport[numberOfElevators];
    for (int i = 0; i < numberOfElevators; i++) {
      reports[i] = elevators[i].getElevatorStatus();
    }
    return reports;
  }

  @Override
  public List<Request> getUpRequests() {
    return new LinkedList<>(upRequests);
  }

  @Override
  public List<Request> getDownRequests() {
    return new LinkedList<>(downRequests);
  }

  @Override
  public BuildingReport getElevatorSystemStatus() {
    return new BuildingReport(numberOfFloors, numberOfElevators, elevatorCapacity,
        getElevatorReports(), upRequests, downRequests, status);
  }

  @Override
  public ElevatorSystemStatus getSystemStatus() {
    return status;
  }

  @Override
  public ElevatorInterface[] getElevators() {
    return Arrays.copyOf(elevators, elevators.length);
  }

  /**
   * This method is used to clear all the requests in the building.
   */
  private void removeRequests() {
    upRequests.clear();
    downRequests.clear();
  }

  @Override
  public void stepForward() throws IllegalStateException {
    if (status == ElevatorSystemStatus.running) {
      try {
        distributeRequests();
      } catch (IllegalStateException e) {
        throw new IllegalStateException(e);
      }
    }
    for (ElevatorInterface elevator : elevators) {
      elevator.step();
    }

    if (status == ElevatorSystemStatus.stopping) {
      boolean allOnGround = true;
      for (ElevatorInterface elevator : elevators) {
        if (elevator.getCurrentFloor() != 0) {
          allOnGround = false;
          break;
        }
      }

      if (allOnGround) {
        status = ElevatorSystemStatus.outOfService;
      }
    }
  }

  /**
   * This method is used to distribute requests to the elevators.
   *
   * @throws IllegalStateException if the number of requests exceed the elevator's capacity.
   */
  private void distributeRequests() throws IllegalStateException {
    for (ElevatorInterface elevator : elevators) {
      if (elevator.getCurrentFloor() == 0 && elevator.isTakingRequests()) {
        if (upRequests.size() > elevatorCapacity) {
          throw new IllegalStateException("Number of requests exceeds the elevator's capacity.");
        }
        elevator.processRequests(upRequests);
        upRequests.clear();
      } else if (elevator.getCurrentFloor() == numberOfFloors - 1 && elevator.isTakingRequests()) {
        if (downRequests.size() > elevatorCapacity) {
          throw new IllegalStateException("Number of requests exceeds the elevator's capacity.");
        }
        elevator.processRequests(downRequests);
        downRequests.clear();
      }
    }
  }
}