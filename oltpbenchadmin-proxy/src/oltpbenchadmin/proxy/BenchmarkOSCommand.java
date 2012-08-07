/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin.proxy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oltpbenchadmin.commons.Database;
import oltpbenchadmin.commons.DatabaseSystem;
import oltpbenchadmin.commons.Workload;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public final class BenchmarkOSCommand {

    public static final int LINUX = 1;
    public static final int WINDOWS = 2;

    private BenchmarkOSCommand() {
    }

    public static int getOperationSystem() {
        String operationalSystem = System.getProperty("os.name");
        if (operationalSystem.equalsIgnoreCase(("linux"))) {
            return LINUX;
        } else {
            return WINDOWS;
        }
    }

    public static List<DatabaseSystem> getDatabaseSystems(List<DBMS> dbmsList) {
        List<DatabaseSystem> result = new ArrayList<DatabaseSystem>();
        if (getOperationSystem() == LINUX) {
            String psql = executeCommand(new String[]{"bash", "-c", "ps aux | grep postgres"});
            if (psql != null && psql.startsWith("postgres")) {
                DBMS dbms = null;
                for (DBMS dbmsItem : dbmsList) {
                    if (dbmsItem.getType() == DatabaseSystem.TYPE_POSTGRES) {
                        dbms = dbmsItem;
                        break;
                    }
                }
                if (dbms != null) {
                    DatabaseSystem ds = new DatabaseSystem();
                    ds.setType(DatabaseSystem.TYPE_POSTGRES);
                    ds.setDatabaseList(getDatabases(ds, dbms.getUsername(), dbms.getPassword()));
                    result.add(ds);
                }
            }
            String mysql = executeCommand(new String[]{"bash", "-c", "ps aux | grep mysql"});
            if (mysql != null && mysql.startsWith("mysql")) {
                DBMS dbms = null;
                for (DBMS dbmsItem : dbmsList) {
                    if (dbmsItem.getType() == DatabaseSystem.TYPE_MYSQL) {
                        dbms = dbmsItem;
                        break;
                    }
                }
                if (dbms != null) {
                    DatabaseSystem ds = new DatabaseSystem();
                    ds.setType(DatabaseSystem.TYPE_MYSQL);
                    ds.setDatabaseList(getDatabases(ds, dbms.getUsername(), dbms.getPassword()));
                    result.add(ds);
                }
            }
        }
        return result;
    }

    public static List<Database> getDatabases(DatabaseSystem databaseSystem, String username, String password) {
        List<Database> result = new ArrayList<Database>();
        if (getOperationSystem() == LINUX) {
            switch (databaseSystem.getType()) {
                case DatabaseSystem.TYPE_POSTGRES: {
                    try {
                        String s = "";
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"psql", "-h", "localhost", "-U", username, "-l"});
                        Map<String, String> env = pb.environment();
                        env.put("PGPASSWORD", password);
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        int c = 0;
                        while ((s = stdInput.readLine()) != null) {
                            String[] b = s.split("[|]");
                            if (b != null && b.length == 6 && b[0].trim().length() > 0 && c > 1) {
                                Database database = new Database(b[0].trim(), DatabaseSystem.TYPE_POSTGRES);
                                result.add(database);
                            }
                            c++;
                        }
                        while ((s = stdError.readLine()) != null);
                    } catch (IOException e) {
                    }
                    break;
                }
                case DatabaseSystem.TYPE_MYSQL: {
                    try {
                        String s = "";
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"mysqlshow", "-u", username, "--password=" + password});
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        int c = 0;
                        while ((s = stdInput.readLine()) != null) {
                            if (s.trim().length() > 2 && c > 2) {
                                Database database = new Database(s.trim().substring(1, s.trim().length() - 1).trim(), DatabaseSystem.TYPE_MYSQL);
                                result.add(database);
                            }
                            c++;
                        }
                        if (result.size() > 0) {
                            result.remove(result.size() - 1);
                        }
                        while ((s = stdError.readLine()) != null);
                    } catch (IOException e) {
                    }
                    break;
                }
            }
        }
        return result;
    }

    public static String getClassPath() {
        String result = "build";
        File libDir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "lib");
        if (libDir.isDirectory()) {
            result = result + getClassPath(libDir);
        } else {
            result = null;
        }
        return result;
    }

    private static String getClassPath(File file) {
        String result = "";
        String libPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "lib" + System.getProperty("file.separator");
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                result += getClassPath(f);
            }
        } else {
            if (file.getAbsolutePath().toLowerCase().endsWith(".jar")) {
                return ":" + "lib" + System.getProperty("file.separator") + file.getAbsolutePath().substring(libPath.length());
            }
        }
        return result;
    }

    private static String executeProcess(String command) {
        String result = "";
        try {
            String s = "";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            s = "";
            while ((s = stdInput.readLine()) != null) {
                result += s + "\n";
            }
            s = "";
            while ((s = stdError.readLine()) != null) {
                result += s + "\n";
            }
        } catch (IOException e) {
            result = "ERROR";
        }
        result = result.trim();
        return result;
    }

    private static String executeProcess(String[] command) {
        String result = "";
        try {
            String s = "";
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            s = "";
            while ((s = stdInput.readLine()) != null) {
                result += s + "\n";
            }
            s = "";
            while ((s = stdError.readLine()) != null) {
                result += s + "\n";
            }
        } catch (IOException e) {
            result = "ERROR";
        }
        result = result.trim();
        return result;
    }

    public static String createDatabaseSchema(Workload workload, String benchmarkClass, long samplingWindow, String outputFile) {
        if (getOperationSystem() == LINUX) {
            String command = "java -Xmx1000m -cp " + getClassPath() + " -Dlog4j.configuration=log4j.properties com.oltpbenchmark.DBWorkload -b " + benchmarkClass + " -c " + workload.getFileAbsolutePath().substring(workload.getBenchmarkPath().length() + 1) + " --create=true -s " + samplingWindow + " -o " + outputFile;
            String result = executeProcess(command);
            return result;
        }
        return null;
    }

    public static String loadDatabase(Workload workload, String benchmarkClass, long samplingWindow, String outputFile) {
        if (getOperationSystem() == LINUX) {
            String command = "java -Xmx1000m -cp " + getClassPath() + " -Dlog4j.configuration=log4j.properties com.oltpbenchmark.DBWorkload -b " + benchmarkClass + " -c " + workload.getFileAbsolutePath().substring(workload.getBenchmarkPath().length() + 1) + " --load=true -s " + samplingWindow + " -o " + outputFile;
            String result = executeProcess(command);
            return result;
        }
        return null;
    }
    
    public static String executeDatabase(Workload workload, String benchmarkClass, long samplingWindow, String outputFile) {
        if (getOperationSystem() == LINUX) {
            String command = "java -Xmx1000m -cp " + getClassPath() + " -Dlog4j.configuration=log4j.properties com.oltpbenchmark.DBWorkload -b " + benchmarkClass + " -c " + workload.getFileAbsolutePath().substring(workload.getBenchmarkPath().length() + 1) + " --execute=true -s " + samplingWindow + " -o " + outputFile;
            String result = executeProcess(command);
            return result;
        }
        return null;
    }

    public static boolean dropDatabase(Database database, String username, String password) {
        boolean success = true;
        if (getOperationSystem() == LINUX) {
            switch (database.getType()) {
                case DatabaseSystem.TYPE_POSTGRES: {
                    try {
                        String s = null;
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"dropdb", "-h", "localhost", "-U", username, database.getName()});
                        Map<String, String> env = pb.environment();
                        env.put("PGPASSWORD", password);
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        while ((s = stdInput.readLine()) != null) {
                        }
                        while ((s = stdError.readLine()) != null) {
                            success = false;
                        }
                    } catch (IOException e) {
                        success = false;
                    }
                    break;
                }
                case DatabaseSystem.TYPE_MYSQL: {
                    try {
                        String s = null;
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"mysqladmin", "-u", username, "-p", "drop", database.getName(), "--password=" + password, "-f"});
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        while ((s = stdInput.readLine()) != null) {
                        }
                        while ((s = stdError.readLine()) != null) {
                            success = false;
                        }
                    } catch (IOException e) {
                        success = false;
                    }
                    break;
                }
            }
        }
        return success;
    }

    public static boolean createDatabase(Database database, String username, String password) {
        boolean success = true;
        if (getOperationSystem() == LINUX) {
            switch (database.getType()) {
                case DatabaseSystem.TYPE_POSTGRES: {
                    try {
                        String s = null;
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"createdb", "-h", "localhost", "-U", username, database.getName()});
                        Map<String, String> env = pb.environment();
                        env.put("PGPASSWORD", password);
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        while ((s = stdInput.readLine()) != null) {
                        }
                        while ((s = stdError.readLine()) != null) {
                            success = false;
                        }
                    } catch (IOException e) {
                        success = false;
                    }
                    break;
                }
                case DatabaseSystem.TYPE_MYSQL: {
                    try {
                        String s = null;
                        ProcessBuilder pb = new ProcessBuilder(new String[]{"mysqladmin", "-u", username, "-p", "create", database.getName(), "--password=" + password});
                        Process p = pb.start();
                        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        s = "";
                        while ((s = stdInput.readLine()) != null) {
                        }
                        while ((s = stdError.readLine()) != null) {
                            success = false;
                        }
                    } catch (IOException e) {
                        success = false;
                    }
                    break;
                }
            }
        }
        return success;
    }

    public static String executeCommand(String command) {
        if (getOperationSystem() == LINUX) {
            return executeProcess(command);
        }
        return null;
    }

    public static String executeCommand(String[] command) {
        if (getOperationSystem() == LINUX) {
            return executeProcess(command);
        }
        return null;
    }

    public static List<Workload> getWorkloadDescriptors() {
        List<Workload> workloadList = new ArrayList<Workload>();
        File benchmark = new File(System.getProperty("user.dir"));
        List<String> fileXmlList = new ArrayList<String>();
        getAllXmlFile(benchmark, fileXmlList);
        for (String fileName : fileXmlList) {
            try {
                Workload workload = WorkloadDescriptorLoader.createWorkload(benchmark.getAbsolutePath(), new File(fileName));
                if (workload.getDbType() != null && workload.getDbType().trim().length() > 0) {
                    workloadList.add(workload);
                }
            } catch (IOException ex) {
                continue;
            }
        }
        return workloadList;
    }

    private static void getAllXmlFile(File file, List<String> fileXmlList) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (int i = 0; fileList != null && i < fileList.length; i++) {
                getAllXmlFile(fileList[i], fileXmlList);
            }
        } else {
            if (file.getAbsolutePath().contains(".") && file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")).equalsIgnoreCase(".xml")) {
                fileXmlList.add(file.getAbsolutePath());
            }
        }
    }
}