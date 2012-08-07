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
public class ExecuteCommand extends Command {

    private ExecuteConfiguration executeConfiguration;

    public ExecuteCommand(ExecuteConfiguration executeConfiguration) {
        this.executeConfiguration = executeConfiguration;
    }

    public ExecuteConfiguration getExecuteConfiguration() {
        return executeConfiguration;
    }

}