/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public final class OutputMessage {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

    private OutputMessage() {
    }

    public static void printError(String message) {
        System.out.println("ERROR: <" + DATE_FORMAT.format(new Date()) + "> " + message);
    }

    public static void printAction(String message) {
        System.out.println("<" + DATE_FORMAT.format(new Date()) + "> " + message);
    }
}