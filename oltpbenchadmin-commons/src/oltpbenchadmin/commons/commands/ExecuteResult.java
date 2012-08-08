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
    private long executionTime;

    public ExecuteResult(ExecuteConfiguration executeConfiguration, long executionTime, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.executionTime = executionTime;
        this.executeConfiguration = executeConfiguration;
    }

    public ExecuteConfiguration getExecuteConfiguration() {
        return executeConfiguration;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}