/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class InterruptProxy extends Thread {

    public InterruptProxy() {
    }

    @Override
    public void run() {
        System.out.println("");
        OutputMessage.printAction("The oltpbtool-proxy was ended");
    }
}