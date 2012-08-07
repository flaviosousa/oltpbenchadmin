/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.util.List;
import oltpbenchadmin.comm.ProxyConnection;
import oltpbenchadmin.commons.ExecuteConfiguration;
import oltpbenchadmin.commons.commands.ExecuteCommand;
import oltpbenchadmin.commons.commands.ExecuteResult;
import oltpbenchadmin.commons.commands.Result;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class ExecuteThread extends Thread {

    private List<ProxyConnection> proxyConnectionList;
    private List<ExecuteResult> executeResults;
    private ExecuteConfiguration executeConfiguration;
    private ProxyConnection proxyConnection;
    
    public ExecuteThread(List<ProxyConnection> proxyConnectionList, List<ExecuteResult> executeResults, ExecuteConfiguration executeConfiguration) {
        this.proxyConnectionList = proxyConnectionList;
        this.executeResults = executeResults;
        this.executeConfiguration = executeConfiguration;
        proxyConnection = getProxyConnectionByString(executeConfiguration.getProxyConnectionString());
    }

    @Override
    public void run() {
        ExecuteCommand command = new ExecuteCommand(executeConfiguration);
        Result result = proxyConnection.executeCommand(command);
        executeResults.add(((ExecuteResult) result));
    }

    private ProxyConnection getProxyConnectionByString(String string) {
        if (string == null) {
            return null;
        }
        for (ProxyConnection pc : proxyConnectionList) {
            if (string.equals(pc.toString())) {
                return pc;
            }
        }
        return null;
    }
}