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
public class SaveWorkloadDescriptorCommand extends Command {
    
    private Workload workload;
    
    public SaveWorkloadDescriptorCommand(Workload workload) {
        this.workload = workload;
    }
    
    public Workload getWorkloadDescriptor() {
        return workload;
    }
    
}