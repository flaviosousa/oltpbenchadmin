/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import oltpbenchadmin.commons.Workload;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class SaveWorkloadDescriptorResult extends Result {

    private Workload workload;
    private boolean saved = false;
    
    public SaveWorkloadDescriptorResult(boolean saved, Workload workload, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.workload = workload;
        this.saved = saved;
    }

    public boolean isSaved() {
        return saved;
    }
    
}