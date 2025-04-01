package group3.p3network.networking;

public class RPC {
    private static volatile RPC instance;
    private static final Object mutex = new Object();

    private RPC() {}

    public static RPC GetInstance() {
        RPC result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    instance = result = new RPC();
                }
            }
        }

        return result;
    }
}
