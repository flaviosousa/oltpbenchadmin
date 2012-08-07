/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import java.util.List;
import oltpbenchadmin.commons.Workload;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class GetWorkloadDescriptorsResult extends Result {

    private List<Workload> workloadList;

    public GetWorkloadDescriptorsResult(List<Workload> workloadList, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.workloadList = workloadList;
    }

    public List<Workload> getWorkloadList() {
        return workloadList;
    }
    
}