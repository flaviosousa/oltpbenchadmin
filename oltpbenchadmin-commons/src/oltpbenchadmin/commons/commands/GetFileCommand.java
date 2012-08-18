/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons.commands;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class GetFileCommand extends Command {

    private String filePath;
    
    public GetFileCommand(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
    
}
