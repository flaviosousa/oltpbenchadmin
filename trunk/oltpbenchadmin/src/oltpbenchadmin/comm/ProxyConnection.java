/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import oltpbenchadmin.MainForm;
import oltpbenchadmin.commons.DatabaseSystem;
import oltpbenchadmin.commons.Workload;
import oltpbenchadmin.commons.commands.*;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class ProxyConnection extends Thread {

    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private MainForm mainForm;
    private List<Workload> workloadList;
    private List<DatabaseSystem> databaseSystemList;

    public ProxyConnection(String host, int port, MainForm mainForm) throws IOException {
        this.mainForm = mainForm;
        this.host = host;
        this.port = port;
        socket = new Socket(host, port);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
        workloadList = new ArrayList<Workload>();
        databaseSystemList = new ArrayList<DatabaseSystem>();
        updateProxy();

    }

    public synchronized void updateProxy() {
        Result workloadResult = executeCommand(new GetWorkloadDescriptorsCommand());
        if (workloadResult != null && workloadResult instanceof GetWorkloadDescriptorsResult && ((GetWorkloadDescriptorsResult) workloadResult).getWorkloadList() != null) {
            workloadList = ((GetWorkloadDescriptorsResult) workloadResult).getWorkloadList();
        }
        Result databaseSystemResult = executeCommand(new GetDatabaseSystemsCommand());
        if (databaseSystemResult != null && databaseSystemResult instanceof GetDatabaseSystemsResult && ((GetDatabaseSystemsResult) databaseSystemResult).getDatabaseSystemList() != null) {
            databaseSystemList = ((GetDatabaseSystemsResult) databaseSystemResult).getDatabaseSystemList();
        }
    }

    @Override
    public void run() {
        while (socket != null && !socket.isClosed() && socket.isConnected()) {
            try {
                sleep(5000);
                Result proxyResult = executeCommand(new GetProxyStateCommand());
                if (proxyResult == null || (proxyResult instanceof GetProxyStateResult && !((GetProxyStateResult) proxyResult).isActivated())) {
                    close();
                }
            } catch (InterruptedException ex) {
                close();
            }
        }
        close();
        mainForm.removeProxyConnection(this);
    }

    public synchronized Result executeCommand(Command command) {
        Result result = null;
        try {
            outputStream.writeObject(command);
            outputStream.flush();
            try {
                result = (Result) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                result = null;
            }
        } catch (IOException ex) {
            result = null;
        } finally {
        }
        return result;
    }

    public synchronized ByteArrayOutputStream executeCommand(GetFileCommand command) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            outputStream.writeObject(command);
            outputStream.flush();
            try {
                Object received = inputStream.readObject();
                while (received != null && received instanceof byte[]) {
                    result.write((byte[]) received);
                    System.out.println("write " + ((byte[]) received).length);
                    received = inputStream.readObject();
                }
            } catch (ClassNotFoundException e) {
                result = null;
            }
        } catch (IOException ex) {
            result = null;
        } finally {
            
        }
        return result;
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ex) {
            socket = null;
        }
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getLocalHost() {
        return socket.getLocalAddress().getHostAddress();
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public Socket getSocket() {
        return socket;
    }

    public List<Workload> getWorkloadList() {
        return workloadList;
    }

    public List<DatabaseSystem> getDatabaseSystemList() {
        return databaseSystemList;
    }

    @Override
    public boolean equals(Object obj) {
        return (host != null && obj instanceof ProxyConnection && host.equals(((ProxyConnection) obj).getHost()) && port == ((ProxyConnection) obj).getPort())
                && (getLocalHost() != null && obj instanceof ProxyConnection && getLocalHost().equals(((ProxyConnection) obj).getLocalHost()) && getLocalPort() == ((ProxyConnection) obj).getLocalPort());
    }

    @Override
    public String toString() {
        String result = "";
        result += getLocalHost() + ":" + getLocalPort() + " <-> " + "proxy@" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        return result;
    }
}