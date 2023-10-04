package casestudy2.control;

import casestudy2.model.User;
import casestudy2.view.ServerView;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerControl {

    private ArrayList<User> list;

    private int serverPort;
    private ServerSocket myServer;
    private Socket clientSocket;
    private ServerView view;

    public ServerControl(ServerView view) {
        this.view = view;
        list = new ArrayList<User>();
        list.add(new User("0987654321", "111111"));
        list.add(new User("0988888888", "111111"));
        list.add(new User("0977777777", "111111"));
        list.add(new User("0987575701", "111111"));
        //openServer(6868);
    }

    public void openServer(int port) {
        this.serverPort = port;
    }

    public void listening() {
        try (ServerSocket serverSocket = new ServerSocket(this.serverPort)) {
            System.out.println("Server is listening at the port: " + this.serverPort);
            while (true) {
                Socket socketFromClient = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socketFromClient.getInputStream());
                User user = (User) ois.readObject();
                OutputStream output = socketFromClient.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                if (this.checkLogin(user)) {
                    this.view.showMessage("New Client Connected");
                    writer.print("Success");
                } else {
                    this.view.showMessage("Failed");
                    writer.print("Failed");
                }

                writer.flush();
                writer.close();
                output.close();
            }

        } catch (Exception e) {
            System.err.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean checkLogin(User user) {
        for (User u : list) {
            if (u.equals(user)) {
                return true;
            }
        }
        return false;
    }
}
