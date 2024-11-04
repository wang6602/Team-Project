/**
 * ClientInterface Interface
 *
 * Client Class/Interface
 * This will be running on each individual user.
 * It will make a connection to the Client Handler class to connect to the network
 * This will be responsible for sending the information to Server, which will send
 * information to Database manager to execute desired actions
 *
 * <p>Purdue University -- CS18000 -- Fall 2024 -- Team Project -- DatabaseManager -- L14, Team 4</p>
 *
 * @authors Tatjana Trajkovic, Rohit Sattuluri, Sophia Zakar, Alan Wang, BLK
 * @version November 3, 2024
 */

public interface ClientHandlerInterface {

    public void run();
}
