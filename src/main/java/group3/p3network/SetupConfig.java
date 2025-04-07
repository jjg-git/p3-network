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

        return config;
    }
}
