/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class GetProxyStateResult extends Result {

    private boolean activated = false;
    
    public GetProxyStateResult(boolean activated, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.activated = activated;
    }
    
    public boolean isActivated() {
        return activated;
    }
    
}