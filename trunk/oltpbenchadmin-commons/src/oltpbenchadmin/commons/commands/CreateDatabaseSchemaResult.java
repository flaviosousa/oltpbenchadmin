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
public class CreateDatabaseSchemaResult extends Result {

    private Workload workload;
    private String benchmarkClass;
    
    public CreateDatabaseSchemaResult(Workload workload, String benchmarkClass, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.workload = workload;
        this.benchmarkClass = benchmarkClass;
    }
    
    public Workload getWorkload() {
        return workload;
    }

    public String getBenchmarkClass() {
        return benchmarkClass;
    }

}