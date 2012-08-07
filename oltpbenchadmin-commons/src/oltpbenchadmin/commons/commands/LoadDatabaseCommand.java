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
public class LoadDatabaseCommand extends Command {

    private Workload workload;
    private String benchmarkClass;
    private long samplingWindow;
    private String outputFile;
    
    public LoadDatabaseCommand(Workload workload, String benchmarkClass, long samplingWindow, String outputFile) {
        this.workload = workload;
        this.benchmarkClass = benchmarkClass;
        this.samplingWindow = samplingWindow;
        this.outputFile = outputFile;
    }

    public Workload getWorkload() {
        return workload;
    }

    public String getBenchmarkClass() {
        return benchmarkClass;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public long getSamplingWindow() {
        return samplingWindow;
    }
    
}