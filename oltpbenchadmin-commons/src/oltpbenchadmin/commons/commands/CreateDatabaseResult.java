/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class CreateDatabaseResult extends Result {

    private CreateDatabaseCommand createDatabaseCommand;
    private String username;
    private String password;

    public CreateDatabaseResult(CreateDatabaseCommand createDatabaseCommand, String username, String password, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.createDatabaseCommand = createDatabaseCommand;
        this.username = username;
        this.password = password;
    }
  
}