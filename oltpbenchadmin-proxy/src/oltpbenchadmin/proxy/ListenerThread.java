/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.*;
import java.net.Socket;
import java.util.List;
import oltpbenchadmin.commons.DatabaseSystem;
import oltpbenchadmin.commons.ExecuteConfiguration;
import oltpbenchadmin.commons.Workload;
import oltpbenchadmin.commons.commands.*;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class ListenerThread extends Thread {

    private Socket socket;
    private String threadId;
    private List<DBMS> dbmsList;

    public ListenerThread(List<DBMS> dbmsList, Socket request) {
        setName(String.valueOf(System.nanoTime()));
        this.dbmsList = dbmsList;
        socket = request;
        threadId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }

    public String getThreadId() {
        return threadId;
    }

    @Override
    public void run() {
        OutputMessage.printAction("[" + threadId + "]: Connection proxy starting");
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        boolean proceed = true;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            OutputMessage.printError("[" + threadId + "]: Closing proxy connection");
            proceed = false;
        }
        OutputMessage.printAction((Thread.activeCount() - 2) + " thread(s) in execution");
        while (proceed && socket != null && socket.isConnected()) {
            try {
                Command command = (Command) inputStream.readObject();
                Result result = null;
                if (command instanceof ExecuteCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Get a Execution Script - Starting...");
                    ExecuteCommand executeCommand = (ExecuteCommand) command;
                    ExecuteConfiguration executeConfiguration = (ExecuteConfiguration) executeCommand.getExecuteConfiguration();
                    String strResult = BenchmarkOSCommand.executeDatabase(executeConfiguration.getWorkload(), executeConfiguration.getBenchmarkClass(), executeConfiguration.getSamplingWindow(), executeConfiguration.getOutputFile());
                    String consoleResult = null;
                    String errorMessage = null;
                    if (strResult != null && !strResult.equalsIgnoreCase("error")) {
                        consoleResult = "Execute Database \n" + strResult;
                    } else {
                        errorMessage = "Execute Database \n" + "ERROR";
                    }
                    result = new ExecuteResult(executeConfiguration, errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Execution Script - Finished");
                }
                if (command instanceof GetWorkloadDescriptorsCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Get a Workload Descriptor File - Starting...");
                    String consoleResult = null;
                    String errorMessage = null;
                    List<Workload> resultList = BenchmarkOSCommand.getWorkloadDescriptors();
                    result = new GetWorkloadDescriptorsResult(resultList, errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Get a Workload Descriptor File - Finished");
                }
                if (command instanceof GetDatabaseSystemsCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Get a Database Systems List - Starting...");
                    String consoleResult = null;
                    String errorMessage = null;
                    List<DatabaseSystem> resultList = BenchmarkOSCommand.getDatabaseSystems(dbmsList);
                    result = new GetDatabaseSystemsResult(resultList, errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Get a Database Systems List - Finished");
                }
                if (command instanceof GetProxyStateCommand) {
                    result = new GetProxyStateResult(true, "", "");
                }
                if (command instanceof SaveWorkloadDescriptorCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Save a Workload Descriptor File - Starting...");
                    SaveWorkloadDescriptorCommand c = (SaveWorkloadDescriptorCommand) command;
                    boolean saved;
                    String consoleResult = null;
                    String errorMessage = null;
                    try {
                        PrintWriter writer = new PrintWriter(new File(c.getWorkloadDescriptor().getFileAbsolutePath()));
                        writer.print(c.getWorkloadDescriptor().toXML());
                        writer.flush();
                        writer.close();
                        saved = true;
                    } catch (FileNotFoundException ex) {
                        saved = false;
                    }
                    if (saved) {
                        consoleResult = "Workload descriptor file was saved successfully";;
                    } else {
                        errorMessage = "Unable to save the workload descriptor file";
                    }
                    result = new SaveWorkloadDescriptorResult(saved, ((SaveWorkloadDescriptorCommand) command).getWorkloadDescriptor(), errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Save a Workload Descriptor File - Finished");
                }
                if (command instanceof CreateDatabaseSchemaCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Create a Database Schema - Starting...");
                    CreateDatabaseSchemaCommand c = (CreateDatabaseSchemaCommand) command;
                    String strResult = BenchmarkOSCommand.createDatabaseSchema(c.getWorkload(), c.getBenchmarkClass(), c.getSamplingWindow(), c.getOutputFile());
                    String consoleResult = null;
                    String errorMessage = null;
                    if (strResult != null && !strResult.equalsIgnoreCase("error")) {
                        consoleResult = "Create Database Schema\n" + strResult;
                    } else {
                        errorMessage = "Create Database Schema\n" + "ERROR";
                    }
                    result = new CreateDatabaseSchemaResult(c.getWorkload(), c.getBenchmarkClass(), c.getSamplingWindow(), c.getOutputFile(), errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Create a Database Schema - Finished");
                }
                if (command instanceof LoadDatabaseCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Load Data in Database - Starting...");
                    LoadDatabaseCommand c = (LoadDatabaseCommand) command;
                    String strResult = BenchmarkOSCommand.loadDatabase(c.getWorkload(), c.getBenchmarkClass(), c.getSamplingWindow(), c.getOutputFile());
                    String consoleResult = null;
                    String errorMessage = null;
                    if (strResult != null && !strResult.equalsIgnoreCase("error")) {
                        consoleResult = "Load Database Data\n" + strResult;
                    } else {
                        errorMessage = "Load Database Data\n" + "ERROR";
                    }
                    result = new LoadDatabaseResult(c.getWorkload(), c.getBenchmarkClass(), c.getSamplingWindow(), c.getOutputFile(), errorMessage, consoleResult);
                    OutputMessage.printAction("[" + threadId + "]: Load Data in Database - Finished");
                }
                if (command instanceof DropDatabaseCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Drop Database - Starting...");
                    DropDatabaseCommand c = (DropDatabaseCommand) command;
                    DBMS dbms = getDbms(c.getDatabase().getType());
                    if (dbms == null) {
                        result = new DropDatabaseResult(c, null, null, "Not exists an administrative account for this DBMS in the proxy configuration file", null);
                    } else {
                        if (BenchmarkOSCommand.dropDatabase(c.getDatabase(), dbms.getUsername(), dbms.getPassword())) {
                            result = new DropDatabaseResult(c, dbms.getUsername(), dbms.getPassword(), null, "Database deleted successfully");
                        } else {
                            result = new DropDatabaseResult(c, dbms.getUsername(), dbms.getPassword(), null, "Unable to delete this database");
                        }
                    }
                    OutputMessage.printAction("[" + threadId + "]: Drop Database - Finished");
                }
                if (command instanceof CreateDatabaseCommand) {
                    OutputMessage.printAction("[" + threadId + "]: Create Database - Starting...");
                    CreateDatabaseCommand c = (CreateDatabaseCommand) command;
                    DBMS dbms = getDbms(c.getDatabase().getType());
                    if (dbms == null) {
                        result = new CreateDatabaseResult(c, null, null, "Not exists an administrative account for this DBMS in the proxy configuration file", null);
                    } else {
                        if (BenchmarkOSCommand.createDatabase(c.getDatabase(), dbms.getUsername(), dbms.getPassword())) {
                            result = new CreateDatabaseResult(c, dbms.getUsername(), dbms.getPassword(), null, "Database created successfully");
                        } else {
                            result = new CreateDatabaseResult(c, dbms.getUsername(), dbms.getPassword(), null, "Unable to create this database");
                        }
                    }
                    OutputMessage.printAction("[" + threadId + "]: Create Database - Finished");
                }
                outputStream.writeObject(result);
                outputStream.flush();
            } catch (IOException ex) {
                OutputMessage.printAction("[" + threadId + "]: Closing proxy connection");
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        socket = null;
                    }
                }
                break;
            } catch (ClassNotFoundException ex) {
                OutputMessage.printError("[" + threadId + "]: Closing proxy connection");
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex1) {
                        socket = null;
                    }
                }
                break;
            }
        }
        OutputMessage.printAction("[" + threadId + "]: Connection proxy ended");
        OutputMessage.printAction((Thread.activeCount() - 3) + " thread(s) in execution");
        close();
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex1) {
                socket = null;
            }
        }
    }

    public boolean isClosed() {
        return socket == null;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ListenerThread && ((ListenerThread) obj).getName().equals(getName()));
    }

    public DBMS getDbms(int type) {
        for (DBMS dbms : dbmsList) {
            if (dbms.getType() == type) {
                return dbms;
            }
        }
        return null;
    }
}