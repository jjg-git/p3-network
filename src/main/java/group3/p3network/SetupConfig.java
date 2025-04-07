package group3.p3network;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class SetupConfig {
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
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                writeDefaultConfigFile(configFile);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return configFile;
    }
    private Config readConfigFromFile() {
        File configFile = loadConfigFile();

        // Didn't get to read the file...
        return null;
    }
}
