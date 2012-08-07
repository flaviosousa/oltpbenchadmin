/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import java.util.List;
import oltpbenchadmin.commons.DatabaseSystem;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class GetDatabaseSystemsResult extends Result {

    private List<DatabaseSystem> databaseSystemList;

    public GetDatabaseSystemsResult(List<DatabaseSystem> databaseSystemList, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.databaseSystemList = databaseSystemList;
    }

    public List<DatabaseSystem> getDatabaseSystemList() {
        return databaseSystemList;
    }

}