package building;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import building.enums.ElevatorSystemStatus;
import elevator.ElevatorInterface;
import elevator.ElevatorReport;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import scanerzus.Request;

/**
 * This class is used to test the Building class.
 */
public class BuildingTest {
  private Building building;

  /**
   * Set up a building for testing.
   */
  @Before
  public void setUp() {
    this.building = new Building(10, 3, 3);
    building.startElevatorSystem();
  }

  /**
   * Test that constructor throws an exception when the number of floors is less than 3.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsExceptionWhenNumberOfFloorsLessThanThree() {
    new Building(2, 3, 10);
  }

  /**
   * Test that constructor throws an exception when the number of floors is greater than 30.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsExceptionWhenNumberOfFloorsGreaterThanThirty() {
    new Building(31, 3, 10);
  }

  /**
   * Test that constructor throws an exception when the number of elevators is less than 1.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsExceptionWhenNumberOfElevatorsLessThanOne() {
    new Building(10, 0, 10);
  }

  /**
   * Test that constructor throws an exception when the elevator capacity is less than 3.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsExceptionWhenElevatorCapacityLessThanThree() {
    new Building(10, 3, 2);
  }

  /**
   * Test that constructor throws an exception when the elevator capacity is greater than 20.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorThrowsExceptionWhenElevatorCapacityGreaterThanTwenty() {
    new Building(10, 3, 21);
  }

  /**
   * Test that addRequest throws an exception when the building is not running.
   */
  @Test(expected = IllegalStateException.class)
  public void testAddRequestThrowsExceptionWhenBuildingIsNotRunning() {
    building.stopElevatorSystem();
    building.addRequest(new Request(5, 6));
  }

  /**
   * Test that addRequest throws an exception when the request is null.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenRequestIsNull() {
    building.addRequest(null);
  }

  /**
   * Test that addRequest throws an exception when the start and end floors are the same.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenStartAndEndFloorsAreTheSame() {
    building.addRequest(new Request(5, 5));
  }

  /**
   * Test that addRequest throws an exception when the start floor is less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenStartFloorLessThanZero() {
    building.addRequest(new Request(-1, 5));
  }

  /**
   * Test that addRequest throws an exception when the start floor is greater than or equal to the
   * number of floors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenStartFloorGreaterThanOrEqualToNumberOfFloors() {
    building.addRequest(new Request(10, 5));
  }

  /**
   * Test that addRequest throws an exception when the end floor is less than 0.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenEndFloorLessThanZero() {
    building.addRequest(new Request(5, -1));
  }

  /**
   * Test that addRequest throws an exception when the end floor is greater than or equal to the
   * number of floors.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAddRequestThrowsExceptionWhenEndFloorGreaterThanOrEqualToNumberOfFloors() {
    building.addRequest(new Request(5, 10));
  }

  /**
   * Test that addRequest correctly adds an up request.
   */
  @Test
  public void testAddRequestAddsUpRequest() {
    Request request = new Request(5, 6);
    building.addRequest(request);
    assertEquals(request, building.getUpRequests().get(0));
  }

  /**
   * Test that addRequest correctly adds a down request.
   */
  @Test
  public void testAddRequestAddsDownRequest() {
    Request request = new Request(6, 5);
    building.addRequest(request);
    assertEquals(request, building.getDownRequests().get(0));
  }

  /**
   * Test that startElevatorSystem throws an exception when the system is stopping.
   */
  @Test(expected = IllegalStateException.class)
  public void testStartElevatorSystemThrowsExceptionWhenSystemIsStopping() {
    building.stopElevatorSystem();
    building.startElevatorSystem();
  }

  /**
   * Test that startElevatorSystem returns false when the system is running.
   */
  @Test
  public void testStartElevatorSystemReturnsFalseWhenSystemIsRunning() {
    assertFalse(building.startElevatorSystem());
  }

  /**
   * Test that startElevatorSystem starts all the elevators.
   */
  @Test
  public void testStartElevatorSystemStartsElevators() {
    Building building = new Building(10, 3, 10);
    building.startElevatorSystem();
    boolean allEqual = true;
    for (ElevatorInterface elevator : building.getElevators()) {
      if (!elevator.isTakingRequests()) {
        allEqual = false;
      }
    }
    assertTrue(allEqual);
  }

  /**
   * Test that stopElevatorSystem throws an exception when the system is not running.
   */
  @Test(expected = IllegalStateException.class)
  public void testStopElevatorSystemThrowsExceptionWhenSystemIsNotRunning() {
    building.stopElevatorSystem();
    building.stopElevatorSystem();
  }

  /**
   * Test that stopElevatorSystem stops all the elevators.
   */
  @Test
  public void testStopElevatorSystemStopsElevators() {
    building.stopElevatorSystem();
    boolean allEqual = true;
    for (ElevatorInterface elevator : building.getElevators()) {
      if (elevator.isTakingRequests()) {
        allEqual = false;
      }
    }
    assertTrue(allEqual);
  }

  /**
   * Test that stopElevatorSystem removes all requests.
   */
  @Test
  public void testStopElevatorSystemRemovesRequests() {
    building.addRequest(new Request(5, 6));
    building.addRequest(new Request(6, 5));
    building.stopElevatorSystem();
    assertTrue(building.getUpRequests().isEmpty());
    assertTrue(building.getDownRequests().isEmpty());
  }

  /**
   * Test that getElevatorReports returns the correct reports.
   */
  @Test
  public void testGetElevatorReports() {
    ElevatorReport[] reports = building.getElevatorReports();
    for (int i = 0; i < reports.length; i++) {
      assertEquals(reports[i], building.getElevators()[i].getElevatorStatus());
    }
  }

  /**
   * Test that getUpRequests returns a copy of the up requests.
   */
  @Test
  public void testGetUpRequestsReturnsCopy() {
    building.addRequest(new Request(5, 6));
    building.addRequest(new Request(2, 7));

    List<Request> upRequests = new LinkedList<>();
    upRequests.add(new Request(5, 6));
    upRequests.add(new Request(2, 7));

    assertEquals(upRequests.toString(), building.getUpRequests().toString());
  }

  /**
   * Test that getDownRequests returns a copy of the down requests.
   */
  @Test
  public void testGetDownRequestsReturnsCopy() {
    building.addRequest(new Request(6, 5));
    building.addRequest(new Request(7, 2));

    List<Request> downRequests = new LinkedList<>();
    downRequests.add(new Request(6, 5));
    downRequests.add(new Request(7, 2));

    assertEquals(downRequests.toString(), building.getDownRequests().toString());
  }

  /**
   * Test that getElevatorSystemStatus returns the correct report.
   */
  @Test
  public void testGetElevatorSystemStatus() {
    BuildingReport report = building.getElevatorSystemStatus();
    assertEquals(building.getElevatorReports(), report.getElevatorReports());
    assertEquals(building.getUpRequests(), report.getUpRequests());
    assertEquals(building.getDownRequests(), report.getDownRequests());
    assertEquals(building.getSystemStatus(), report.getSystemStatus());
  }

  /**
   * Test that getSystemStatus returns the correct status.
   */
  @Test
  public void testGetSystemStatus() {
    assertEquals(ElevatorSystemStatus.running, building.getSystemStatus());
  }

  /**
   * Test that stepForward distributes up requests and steps the elevators.
   */
  @Test
  public void testStepForwardDistributesUpRequests() {
    building.addRequest(new Request(5, 6));
    assertEquals(0, building.getElevators()[0].getCurrentFloor());
    for (int i = 0; i < 9; i++) {
      building.stepForward();
    }
    assertEquals(5, building.getElevators()[0].getCurrentFloor());
  }

  /**
   * Test that stepForward distributes down requests and steps the elevators.
   */
  @Test
  public void testStepForwardDistributesDownRequests() {
    building.addRequest(new Request(6, 5));
    assertEquals(0, building.getElevators()[0].getCurrentFloor());
    for (int i = 0; i < 18; i++) {
      building.stepForward();
    }
    assertEquals(6, building.getElevators()[0].getCurrentFloor());
  }

  /**
   * Test that stepForward correctly changes the status of the system after a stop.
   */
  @Test
  public void testStepForwardChangesSystemStatusAfterStop() {
    assertEquals(ElevatorSystemStatus.running, building.getSystemStatus());

    building.addRequest(new Request(1, 3));
    for (int i = 0; i < 9; i++) {
      building.stepForward();
    }
    assertEquals(2, building.getElevators()[0].getElevatorStatus().getDoorOpenTimer());

    building.stopElevatorSystem();
    assertEquals(ElevatorSystemStatus.stopping, building.getSystemStatus());
    for (int i = 0; i < 30; i++) {
      building.stepForward();
    }
    assertEquals(ElevatorSystemStatus.outOfService, building.getSystemStatus());
  }

  /**
   * Test that stepForward throws an exception when the number of requests exceeds the elevator's
   * capacity.
   */
  @Test(expected = IllegalStateException.class)
  public void testStepForwardThrowsExceptionWhenRequestsExceedCapacity() {
    building.addRequest(new Request(1, 3));
    building.addRequest(new Request(2, 5));
    building.addRequest(new Request(3, 7));
    building.addRequest(new Request(4, 9));
    building.stepForward();
  }
}