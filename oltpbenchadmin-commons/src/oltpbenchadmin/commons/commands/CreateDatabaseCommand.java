/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import oltpbenchadmin.commons.Database;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class CreateDatabaseCommand extends Command {

    private Database database;

    public CreateDatabaseCommand(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}