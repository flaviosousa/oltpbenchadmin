/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import oltpbenchadmin.commons.DatabaseSystem;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class Main {

    public static void main(String[] args) {
        StringBuilder help = new StringBuilder();
        help.append("Command: java -jar oltpbtool-proxy.jar <proxy port>");
        if (args == null || args.length != 1) {
            help.append("\n");
            help.append("Invalid argument to <proxy port>");
            OutputMessage.printAction(help.toString());
            System.exit(0);
        }
        Integer proxyPort = null;
        try {
            proxyPort = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            help.append("\n");
            help.append("Invalid argument to <proxy port>");
            OutputMessage.printAction(help.toString());
            System.exit(0);
        }
        OutputMessage.printAction("Parameters' test was successful");
        File fileOltpBenchmarkPath = new File(System.getProperty("user.dir"));
        if (fileOltpBenchmarkPath == null || !fileOltpBenchmarkPath.exists()) {
            help.append("\n");
            help.append("This jar file is not in the root folder of OLTPBenchmark");
            OutputMessage.printAction(help.toString());
            System.exit(0);
        }
        if (!fileOltpBenchmarkPath.canRead() || !fileOltpBenchmarkPath.canWrite() || !fileOltpBenchmarkPath.canExecute()) {
            help.append("\n");
            help.append("The directory containing the jar file does not have the necessary permissions");
            OutputMessage.printAction(help.toString());
            System.exit(0);
        }
        OutputMessage.printAction("The OLTPBenchmark's path is correct");

        List<DBMS> dbmsList = new ArrayList<DBMS>();

        File databaseSystemConfiguration = new File("oltpbtool-proxy.xml");
        if (!databaseSystemConfiguration.exists()) {
            try {
                databaseSystemConfiguration.createNewFile();
                PrintWriter writer = new PrintWriter(databaseSystemConfiguration);
                writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.println("<dbms_list>");
                writer.println("    <!-- Example for the PostgreSQL -->");
                writer.println("    <dbms>");
                writer.println("        <type>" + "postgres" + "</type>");
                writer.println("        <username>" + "postgres" + "</username>");
                writer.println("        <password>" + "password" + "</password>");
                writer.println("    </dbms>");
                writer.println("    <!-- Example for the MySQL -->");
                writer.println("    <dbms>");
                writer.println("        <type>" + "mysql" + "</type>");
                writer.println("        <username>" + "root" + "</username>");
                writer.println("        <password>" + "password" + "</password>");
                writer.println("    </dbms>");
                writer.println("</dbms_list>");
                writer.close();
                OutputMessage.printAction("ATTENTION: An example of the configuration file (oltpbtool-proxy.xml) was created. Please fill in the configuration file and start again oltpbtool-proxy. This is necessary so that you can manipulate the database through the grafical interface");
                System.exit(0);
            } catch (IOException ex) {
                help.append("\n");
                help.append("Can not create the oltpbtool-proxy configuration file");
                OutputMessage.printAction(help.toString());
                System.exit(0);
            }
        } else {
            SAXBuilder builder = new SAXBuilder();
            try {
                org.jdom2.Document document = builder.build(databaseSystemConfiguration);
                Element rootNode = document.getRootElement();
                List<Element> dbmsListNode = rootNode.getChildren();
                for (Element e : dbmsListNode) {
                    DBMS dbms = new DBMS();
                    String type = e.getChild("type").getValue();
                    if (type.equalsIgnoreCase("postgres")) {
                        dbms.setType(DatabaseSystem.TYPE_POSTGRES);
                    }
                    if (type.equalsIgnoreCase("mysql")) {
                        dbms.setType(DatabaseSystem.TYPE_MYSQL);
                    }
                    dbms.setUsername(e.getChild("username").getValue());
                    dbms.setPassword(e.getChild("password").getValue());
                    dbmsList.add(dbms);
                }
            } catch (JDOMException ex) {
                help.append("\n");
                help.append("Error processing configuration file");
                OutputMessage.printAction(help.toString());
                System.exit(0);
            } catch (IOException ex) {
                help.append("\n");
                help.append("Error processing configuration file");
                OutputMessage.printAction(help.toString());
                System.exit(0);
            }
        }

        for (DBMS dbms : dbmsList) {
            OutputMessage.printAction(dbms.toString() + " configuration has been loaded into oltpbtool-proxy");
        }
        
        try {
            Listener listener = new Listener(dbmsList, proxyPort);
            listener.start();
        } catch (IOException ex) {
            help.append("\n");
            help.append(("Unable to open the server on port " + proxyPort));
            OutputMessage.printAction(help.toString());
            System.exit(0);
        }
        OutputMessage.printAction("The oltpbtool-proxy was initiated successfully");
    }
}