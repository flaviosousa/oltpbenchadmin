/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons;

import java.io.Serializable;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class TransactionType implements Serializable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}