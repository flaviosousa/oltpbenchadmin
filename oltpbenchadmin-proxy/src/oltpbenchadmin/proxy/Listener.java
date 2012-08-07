/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class Listener extends Thread {

    private ServerSocket serverSocket;
    private int serverPort;
    private List<DBMS> dbmsList;
    
    public Listener(List<DBMS> dbmsList, int serverPort) throws IOException {
        Runtime.getRuntime().addShutdownHook(new InterruptProxy());
        this.dbmsList = dbmsList;
        this.serverPort = serverPort;
        serverSocket = new ServerSocket(serverPort);
    }

    @Override
    public void run() {
        OutputMessage.printAction("oltpbenchadmin listener is running on port " + serverPort);
        while (serverSocket != null && !serverSocket.isClosed()) {
            try {
                Socket request = serverSocket.accept();
                OutputMessage.printAction("Receiving a connection request from " + request.getInetAddress().getHostAddress() + ":" + request.getPort());
                ListenerThread thread = new ListenerThread(dbmsList, request);
                thread.start();
            } catch (IOException ex) {
                OutputMessage.printError(ex.getMessage());
                System.exit(0);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}