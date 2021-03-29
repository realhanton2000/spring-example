package bankapp.cache;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CacheEventLogger implements CacheEventListener<Integer, Long> {
    Logger logger = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(
            CacheEvent<? extends Integer, ? extends Long> cacheEvent) {
        List<String> l = new ArrayList<String>();
        Integer key;
        Long value;
        l.add((key = cacheEvent.getKey()) != null ? key.toString() : "");
        l.add((value = cacheEvent.getOldValue()) != null ? value.toString() : "");
        l.add((value = cacheEvent.getNewValue()) != null ? value.toString() : "");
        logger.info(cacheEvent.getType().toString() + "-" + (l.stream().reduce((a, b) -> (a + ":" + b)).orElse(l.get(0))).toString());
    }
}
