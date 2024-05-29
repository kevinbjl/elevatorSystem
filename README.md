### Overview
This project is a simple implementation of an elevator system.
It's able to take user requests, then distribute those requests to the elevator and demonstrate the behaviour of the elevator system.
The executable is in the res folder.

### List of Features
__Step the system by a number of steps you want__: The user can specify the number of steps to step the system by using the spinner to the left of the step button.

__Generate random requests__: The user can generate random requests by specifying the number of requests to generate using the spinner to the left of the generate button.

__Gradual animation__: The system will show a gradual animation of the elevators moving if the user steps the system by more than 1 step.

### How to Run
It is highly recommended to run the program from the command line with the following command:

```java -jar BuildingWithSwing.jar <number of floors> <number of elevators> <capacity>```

_number of floors_: The number of floors in the system.\
_number of elevators_: The number of elevators in the system.\
_capacity_: The capacity of each elevator.

However, I've set the default values for the number of floors, number of elevators and capacity to 10, 5 and 10 respectively, so the jar should still work without any arguments:

```java -jar BuildingWithSwing.jar```

### How to Use the Program
__Status Panel__: The status panel at the top shows the current status of the elevator system (running, stopping or out of service) and its basic information (number of floors, number of elevators and capacity).

__Request Panel__: The request panel shows requests that are waiting to be processed, and the stops that each elevator will make.

__Elevator Panel__: This is the main panel of the system. It shows each elevator's current position, along with its status, direction, door status, and a timer that shows how long the door will be open or how long the elevator will be waiting.

__Input Panel__: The input panel allows users to enter a request. The user can specify the start and end floor in the spinner. The spinner does not allow user to enter a non-numeric value, and it sets an upper limit based on the number of floors. The submit button will submit the request to the system.\
The user can also specify the number of random requests to generate using the spinner on the right, then click the generate button to generate and submit those requests. The spinner has an upper limit which is the capacity.

__Control Panel__: The control panel allows the user to step, halt, continue or quit the elevator system. By using the spinner to the left of the step button, the user can specify the number of steps to step the system by.

### Design/Model Changes
I've made a huge change from the early design. In the earliest design, I let the Building class inherits from the BuildingReport class, because it uses a lot of methods from the BuildingReport class. However, when it comes to actually implementing it, I found out that this is not optimal. Although the Building class does use a majority of methods from the BuildingReport class, it does not use all of them, and it also has its own methods.\
Furthermore, the logic is not intuitive because Building and BuildingReport are not an "is-a" relationship. Building is not a type of BuildingReport.

Therefore, I decided to switch to composition instead of inheritance. I let the Building class have a BuildingReport object, and I delegate some methods to the BuildingReport object. This way, the Building class can use the methods from the BuildingReport class without having to inherit from it.

### Assumption
1. Number of floors must be between 3 and 30. However, if there are too many floors, the program will not be able to display all of them on the screen.
2. Capacity must be between 3 and 20.
3. There is no limit on number of elevators, but if it exceeds 11, the program will not be able to display all of them on the screen.

### Limitation
1. The system will simply quit when the number of requests exceeds the capacity of the elevator. This is not a reflection of the real world, where the excessive requests will be queued and processed later.
2. The program must be run from the command line. It cannot be run by double-clicking the jar file.

### Citation
I've used the following resources to help me with this project:
1. [Java Swing Tutorial](https://www.javatpoint.com/java-swing)
2. OpenAI & GitHub. (2024). GitHub Copilot. https://copilot.github.com/
