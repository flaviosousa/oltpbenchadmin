/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import java.io.Serializable;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public abstract class Result implements Serializable { 
    
    protected String errorMessage;
    protected String consoleMessage;
    
    public Result(String errorMessage, String consoleMessage) {
        this.consoleMessage = consoleMessage;
        this.errorMessage = errorMessage;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

    public void setConsoleMessage(String consoleMessage) {
        this.consoleMessage = consoleMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean hasError() {
        return (errorMessage != null && errorMessage.trim().length() > 0);
    }
    
    public boolean isSuccess() {
        return !hasError();
    }
    
}