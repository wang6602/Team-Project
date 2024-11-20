Phase One README.txt File

TESTING INSTRUCTION

- To set up the testing environment, first run the Server.java class.
Now that the server is running, click run on RunLocalTestPhase2 and RunLocalTest. Then the test Cases will pass.
- Clients will be able to connect to the server, and the RunLocalTestPhase2 can be run.
To run the test cases for phase 2, run the server, then run the RunLcoalTestPhase2.java file.


User Class/Interface
- This class is used for storing each individual users' information.
  This class stores things related to users such as username, userID, password, profile picture, friends, and blocked users
  The users class will interact heavily with the Database Manager class, where it will store it along with the chat Data.

UserDatabase.txt
- This text file will be automatically created by the program to store an ArrayList of the Users using ObjectOutputStream.

ChatID.txt
- This file will store the fileNames of each groupChat followed by the Users in the provided grouptchat.
EX. Groupchat1,user1,user2.
- The database manager class will use the groupChat name to find the according file of the specific groupChat, and retrieve, delete, and add text messages


DatabaseManager Class
- This class is used for interacting with the various databases, like messaging and the users database
  It will store the users in usersDatabase and chats in their own text files.
  It also has a ChatID database to store the filenames of chats between certain users
  This class will be responsible from receiving information from the server class to create new messages, delete texts, add users, delete users, and more

Server Class
-  This is the main server, and it handles connections with the clients.
   Once a client is connected, it creates a new thread of the ClientHandlerClass, to allow multiple connections to the Database at once.

Client Handler Class
- Client connects to this class which extends thread.
This class creates a thread for the client so that the server can handle multiple clients at once using concurrency.

Client Class/Interface
- This will be running on each individual user. It will make a connection to the Client Handler class to connect to the network
This will be responsible for sending the information to Server, which will send information to Database manager to execute desired actions

RunLocalTest and RunLocalTestPhase2
- Implements JUnit test cases that were written to ensure that methods in User class and DatabaseManager class work as intended













