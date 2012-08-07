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
public class ExecuteConfiguration implements Serializable {
    
    private Workload workload;
    private String benchmarkClass;
    private long samplingWindow;
    private String outputFile;
    private String proxyConnectionString;

    public String getBenchmarkClass() {
        return benchmarkClass;
    }

    public void setBenchmarkClass(String benchmarkClass) {
        this.benchmarkClass = benchmarkClass;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getProxyConnectionString() {
        return proxyConnectionString;
    }

    public void setProxyConnectionString(String proxyConnectionString) {
        this.proxyConnectionString = proxyConnectionString;
    }

    public long getSamplingWindow() {
        return samplingWindow;
    }

    public void setSamplingWindow(long samplingWindow) {
        this.samplingWindow = samplingWindow;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }
    
}