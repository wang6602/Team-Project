Phase One README.txt File

User Class/Interface
- This class is used for storing each individual users' information.
  This class stores things related to users such as username, userID, password, profile picture, friends, and blocked users
  The users class will interact heavily with the Database Manager class, where it will store it along with the chat Data.

DatabaseManager Class
- This class is used for interacting with the various databases, like messaging and the users database
  It will store the users in usersDatabase and chats in their own text files.
  It also has a ChatID database to store the filenames of chats between certain users
  This class will be responsible from receiving information from the server class to create new messages, delete texts, add users, delete users, and more

Server Class
-  This is the main server, and it handles connections with the clients.
   Uses the Database Manager class to handle the actions of the clients

Client Handler Class
Client connects to this class which extends thread.
This class creates a thread for the client so that the server can handle multiple clients at once using concurrency.

Client Class/Interface
This will be running on each individual user. It will make a connection to the Client Handler class to connect to the network
This will be responsible for sending the information to Server, which will send information to Database manager to execute desired actions

RunLocalTest
-Implements JUnit test cases that were written to ensure that methods in User class and DatabaseManager class work as intended

PROGRAM INSTRUCTIONS
When first running the program and creating the first user, make sure the userDatabase.txt does not exist.
The program will automatically create this file.
If not, the program will not work.
Additionally, when testing the program, make sure to only have the classes, interfaces, and the chatIDs.txt file. All other files, like the text chats and the usertDatabase should be deleted.
Then run the RunLocalTest File.

TESTING INSTRUCTION
To set up the testing environment, first run the Server.java class.
Now that the server is running, Clients will be able to connect to the server, and the RunLocalTestPhase2 can be run.
To run the test cases for phase to, run the server, then run the RunLcoalTestPhase2.java file.








