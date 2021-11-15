package ee.mikkelsaar.stockapi.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public ApiException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

}
