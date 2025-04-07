package group3.p3network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

public class SetupConfig {
    private int threads = 2;
    private String connection = "localhost:50051";

    public Config setup(String[] args) {
        Config config = null;

        if (args.length > 0) {
            if (args[0].equals("--help")) {
                showHelp();
                System.exit(1);
            }

            if (notConnection(args[1])) {
                System.exit(1);
            }

            config = new Config(
                Integer.parseInt(args[0]),
                args[1]
            );
        }
        if (config == null) {
            config = readConfigFromFile();
        }

        return config;
    }

    private void showHelp() {
        System.err.println("Syntax: threads [hostname:port]\n");
        System.err.println("    threads   number of threads to run");
        System.err.println("    hostname  localhost or ip address in " +
            "the format of XXX.XXX.XXX.XXX");
        System.err.println("    port      port number");
        System.exit(1);
    }

    private boolean notConnection(String connection) {
        boolean result = false;

        if (!connection.contains(":")) {
            return true;
        }

        String[] split = connection.split(":");

        String hostname = split[0];
        String port = split[1];

        try {
            InetAddress.getByName(hostname);
        } catch (UnknownHostException e) {
            System.err.println(e.getMessage());
        }

        try {
            new ServerSocket(Integer.parseInt(port));
            result = true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return !result;
    }

    private File loadConfigFile() {
        File configFile = new File("server-config.txt");
        try {
            if (!configFile.createNewFile()) {
                writeDefaultConfigFile(configFile);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return configFile;
    }

    private void writeDefaultConfigFile(File configFile) {
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("threads " + threads);
            writer.write("connection " + connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Config readConfigFromFile() {
        File configFile = loadConfigFile();

        try (BufferedReader reader =
             new BufferedReader(
                 new FileReader(configFile)
             )
        ){
            String line;
            ArrayList<String> lines = new ArrayList<>();
            Config result;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            reader.close();

            result = handleThisLine(lines);

            if (result == null) {
                System.err.println("File is empty!");
            }

            return result;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        // Didn't get to read the file...
        return null;
    }

    private Config handleThisLine(ArrayList<String> lines) {
        for (String line : lines) {
            String[] splitLine = line.split(" ");

            if (splitLine.length < 2) {
                System.err.println(
                    "Missing value or option in the config file"
                );
                return null;
            }

            String optionToCheck = splitLine[0];
            String valueToCheck = splitLine[1];

            CheckArg check = new CheckArg();

            if (!check.thisOption(optionToCheck)) {
                System.err.println("Option " + optionToCheck + " is not available!");
                return null;
            }

            handleConfigs(optionToCheck, valueToCheck);
        }

        return new Config(
            threads,
            connection
        );
    }

    private void handleConfigs(String option, String valueToParse) {
        handleThreads(option, valueToParse);
    }

    private void handleThreads(String option, String valueToParse) {
        if (!option.equals("threads")){
            handleConnection(option, valueToParse);
            return;
        }
        parseThreads(valueToParse);
    }

    private void handleConnection(String option, String valueToParse) {
        if (!option.equals("connection")) {
            return;
        }

        parseConnection(valueToParse);
    }

    private void parseThreads(String valueToParse) {
        threads = Integer.parseInt(valueToParse);
    }

    private void parseConnection(String valueToParse) {
        if (notConnection(valueToParse)) {
            System.err.println(valueToParse + " is not a valid " +
                "connection");
            return;
        }

        connection = valueToParse;
    }


    private static class CheckArg {
        private final HashMap<String, Object> neededOptions = new HashMap<>();

        CheckArg() {
            neededOptions.put("threads", int.class);
            neededOptions.put("connection", String.class);
        }

        public boolean thisOption(String optionToFind) {
            return neededOptions.containsKey(optionToFind);
        }
    }
}
