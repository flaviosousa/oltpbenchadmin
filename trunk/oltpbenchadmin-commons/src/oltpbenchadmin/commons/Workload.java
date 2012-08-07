/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class Workload implements Serializable {

    private String dbType;
    private String driver;
    private String dbUrl;
    private String username;
    private String password;
    private String isolation;
    private int scaleFactor;
    private int terminals;
    private List<Work> works;
    private List<TransactionType> transactionTypes;
    private String fileAbsolutePath;
    private String benchmarkPath;

    public Workload(String fileAbsolutePath, String benchmarkPath) {
        this.fileAbsolutePath = fileAbsolutePath;
        this.benchmarkPath = benchmarkPath;
        works = new ArrayList<Work>();
        transactionTypes = new ArrayList<TransactionType>();
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public String getBenchmarkPath() {
        return benchmarkPath;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getIsolation() {
        return isolation;
    }

    public void setIsolation(String isolation) {
        this.isolation = isolation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getTerminals() {
        return terminals;
    }

    public void setTerminals(int terminals) {
        this.terminals = terminals;
    }

    public List<TransactionType> getTransactionTypes() {
        return transactionTypes;
    }

    public void setTransactionTypes(List<TransactionType> transactionTypes) {
        this.transactionTypes = transactionTypes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    @Override
    public String toString() {
        return fileAbsolutePath.substring(benchmarkPath.length());
    }

    public String toXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("\n");
        sb.append("<parameters>");
        sb.append("\n");
        sb.append(("    <dbtype>" + treatGetMethodToXML(getDbType()) + "</dbtype>"));
        sb.append("\n");
        sb.append(("    <driver>" + treatGetMethodToXML(getDriver()) + "</driver>"));
        sb.append("\n");
        sb.append(("    <DBUrl>" + treatGetMethodToXML(getDbUrl()) + "</DBUrl>"));
        sb.append("\n");
        sb.append(("    <username>" + treatGetMethodToXML(getUsername()) + "</username>"));
        sb.append("\n");
        sb.append(("    <password>" + treatGetMethodToXML(getPassword()) + "</password>"));
        sb.append("\n");
        sb.append(("    <isolation>" + treatGetMethodToXML(getIsolation()) + "</isolation>"));
        sb.append("\n");
        sb.append(("    <scalefactor>" + getScaleFactor() + "</scalefactor>"));
        sb.append("\n");
        sb.append(("    <terminals>" + getTerminals() + "</terminals>"));
        sb.append("\n");
        sb.append(("    <works>"));
        sb.append("\n");
        for (Work work : getWorks()) {
            sb.append(("        <work>"));
            sb.append("\n");
            sb.append(("            <time>" + work.getTime() + "</time>"));
            sb.append("\n");
            sb.append(("            <rate>" + work.getRate() + "</rate>"));
            sb.append("\n");
            sb.append(("            <weights>" + treatGetMethodToXML(work.getWeights()) + "</weights>"));
            sb.append("\n");
            sb.append(("        </work>"));
            sb.append("\n");
        }
        sb.append(("    </works>"));
        sb.append("\n");
        sb.append(("    <transactiontypes>"));
        sb.append("\n");
        for (TransactionType transactionType : getTransactionTypes()) {
            sb.append(("        <transactiontype>"));
            sb.append("\n");
            sb.append(("            <name>" + treatGetMethodToXML(transactionType.getName()) + "</name>"));
            sb.append("\n");
            sb.append(("        </transactiontype>"));
            sb.append("\n");
        }
        sb.append(("    </transactiontypes>"));
        sb.append("\n");
        sb.append("</parameters>");
        return sb.toString();
    }
    
    private String treatGetMethodToXML(String string) {
        return (string == null ? "" : string);
    }
}