package ru.mail.polis.radleweird;

import java.util.HashMap;
import java.util.Map;

public class QueryWorker {
    public static Map<String, String> getQueryPairs(String query) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int equalMarkPos = pair.indexOf("=");
            if (equalMarkPos != -1) {
                String key = pair.substring(0, equalMarkPos);
                String value = pair.substring(equalMarkPos + 1);
                result.put(key, value);
            }
        }
        return result;
    }
}
