/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

import java.util.List;
import oltpbenchadmin.commons.ResultFile;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class GetResultFilesResult extends Result {

    private List<ResultFile> resultFileList;

    public GetResultFilesResult(List<ResultFile> resultFileList, String errorMessage, String consoleMessage) {
        super(errorMessage, consoleMessage);
        this.resultFileList = resultFileList;
    }

    public List<ResultFile> getResultFileList() {
        return resultFileList;
    }
    
}