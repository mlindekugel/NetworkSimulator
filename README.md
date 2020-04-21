# Support
We're all teachers and tight for money; writing this program took a considerable amount of time and effort. If you find it useful, please consider sending a donation through PayPal to mlinde@hotmail.com . The link to do so is here: https://www.paypal.com/myaccount/transfer/homepage

# Compilation and running
To compile: 
javac *.java

To run: 
java NetworkSimulator

To create a jar file to distribute to students:
jar cfm NetworkSimulator.jar MANIFEST.MF *.class

You could distribute the jar file to students if they have computers with Java. To run the program, they would type:
java -jar NetworkSimulator.jar

# NetworkSimulator
NetworkSimulator is centered on learning goals of PLTW CSP 2.1.1 activity

This simulation allows the user to specify several "machines" and draw connections between them. Messages are then assigned to be sent from one machine to another. The user converts the messages to packets, then steps them through the network one machine at a time by clicking the "Route Packets" button. Parameters that can be controlled include the time to live for each packet and the buffer size of all machines.

To place a machine, left-click where it should appear. To Move a machine, drag it to a new location using the right mouse button. To delete a machine, left-click it.

To create a connection between two machines, left-click the first machine then drag to the machine to which it should be connected and release. All connections are two-way. To delete a connection, move the mouse over it till its color turns from black to yellow, then click.

To assign a message to a machine, right-click the machine; a dialog will appear to allow you to assign a destination machine and to change the text if desired. 

You can change the time to live for all packets by changing the text box labeled TTL and clicking the TTL button; change the buffer size by changing the value in the text box labeled Buffer and clicking the Buffer button.

To run the simulation, click "Create packets." This converts all messages to packets (and deletes the original message). Click "Route packets" repeatedly to move packets from one machine to a connected machine. At any point during the simulation, you can left-click a machine to look at the packets it is holding. The machine will turn red and display in a gray box all the packets that are in transition, as well as each packet's TTL value. It will display in white all packets destined for it that it has received. 

Two status bars appear at the top of each machine. 

The left bar indicates how many packets are in transit on that machine; if it is white, there are no packets in transit, if it is black and gray, the black bar indicates the percentage of the machine's buffer filled by packets in transition. Any packets beyond the buffer capacity are dropped (a message indicating this is printed to the terminal).

The right bar indicates how many packets destined for the machine have been received. If it is white, none have been received. If it is green and red, the green bar indicates the percentage of expected packets that have been received.

While the simulation is running, network connections can be broken or created, but machines cannot be deleted.

To exit the simulation, click Clear Packets.
