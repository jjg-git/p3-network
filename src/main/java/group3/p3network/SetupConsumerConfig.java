package group3.p3network;

import java.io.*;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class SetupConsumerConfig {
    private int threads = 2;
    private String target = "localhost:50051";
    private final String defaultDirectory = "consumer-dir/";
    private String directory = defaultDirectory;

    private final String filename = "consumer-config.txt";

    public ConsumerConfig setup(String[] args) {
        ConsumerConfig config = null;

        if (args.length > 0) {
            config = handleArgs(args);
        }
        if (config == null) {
            System.out.println("Reading the configuration file...");
            config = readConfigFromFile();
            System.out.println("Read the configuration file.");
        }

        System.out.println("Configuration set:");
        System.out.println("threads: " + threads);
        System.out.println("target: " + target);
        System.out.println("directory: " + directory);

        return config;
    }

    private ConsumerConfig handleArgs(String[] args) {
        if (args.length == 1 && args[0].equals("--help")) {
            showHelp();
            System.exit(1);
        }

        System.out.println("Taking from command line arguments...");
        int threadArg = Integer.parseInt(args[0]);

        String targetArg = args[1];

        if (!networkAvailable(targetArg)) {
            System.err.println("Target \"" + targetArg + "\" cannot be " +
                "reached");
            System.exit(1);
        }

        // There is an output argument
        String directoryArg = defaultDirectory;
        if (args.length == 3) {

            directoryArg = args[2];
            if (!checkDirectory(directoryArg)) {
                System.err.println("Cannot find the directory " +
                    "named \"" + directoryArg + "\".");

                System.exit(1);
            } else {
                System.out.println("Found \"" + directoryArg + "\" directory.");
            }
        }

        return new ConsumerConfig(
            threadArg,
            targetArg,
            directoryArg
        );
    }

    private void makeDefaultDirectory() {
        Path pathToDefaultDirectory = Path.of(defaultDirectory);

        if (Files.exists(pathToDefaultDirectory)) {
            return;
        }

        try {
            System.err.println("Creating a directory named \""
                + defaultDirectory + "\" where the videos will be downloaded.");
            Files.createDirectory(pathToDefaultDirectory);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean checkDirectory(String directory) {
        return new File(directory).exists();
    }

    private boolean networkAvailable(String targetArg) {
        try {
            return InetAddress.getByName(targetArg) != null;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    private void showHelp() {
        System.err.println("Syntax: hostname:port [directory]\n");
        System.err.println("    hostname  localhost or ip address in " +
            "the format of XXX.XXX.XXX.XXX");
        System.err.println("    port      port number");
        System.err.println("    directory    name of the directory");
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

    private ConsumerConfig readConfigFromFile() {
        File configFile = loadConfigFile();

        try (BufferedReader reader =
             new BufferedReader(
                 new FileReader(configFile)
             )
        ){
            String line;
            ArrayList<String> lines = new ArrayList<>();
            ConsumerConfig result;

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

    private ConsumerConfig handleThisLine(ArrayList<String> lines) {
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
            handleDirectory(option, valueToParse);
        }

        parseConnection(valueToParse);
    }

    private void handleDirectory(String option, String valueToParse) {
        if (!option.equals("output")) {
            return;
        }

        parseDirectory(valueToParse);
    }

    private void parseDirectory(String valueToParse) {
        directory = valueToParse;
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
