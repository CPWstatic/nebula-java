package com.vesoft.nebula.tools;

import com.google.common.net.HostAndPort;
import com.vesoft.nebula.graph.client.GraphClient;
import com.vesoft.nebula.graph.client.GraphClientImpl;
import java.util.List;
import org.apache.log4j.Logger;


public class ClientManager {
    private static final Logger LOGGER = Logger.getLogger(ClientManager.class.getClass());

    private static ThreadLocal<GraphClient> clientThreadLocal = new ThreadLocal<>();

    public static GraphClient getClient(List<HostAndPort> hostAndPorts, Options options)
            throws GetClientFailException {
        GraphClient client = clientThreadLocal.get();
        if (client == null) {
            client = new GraphClientImpl(hostAndPorts,
                    options.timeout, options.connectionRetry, options.executionRetry);
            if (client.connect(options.user, options.password) != 0) {
                throw new GetClientFailException("Connect fail.");
            }
            if (client.execute(String.format(Constant.USE_TEMPLATE, options.spaceName)) != 0) {
                throw new GetClientFailException("Switch space fail.");
            }
            LOGGER.info(Thread.currentThread().getName()
                    + ": switch to space " + options.spaceName);
            clientThreadLocal.set(client);
        }

        return client;
    }

    public static class GetClientFailException extends Exception {
        public GetClientFailException(String message) {
            super(message);
        }
    }
}
