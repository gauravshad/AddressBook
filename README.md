# AddressBook

## Elastic Search Setup
1. This is a dependency for this project to run that elastic search service shoud be running already.
2. Start Elastic Search service `elasticsearch` . This will start the service on default ports.

## Build Instructions
1. I am using maven to build this project.
2. When you build the project, it will run the test cases and build the jar in the target folder.
3. Change current directory to target `cd target`
4. Use this command to run the ticket service `java -jar AddressBook-1.0-SNAPSHOT.jar 8080` . 8080 is the command line argument which tells the port number on which to start the Rest server. You can use any integral values for those arguments. This command will start the service where you can easily naviagte through the command line interface.
