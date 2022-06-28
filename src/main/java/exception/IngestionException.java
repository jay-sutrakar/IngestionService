package exception;

import lombok.Getter;

@Getter
public class IngestionException extends RuntimeException {
    private final int statusCode;
    private final String description;
    public IngestionException(int statusCode, String description, Throwable throwable) {
        super(throwable);
        this.statusCode = statusCode;
        this.description = description;
    }
}
