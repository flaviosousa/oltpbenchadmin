/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.Serializable;
import oltpbenchadmin.commons.DatabaseSystem;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class DBMS implements Serializable {

    private int type;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        switch (type) {
            case DatabaseSystem.TYPE_MYSQL: {
                return "MySQL";
            }
            case DatabaseSystem.TYPE_POSTGRES: {
                return "PostgreSQL";
            }
        }
        return super.toString();
    }
}