package group3.p3network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

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

        // Didn't get to read the file...
        return null;
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
