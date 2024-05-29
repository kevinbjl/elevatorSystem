package view;

import building.BuildingInterface;
import building.enums.ElevatorSystemStatus;
import controller.ControllerInterface;
import elevator.ElevatorInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import scanerzus.Request;

/**
 * This is the implementation of the view interface
 * It'll use Swing to create the GUI.
 */
public class SwingViewImpl extends JFrame implements ViewInterface {
  private final JPanel[][] floorPanels;
  private final JLabel[][] doorLabels;
  private final JButton stepButton;
  private final JSpinner startFloorField;
  private final JSpinner endFloorField;
  private final JButton submitButton;
  private final JButton quitButton;
  private final JButton continueButton;
  private final JButton haltButton;
  private final JButton generateRequestButton;
  private final JSpinner stepsSpinner;
  private final JSpinner numRequestsSpinner;
  private final JLabel systemStatusLabel;
  private final JLabel upRequestLabel;
  private final JLabel downRequestLabel;
  private final JLabel[] floorRequestLabel;
  private Timer timer;

  /**
   * This is the constructor for the SwingViewImpl class.
   * It'll create a new JFrame, and initialize the panels and buttons.
   *
   * @param title The title of the JFrame.
   * @param numFloors The number of floors in the building.
   * @param numElevators The number of elevators in the building.
   * @param capacity The capacity of the elevators.
   */
  public SwingViewImpl(String title, int numFloors, int numElevators, int capacity) {
    super(title);
    setLayout(new BorderLayout());
    // Customize the UI
    try {
      UIManager.setLookAndFeel(new NimbusLookAndFeel() {
      });
    } catch (UnsupportedLookAndFeelException e) {
      JOptionPane.showMessageDialog(this,
          "Error setting the look and feel.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Set up the basic components
    JPanel elevatorPanelContainer = new JPanel();
    elevatorPanelContainer.setBackground(new Color(240, 244, 246));
    this.floorPanels = new JPanel[numElevators][numFloors];
    JPanel[] elevatorPanels = new JPanel[numElevators];
    this.doorLabels = new JLabel[numElevators][numFloors];

    // Set up the panel for displaying elevators
    for (int i = 0; i < numElevators; i++) {
      elevatorPanels[i] = new JPanel();
      elevatorPanels[i].setBorder(BorderFactory.createTitledBorder("Elevator " + (i)));
      elevatorPanels[i].setLayout(new BoxLayout(elevatorPanels[i], BoxLayout.Y_AXIS));
      // Fill the elevator panel with floor panels
      for (int j = 0; j < numFloors; j++) {
        floorPanels[i][j] = new JPanel();
        floorPanels[i][j].setPreferredSize(new Dimension(100, 20));
        floorPanels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
        floorPanels[i][j].setBackground(new Color(240, 244, 246));
        // Add a JLabel to signify the door, direction and timer
        doorLabels[i][j] = new JLabel();
        doorLabels[i][j].setForeground(Color.WHITE);
        doorLabels[i][j].setFont(new Font("Monospaced", Font.PLAIN, 12));
        doorLabels[i][j].setVisible(false);
        floorPanels[i][j].add(doorLabels[i][j]);
        elevatorPanels[i].add(floorPanels[i][j]);
      }
      elevatorPanels[i].setBackground(new Color(240, 244, 246));
      elevatorPanelContainer.add(elevatorPanels[i]);
    }
    add(elevatorPanelContainer, BorderLayout.CENTER);

    // Set up a panel for displaying floor numbers
    JPanel floorNumberPanel = new JPanel();
    floorNumberPanel.setLayout(new BoxLayout(floorNumberPanel, BoxLayout.Y_AXIS));
    floorNumberPanel.setBorder(BorderFactory.createTitledBorder("\n"));
    floorNumberPanel.setBackground(new Color(240, 244, 246));
    for (int i = numFloors - 1; i >= 0; i--) {
      JPanel floorNumber = new JPanel();
      floorNumber.add(new JLabel(Integer.toString(i)));
      floorNumber.setPreferredSize(new Dimension(20, 20));
      floorNumber.setBackground(new Color(240, 244, 246));
      floorNumberPanel.add(floorNumber);
    }
    add(floorNumberPanel, BorderLayout.WEST);

    // Set up a panel for displaying system status
    JPanel systemStatusPanel = new JPanel();
    systemStatusPanel.setBackground(new Color(101, 119, 134));
    systemStatusPanel.setLayout(new BorderLayout());
    JLabel informationLabel = new JLabel("Floors: " + numFloors + ", Elevators: "
        + numElevators + ", Capacity: " + capacity);
    informationLabel.setFont(new Font("Arial", Font.BOLD, 16));
    informationLabel.setForeground(new Color(225, 232, 237));
    informationLabel.setHorizontalAlignment(JLabel.CENTER);
    systemStatusPanel.add(informationLabel, BorderLayout.NORTH);
    this.systemStatusLabel = new JLabel("Elevator System Running");
    systemStatusLabel.setHorizontalAlignment(JLabel.CENTER);
    systemStatusLabel.setFont(new Font("Arial", Font.BOLD, 16));
    systemStatusLabel.setForeground(new Color(225, 232, 237));
    systemStatusPanel.add(systemStatusLabel, BorderLayout.SOUTH);

    // Set up a panel for displaying requests stored in the building and yet to be processed
    JPanel requestDisplayPanel = new JPanel();
    requestDisplayPanel.setLayout(new BoxLayout(requestDisplayPanel, BoxLayout.Y_AXIS));
    this.upRequestLabel = new JLabel("Up: None");
    upRequestLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
    this.downRequestLabel = new JLabel("Down: None");
    downRequestLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
    requestDisplayPanel.add(upRequestLabel);
    requestDisplayPanel.add(downRequestLabel);
    requestDisplayPanel.setBackground(new Color(241, 248, 241));
    requestDisplayPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1,
        0, Color.BLACK));

    // Set up a panel for displaying each elevator's floor requests
    JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
    requestDisplayPanel.add(separator);
    JPanel floorRequestPanel = new JPanel();
    floorRequestPanel.setBackground(new Color(241, 248, 241));
    this.floorRequestLabel = new JLabel[numElevators];
    floorRequestPanel.setLayout(new BoxLayout(floorRequestPanel, BoxLayout.Y_AXIS));
    for (int i = 0; i < numElevators; i++) {
      floorRequestLabel[i] = new JLabel();
      floorRequestLabel[i].setForeground(new Color(55, 68, 140));
      floorRequestLabel[i].setText("Elevator " + i + ": <" + " --".repeat(numFloors) + " >");
      floorRequestLabel[i].setFont(new Font("Monospaced", Font.PLAIN, 12));
      floorRequestPanel.add(floorRequestLabel[i]);
    }
    requestDisplayPanel.add(floorRequestPanel);

    // Set up fields for user to enter start and end floors
    this.startFloorField = new JSpinner(new SpinnerNumberModel(0, 0,
        numFloors - 1, 1));
    this.endFloorField = new JSpinner(new SpinnerNumberModel(0, 0,
        numFloors - 1, 1));
    startFloorField.setPreferredSize(new Dimension(50, 25));
    endFloorField.setPreferredSize(new Dimension(50, 25));

    // Set up a request panel for taking user inputs
    JPanel requestPanel = new JPanel();
    requestPanel.setBackground(new Color(55, 68, 160));

    JLabel startText = new JLabel("Start Floor: ");
    startText.setForeground(new Color(225, 232, 237));
    startText.setFont(new Font("Arial", Font.PLAIN, 12));
    requestPanel.add(startText);
    requestPanel.add(startFloorField);
    JLabel endText = new JLabel("End Floor: ");
    endText.setForeground(new Color(225, 232, 237));
    endText.setFont(new Font("Arial", Font.PLAIN, 12));
    requestPanel.add(endText);
    requestPanel.add(endFloorField);

    // Set up a submit button that submits the request
    this.submitButton = new JButton("Submit");
    requestPanel.add(submitButton);

    // Set up a spinner for the user to input the number of requests
    this.numRequestsSpinner = new JSpinner(new SpinnerNumberModel(1, 1,
        capacity, 1));
    requestPanel.add(numRequestsSpinner);

    // Set up a generate request button that generates and submits the random requests
    this.generateRequestButton = new JButton("Generate");
    requestPanel.add(generateRequestButton);

    // Set up a control panel for the control flow buttons
    JPanel controlPanel = new JPanel();
    controlPanel.setBackground(new Color(66, 133, 204));

    // Set up a step spinner for the user to input the number of steps
    this.stepsSpinner = new JSpinner(new SpinnerNumberModel(1, 1,
        100, 1));
    controlPanel.add(stepsSpinner);

    // Set up a step button
    this.stepButton = new JButton("Step");
    controlPanel.add(stepButton);

    // Set up a halt button
    this.haltButton = new JButton("Halt");
    controlPanel.add(haltButton);

    // Set up a continue button
    this.continueButton = new JButton("Continue");
    controlPanel.add(continueButton);

    // Set up a quit button
    this.quitButton = new JButton("Quit");
    controlPanel.add(quitButton);

    // Put the status panel and display request panel together as a NORTH panel
    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BorderLayout());
    northPanel.add(systemStatusPanel, BorderLayout.NORTH);
    northPanel.add(requestDisplayPanel, BorderLayout.SOUTH);

    // Put the request panel and control panel together as a SOUTH panel
    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BorderLayout());
    southPanel.add(requestPanel, BorderLayout.NORTH);
    southPanel.add(controlPanel, BorderLayout.SOUTH);

    add(northPanel, BorderLayout.NORTH);
    add(southPanel, BorderLayout.SOUTH);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  @Override
  public void displayErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message, "Error",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void addFeatures(ControllerInterface controllerFeatures) {
    submitButton.addActionListener(evt -> controllerFeatures.addRequest(
        (int) startFloorField.getValue(), (int) endFloorField.getValue()));
    stepButton.addActionListener(evt -> {
      // Set up a timer so that the user can see the process clearly
      int numberOfTimes = (int) stepsSpinner.getValue();
      timer = new Timer(200, new ActionListener() {
        int count = 0;

        @Override
        public void actionPerformed(ActionEvent evt) {
          if (count < numberOfTimes) {
            controllerFeatures.stepSimulation();
            count++;
          }
        }
      });
      timer.start();
    });
    quitButton.addActionListener(evt -> controllerFeatures.quitSimulation());
    haltButton.addActionListener(evt -> {
      controllerFeatures.haltBuilding();
      // Stop the timer so that the simulation won't keep running by itself
      // after the halt or continue button is clicked.
      // This is necessary because when another button is clicked, the action performed
      // by the previous button will not stop running automatically.
      if (timer != null && timer.isRunning()) {
        timer.stop();
      }
    });
    continueButton.addActionListener(evt -> {
      controllerFeatures.startBuilding();
      if (timer != null && timer.isRunning()) {
        timer.stop();
      }
    });
    generateRequestButton.addActionListener(evt -> controllerFeatures.generateRandomRequest(
        (int) numRequestsSpinner.getValue()));
  }

  @Override
  public void clearStartString() {
    startFloorField.setValue(0);
  }

  @Override
  public void clearEndString() {
    endFloorField.setValue(0);
  }

  @Override
  public void update(BuildingInterface building) {
    ElevatorInterface[] elevators = building.getElevators();

    for (int i = 0; i < elevators.length; i++) {
      StringBuilder sb = new StringBuilder();
      // Update the floor panels to highlight the elevator's position
      for (int j = 0; j < floorPanels[i].length; j++) {
        if (elevators[i].getCurrentFloor() == floorPanels[i].length - j - 1) {
          floorPanels[i][j].setBackground(new Color(29, 49, 93));
          doorLabels[i][j].setVisible(true);
          // Modify the door display
          if (elevators[i].getElevatorStatus().isOutOfService()
              && elevators[i].getCurrentFloor() == 0) {
            doorLabels[i][j].setText("Out of Service");
          } else if (elevators[i].getElevatorStatus().getEndWaitTimer() > 0) {
            doorLabels[i][j].setText("Waiting|"
                + elevators[i].getElevatorStatus().getEndWaitTimer());
          } else {
            if (elevators[i].isDoorClosed()) {
              doorLabels[i][j].setText(elevators[i].getCurrentFloor() + "|"
                  + elevators[i].getDirection().toString() + "|C");
            } else {
              doorLabels[i][j].setText(elevators[i].getCurrentFloor() + "|"
                  + elevators[i].getDirection().toString() + "|O ->"
                  + elevators[i].getElevatorStatus().getDoorOpenTimer() + "s");
            }
          }
        } else {
          floorPanels[i][j].setBackground(new Color(240, 244, 246));
          doorLabels[i][j].setVisible(false);
        }

        // Update the floor request labels
        if (elevators[i].getFloorRequests()[j]) {
          sb.append(String.format(" %2d", j));
        } else {
          sb.append(" --");
        }
      }
      floorRequestLabel[i].setText("Elevator " + i + ": <" + sb + " >");
    }

    // Update the system status panel
    if (building.getSystemStatus() == ElevatorSystemStatus.running) {
      systemStatusLabel.setText("Elevator System Running");
    } else if (building.getSystemStatus() == ElevatorSystemStatus.stopping) {
      systemStatusLabel.setText("Elevator System Stopping");
    } else if (building.getSystemStatus() == ElevatorSystemStatus.outOfService) {
      systemStatusLabel.setText("Elevator System Out of Service");
    }

    // Disable the continue button if the system is not out of service
    continueButton.setEnabled(building.getSystemStatus() == ElevatorSystemStatus.outOfService);

    // Disable the submit button if the system is not running
    submitButton.setEnabled(building.getSystemStatus() == ElevatorSystemStatus.running);

    // Disable the generate button if the system is not running
    generateRequestButton.setEnabled(building.getSystemStatus() == ElevatorSystemStatus.running);

    // Update the stored request panel
    if (!building.getUpRequests().isEmpty()) {
      List<Request> requests = building.getUpRequests();
      StringBuilder upRequest = new StringBuilder();
      for (Request request : requests) {
        upRequest.append(request.toString()).append("; ");
      }
      upRequestLabel.setText("Up: " + upRequest);
    } else {
      upRequestLabel.setText("Up: None");
    }
    if (!building.getDownRequests().isEmpty()) {
      List<Request> requests = building.getDownRequests();
      StringBuilder downRequest = new StringBuilder();
      for (Request request : requests) {
        downRequest.append(request.toString()).append("; ");
      }
      downRequestLabel.setText("Down: " + downRequest);
    } else {
      downRequestLabel.setText("Down: None");
    }
  }
}
