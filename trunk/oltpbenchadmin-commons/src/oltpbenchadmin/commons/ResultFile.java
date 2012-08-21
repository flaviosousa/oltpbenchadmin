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
public class ResultFile implements Serializable {

    private String fileAbsolutePath;
    private String benchmarkPath;

    public ResultFile(String fileAbsolutePath, String benchmarkPath) {
        this.fileAbsolutePath = fileAbsolutePath;
        this.benchmarkPath = benchmarkPath;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public String getBenchmarkPath() {
        return benchmarkPath;
    }

    @Override
    public String toString() {
        return fileAbsolutePath.substring(benchmarkPath.length());
    }
}