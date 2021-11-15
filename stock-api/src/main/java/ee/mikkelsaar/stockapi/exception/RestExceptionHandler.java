package ee.mikkelsaar.stockapi.exception;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Exception e) throws IOException {
        log.error("Rest error occurred \n " + e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                .body(ApiExceptionUtil.convertErrorToJSON(e));
    }

    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleApiError(ApiException e) throws IOException {
        return ResponseEntity.status(e.getStatus()).contentType(MediaType.APPLICATION_JSON)
                .body(ApiExceptionUtil.convertErrorToJSON(e));
    }

}
