package ee.mikkelsaar.stockapi.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiExceptionUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertErrorToJSON(Exception error) throws IOException {
        return convertErrorToJSON(error.toString());
    }

    public static String convertErrorToJSON(String message) throws IOException {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("message", message);
        return objectMapper.writeValueAsString(error);
    }

    public static String convertErrorToJSON(ApiException er) throws JsonProcessingException {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now().toString());
        error.put("status", er.getStatus());
        error.put("message", er.getMessage());

        return objectMapper.writeValueAsString(error);
    }

}
