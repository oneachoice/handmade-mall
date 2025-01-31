package newbies.handmade_mall.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
public class ResponseApi <T> {

    private final HttpStatus httpStatus;

    private final String message;

    private final Optional<T> optData;

    private ResponseApi(HttpStatus httpStatus, String message, T data) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.optData = Optional.ofNullable(data);
    }

    private ResponseApi(HttpStatus httpStatus, String message) {
        this(httpStatus, message, null);
    }

    public static <T> ResponseApi<T> of(HttpStatus httpStatus, String message, T data) {
        return new ResponseApi<>(httpStatus, message, data);
    }

    public static <T> ResponseApi<T> of(HttpStatus httpStatus, String message) {
        return new ResponseApi<>(httpStatus, message);
    }
}
