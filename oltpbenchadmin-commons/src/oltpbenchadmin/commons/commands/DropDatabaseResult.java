/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class DropDatabaseResult extends Result {

    private DropDatabaseCommand dropDatabaseCommand;
    private String username;
    private String password;

    public DropDatabaseResult(DropDatabaseCommand dropDatabaseCommand, String username, String password, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.dropDatabaseCommand = dropDatabaseCommand;
        this.username = username;
        this.password = password;
    }

}