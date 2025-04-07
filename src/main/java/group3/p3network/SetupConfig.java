package group3.p3network;

public class SetupConfig {
    public Config setup(String[] args) {
        Config config = null;

        if (args.length > 0) {
            if (args[0].equals("--help")) {
                showHelp();
                System.exit(1);
            }

            if (!notConnection(args[1])) {
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
    private Config readConfigFromFile() {

        // Didn't get to read the file...
        return null;
    }
}
