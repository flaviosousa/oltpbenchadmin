/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class DatabaseSystem implements Serializable {

    public static final int TYPE_POSTGRES = 1;
    public static final int TYPE_MYSQL = 2;
    private int type;
    private List<Database> databaseList;

    public DatabaseSystem() {
        databaseList = new ArrayList<Database>();
    }

    public List<Database> getDatabaseList() {
        return databaseList;
    }

    public void setDatabaseList(List<Database> databaseList) {
        this.databaseList = databaseList;
    }
    
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        switch (type) {
            case TYPE_MYSQL: {
                return "MySQL";
            }
            case TYPE_POSTGRES: {
                return "PostgreSQL";
            }
        }
        return super.toString();
    }
}