package io.github.oliviercailloux.y2018.apartments.apartment;

/**
 * This class indicates that we can retrieve a good address by the API. <br>
 * This Exception will be thrown only if the API returns an HTTP 200 but no address was found
 */
public class AddressApiException extends Exception {
  private static final long serialVersionUID = 8057892188727057681L;

  /**
   * Constructs an <code>AddressApiException</code> with the specified detail message.
   *
   * @param message the detail message.
   */
  public AddressApiException(String message) {
    super(message);
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the
   *        {@link Throwable#getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link Throwable#getCause()}
   *        method). (A {@code null} value is permitted, and indicates that the cause is nonexistent
   *        or unknown.)
   */
  public AddressApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
