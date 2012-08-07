/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import oltpbenchadmin.commons.ExecuteConfiguration;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class ExecuteResult extends Result {

    private ExecuteConfiguration executeConfiguration;

    public ExecuteResult(ExecuteConfiguration executeConfiguration, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.executeConfiguration = executeConfiguration;
    }

    public ExecuteConfiguration getExecuteConfiguration() {
        return executeConfiguration;
    }
    
}