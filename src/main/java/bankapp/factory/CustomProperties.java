package bankapp.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomProperties {

    public enum DBAccessType {JPA, MONGO}

    @Value("${bankapp.dbAccessType:JPA}")
    public String dbAccessTypeString;

    public DBAccessType getDbAccessType() {
        return DBAccessType.valueOf(dbAccessTypeString);
    }

}
