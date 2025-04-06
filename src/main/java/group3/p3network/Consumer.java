package group3.p3network;

public class Consumer {
    public static void main(String[] args) {
        String target = "localhost:50051";
        if (args.length > 0) {
            if (args.length == 1 && args[0].equals("--help")) {
                System.err.println("Syntax: hostname:port");
                System.err.println("");
                System.err.println("   hostname  localhost or ip address in " +
                    "the format of XXX.XXX.XXX.XXX");
                System.err.println("   port      port number");
                System.exit(1);
            }
            target = args[0];
        }

    }
}
