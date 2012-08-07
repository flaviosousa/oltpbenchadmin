/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import oltpbenchadmin.commons.TransactionType;
import oltpbenchadmin.commons.Work;
import oltpbenchadmin.commons.Workload;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public final class WorkloadDescriptorLoader {

    public static Workload createWorkload(String benchmarkPath, File workloadDescriptorXML) throws IOException {
        Workload workload = new Workload(workloadDescriptorXML.getAbsolutePath(), benchmarkPath);
        SAXBuilder builder = new SAXBuilder();
        try {
            org.jdom2.Document document = builder.build(workloadDescriptorXML);
            try {
                setWorkload(document, workload);
            } catch (Exception ex) {
                throw new IOException(ex.getMessage());
            }
        } catch (JDOMException ex) {
            throw new IOException(ex.getMessage());
        }
        return workload;
    }

    private static void setWorkload(org.jdom2.Document document, Workload workload) {
        Element parameters = document.getRootElement();
        for (Element e : parameters.getChildren()) {
            if (e.getName().equalsIgnoreCase("dbType")) {
                workload.setDbType(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("driver")) {
                workload.setDriver(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("dbUrl")) {
                workload.setDbUrl(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("username")) {
                workload.setUsername(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("password")) {
                workload.setPassword(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("isolation")) {
                workload.setIsolation(e.getValue());
            }
            if (e.getName().equalsIgnoreCase("scaleFactor")) {
                workload.setScaleFactor(Integer.parseInt(e.getValue()));
            }
            if (e.getName().equalsIgnoreCase("terminals")) {
                workload.setTerminals(Integer.parseInt(e.getValue()));
            }
        }
        Element works = parameters.getChild("works");
        for (Element eWorks : works.getChildren()) {
            Work work = new Work();
            for (Element e : eWorks.getChildren()) {
                if (e.getName().equalsIgnoreCase("time")) {
                    work.setTime(Long.parseLong(e.getValue()));
                }
                if (e.getName().equalsIgnoreCase("rate")) {
                    work.setRate(Long.parseLong(e.getValue()));
                }
                if (e.getName().equalsIgnoreCase("weights")) {
                    work.setWeights(e.getValue());
                }
            }
            workload.getWorks().add(work);
        }
        Element transactionTypes = parameters.getChild("transactiontypes");
        for (Element eTransactionsTypes : transactionTypes.getChildren()) {
            TransactionType transactionType = new TransactionType();
            for (Element e : eTransactionsTypes.getChildren()) {
                if (e.getName().equalsIgnoreCase("name")) {
                    transactionType.setName(e.getValue());
                }
            }
            workload.getTransactionTypes().add(transactionType);
        }
    }
}