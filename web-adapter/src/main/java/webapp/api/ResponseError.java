/*
 * Developed 2020 by m_afattah as a workshop demo.
 * All rights reserved.
 */
package webapp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 * Response error.
 *
 * @since 1.0
 */
public final class ResponseError {
    /**
     * Http status.
     */
    private final HttpStatus status;

    /**
     * Error message.
     */
    private final String message;

    /**
     * Error list.
     */
    private final List<String> error;

    /**
     * Constructor.
     *
     * @param status Http status.
     * @param message Message.
     * @param error List of errors.
     * @since 1.0
     */
    public ResponseError(final HttpStatus status, final String message, final List<String> error) {
        super();
        this.status = status;
        this.message = message;
        this.error = error;
    }

    /**
     * Helper constructor.
     *
     * @param status Http status.
     * @param message Message.
     * @param error Error.
     * @since 1.0
     */
    public ResponseError(final HttpStatus status, final String message, final String error) {
        this(status, message, Collections.singletonList(error));
    }

    /**
     * Helper constructor.
     *
     * @param status Http status.
     * @param message Message.
     * @since 1.0
     */
    public ResponseError(final HttpStatus status, final String message) {
        this(status, message, Collections.emptyList());
    }

    /**
     * Http Status.
     *
     * @return Http status.
     * @since 1.0
     */
    @JsonProperty("status")
    public HttpStatus httpStatus() {
        return this.status;
    }

    /**
     * Error message.
     *
     * @return Error message.
     * @since 1.0
     */
    @JsonProperty("message")
    public String errorMessage() {
        return this.message;
    }

    /**
     * Errors list.
     *
     * @return Errors list.
     * @since 1.0
     */
    @JsonProperty("errors")
    public List<String> errors() {
        return this.error;
    }

    @Override
    public String toString() {
        return String.format(
            "ResponseError{status=%s, message=%s, error=%s}",
            this.status,
            this.message,
            this.error.toString()
        );
    }
}
