package group3.p3network;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;

public class SetupConsumerConfig {
    private int threads = 2;
    private int port = 50051;
    private final String filename = "producer-config.txt";

    public ProducerConfig setup(String[] args) {
        ProducerConfig config = null;

        if (args.length > 0) {
            System.out.println("Taking from command line arguments...");
            if (args[0].equals("--help")) {
                showHelp();
                System.exit(1);
            }

            String portArg = args[1];

            if (!portIsAvailable(args[1])) {
                System.err.println("Port " + portArg + " is used");
                System.exit(1);
            }

            config = new ProducerConfig(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1])
            );
        }
        if (config == null) {
            System.out.println("Reading the configuration file...");
            config = readConfigFromFile();
            System.out.println("Read the configuration file.");
        }

        System.out.println("Configuration set:");
        System.out.println("threads: " + threads);
        System.out.println("port: " + port);

        return config;
    }

    private void showHelp() {
        System.err.println("Syntax: threads port\n");
        System.err.println("    threads  number of threads to run");
        System.err.println("    port     port number");
        System.exit(1);
    }

    private boolean portIsAvailable(String port) {
        try {
            new ServerSocket(Integer.parseInt(port));
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private File loadConfigFile() {
        File configFile = new File(filename);
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
            writer.write("threads " + threads + "\n");
            writer.write("connection " + port + "\n");

            System.out.println("Created a configuration file in " +
                configFile.getPath() + ".");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ProducerConfig readConfigFromFile() {
        File configFile = loadConfigFile();

        try (BufferedReader reader =
             new BufferedReader(
                 new FileReader(configFile)
             )
        ){
            String line;
            ArrayList<String> lines = new ArrayList<>();
            ProducerConfig result;

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

    private ProducerConfig handleThisLine(ArrayList<String> lines) {
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

        return new ProducerConfig(
            threads,
            port
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
        port = Integer.parseInt(valueToParse);
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
