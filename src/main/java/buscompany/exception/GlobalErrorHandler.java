package buscompany.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.*;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationExcResponse handleValidation(MethodArgumentNotValidException exc) {
        final ValidationExcResponse errResponse = new ValidationExcResponse();
        exc.getBindingResult().getFieldErrors().forEach(fieldError-> {
            errResponse.getErrorResponses().add(new ErrorResponse(ServerErrorCode.valueOf("WRONG_" + fieldError.
                    getField().toUpperCase().replace(".","_").replaceAll("\\[\\d]", "").replace("[]","")),
                    fieldError.getField(), fieldError.getDefaultMessage()));
        });

        return errResponse;
    }

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidation(ServerException ex){
        return new ErrorResponse(ex.getErrorCode(), ex.getField(), ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidation(HttpRequestMethodNotSupportedException ex){
        return new ErrorResponse(ServerErrorCode.WRONG_METHOD, "", "You chose wrong method or entered wrong url.");
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidation(MissingRequestCookieException ex){
        return new ErrorResponse(ServerErrorCode.MISSING_COOKIE, ex.getCookieName(), "You didn't send a cookie JAVASESSIONID");
    }

    @ExceptionHandler(DateTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidation(DateTimeException ex){
        return new ErrorResponse(ServerErrorCode.PARSE_EXCEPTION, "date", "You entered invalid date");
    }


    /*@ExceptionHandler(MySQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationExcResponse handleValidation(MySQLIntegrityConstraintViolationException ex){
        final ValidationExcResponse response = new ValidationExcResponse();
        response.getErrorResponses().add(new ErrorResponse(ServerErrorCode.WRONG_LOGIN, ex.getMessage(), "fdklglkdf"));
        return response;
    }*/



    public static class ValidationExcResponse {
        private List<ErrorResponse> errorResponses = new ArrayList<>();

        public List<ErrorResponse> getErrorResponses() {
            return errorResponses;
        }

        public void setErrorResponses(List<ErrorResponse> errorResponses) {
            this.errorResponses = errorResponses;
        }
    }
    public static class ErrorResponse {

        private ServerErrorCode errorCode;
        private String field;

        private String message;

        public ErrorResponse(ServerErrorCode errorCode, String field, String message) {
            this.errorCode = errorCode;
            this.field = field;
            this.message = message;
        }

        public ServerErrorCode getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(ServerErrorCode errorCode) {
            this.errorCode = errorCode;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
