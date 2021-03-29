package bankapp.factory;

import bankapp.factory.handler.DataRepoHandler;

import java.util.HashMap;
import java.util.Map;

public class RepoFactory {

    private static Map<CustomProperties.DBAccessType, DataRepoHandler> strategyMap = new HashMap<CustomProperties.DBAccessType, DataRepoHandler>();

    public static DataRepoHandler getInvokeStrategy(CustomProperties.DBAccessType dbAccessType) {
        return strategyMap.get(dbAccessType);
    }

    public static void register(CustomProperties.DBAccessType dbAccessType, DataRepoHandler dataRepoHandler) {
        if (dbAccessType == null || dataRepoHandler == null) {
            return;
        }
        strategyMap.put(dbAccessType, dataRepoHandler);
    }
}
