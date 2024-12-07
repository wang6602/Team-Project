README File

TESTING INSTRUCTION

- To set up the testing environment, first run the Server.java class. The remaining steps are below, depending on whether you want to test the GUI or test cases.

For testing GUI
- Run MainScreen.java. You will be directed to the login screen
- Create an account or login. Now you will enter the Chat page.
- To interact with others, head to "My Profile". 
- Click on another user to follow them and head back to the chatPage.
- Now you can create a chat with that individual.
- Keep in mind to use the Reload Button to update the Screen regularly.


For testing test cases
- Now that the server is running, click run on RunLocalTestPhase2 and RunLocalTest. Then the test Cases will pass.
- Clients will be able to connect to the server, and the RunLocalTestPhase2 can be run.
- To run the test cases for phase 2, run the server, then run the RunLcoalTestPhase2.java file.

SUBMISSIONS
- Rohit Sattuluri submitted the project presentation on Brightspace
- Tatjana Trajkovic submitted the project report on Brightspace
- Rohit Sattuluri submitted on Vocareum workspace

ABOUT THE CLASSES

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

MainScreen class
- This is the central class for our gui. This is where the client starts whenever the program is run. The MainScreen will change the screens, as the user navigates the App.

LoginPanel class
- This class has the GUI for our login screen. The MainScreen will initially direct the User to enter the Login page
- The login GUI will require the User to login or create a new user to advance to the other pages of the app.

ChatPanel Class
- This class has the GUI for accessing user's chats. Once the user logs in, they will be directed to this screen.
- On this Panel, the user can send messages, create new chats, add users to chats, delete texts, remove users from chats, and logout.
-Note that the User can only create chats/send messages to people that they follow. This can be found in the User Panel Class

UserPanel Class
- This class has the GUI for the user to view their information, like thier username, password, and profiile Pic. We also setup the GUI to be able to change certain parts of the user's info.
- On the right side, you can see the all of the people on the app, as well as the individuals that the user follows.

OtherUserPanel Class
- When clicking on another person's profile, this GUI code in this class will display another user's info to the current user. There is also a follow/block button to interact with that specific user. 

RunLocalTest and RunLocalTestPhase2
- Implements JUnit test cases that were written to ensure that methods in User class and DatabaseManager class work as intended













