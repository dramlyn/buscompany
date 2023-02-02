package buscompany.exception;

public class ServerException extends RuntimeException {
    private final ServerErrorCode errorCode;

    private final String field;

    private final String message;

    public ServerException(ServerErrorCode err, String field, String message){
        this.errorCode = err;
        this.field = field;
        this.message = message;
    }

    public ServerErrorCode getErrorCode(){
        return errorCode;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
