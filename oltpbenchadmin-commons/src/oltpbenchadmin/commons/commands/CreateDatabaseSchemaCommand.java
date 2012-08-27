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
public class CreateDatabaseSchemaCommand extends Command {

    private Workload workload;
    private String benchmarkClass;
    
    public CreateDatabaseSchemaCommand(Workload workload, String benchmarkClass) {
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