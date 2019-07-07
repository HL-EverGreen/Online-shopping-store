package models.users;

import javax.persistence.Inheritance;
import java.util.LinkedHashMap;
import java.util.Map;

@Inheritance
public class Developer extends UserInfo {

    /**
     * Generic query helper for entity Developer with id Long
     */
    public static Find<String,Developer> find = new Find<String, Developer>(){};

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Developer c: Developer.find.orderBy("name").findList()) {
            options.put(c.emailID.toString(), c.name);
        }
        return options;
    }

    public void register() {

    }
}
