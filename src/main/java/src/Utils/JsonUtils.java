package src.Utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {
    private final static ObjectMapper mapper = newObjectMapperToRead();

    public static class JsonAsMapReference extends TypeReference<HashMap<String, Object>> {

    }


    public static Map<String, Object> readJsonAsMap(final String source) {
        JsonUtils.JsonAsMapReference typeRef = new JsonAsMapReference();
        try {
            return newObjectMapperToRead().readValue(source, typeRef);
        } catch (IOException e) {
            System.out.println("Invalid json: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String toJsonFromObject(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            System.out.println("Invalid Json: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public static ObjectMapper newObjectMapperToRead() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        return mapper;
    }

}
