package com.ufrn.pertindetu.base.utils.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.ufrn.pertindetu.base.dto.ApiResponseDTO;
import com.ufrn.pertindetu.base.dto.ErrorDTO;
import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.base.utils.exception.ConversionException;
import com.ufrn.pertindetu.base.utils.exception.ResourceNotFoundException;
import com.ufrn.pertindetu.base.utils.logging.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Global exception handler for controllers in the application.
 * Handles various types of exceptions and maps them to appropriate error
 * responses.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Pattern JACKSON_ENUM_ERROR = Pattern.compile(
            "from String \"([^\"]*)\".*?Enum class: \\[([^]]+)]", Pattern.DOTALL);

    Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Handles BusinessException and maps it to a custom error response.
     *
     * @param exception The BusinessException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(
            BusinessException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> businessException(BusinessException exception,
                                                                      HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                exception.getHttpStatusCode().value(),
                "Business error",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(exception.getHttpStatusCode()).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    /**
     * Handles validation errors from @Valid on request body (MethodArgumentNotValidException).
     * @return ResponseEntity containing the error response with details about which fields failed validation.
     * @param ex The MethodArgumentNotValidException instance containing validation errors.
     * @param request The HttpServletRequest.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors.toString(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    /**
     * Handles invalid query/path parameters (e.g. enum or numeric conversion failures).
     *
     * @param ex      The MethodArgumentTypeMismatchException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String param = ex.getName();
        Object raw = ex.getValue();
        String value = raw != null ? raw.toString() : "null";
        Class<?> requiredType = ex.getRequiredType();
        String message;
        if (requiredType != null && requiredType.isEnum()) {
            String allowed = Arrays.stream(requiredType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            message = String.format(
                    "Invalid value '%s' for parameter '%s'. Allowed values: %s.",
                    value, param, allowed);
        } else {
            message = String.format(
                    "Invalid value '%s' for parameter '%s'.",
                    value, param);
        }
        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request parameter",
                message,
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    /**
     * Handles JSON deserialization failures (invalid enum value, wrong type, malformed JSON).
     *
     * @param ex      The HttpMessageNotReadableException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {

        String rawMessage = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        String message;
        var enumMatch = JACKSON_ENUM_ERROR.matcher(rawMessage != null ? rawMessage : "");
        if (enumMatch.find()) {
            String badValue = enumMatch.group(1);
            String allowed = enumMatch.group(2).trim();
            JsonMappingException jme = findCauseInChain(ex, JsonMappingException.class);
            String fieldPath = (jme != null && jme.getPath() != null && !jme.getPath().isEmpty())
                    ? buildFieldPath(jme.getPath())
                    : "request body";
            message = String.format(
                    "Invalid value '%s' at '%s'. Allowed values: %s.", badValue, fieldPath, allowed);
        } else {
            message = "Invalid or malformed JSON in request body.";
        }

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body",
                message,
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(false, null, err));
    }

    /**
     * Handles AuthorizationDeniedException and maps it to a custom error response.
     *
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> authorizationDenied(HttpServletRequest request) {
        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                "User doesn't have permission to access this resource.",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }


    /**
     * Handles RestClientResponseException and maps it to a custom error response.
     *
     * @param exception The RestClientResponseException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> restClientException(RestClientResponseException exception,
                                                                        HttpServletRequest request) {
        LogUtils.error(logger, exception.getMessage(), exception);

        var errorMsg =
                "We are currently experiencing technical difficulties connecting to the external service. Please try again later.";

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                exception.getStatusCode().value(),
                "Service unavailable",
                errorMsg,
                request.getRequestURI());

        return ResponseEntity.status(exception.getStatusCode()).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    /**
     * Handles ResourceNotFoundException and maps it to a custom error response.
     *
     * @param exception The ResourceNotFoundException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> notFound(ResourceNotFoundException exception,
                                                             HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    /**
     * Handles ConversionException and maps it to a custom error response.
     *
     * @param exception The ConversionException instance.
     * @param request   The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(ConversionException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> conversionException(ConversionException exception,
                                                                        HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred while converting data.",
                exception.getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                false,
                null,
                err));

    }

    /**
     * Handles TransactionSystemException and maps it to a custom error response.
     *
     * @param ex      The TransactionSystemException instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler({TransactionSystemException.class})
    protected ResponseEntity<ApiResponseDTO<ErrorDTO>> handlePersistenceException(Exception ex,
                                                                                  HttpServletRequest request) {
        logger.info(ex.getClass().getName());

        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException consEx) {
            final List<String> errors = new ArrayList<>();
            for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            final var err = new ErrorDTO(
                    ZonedDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    "Erro ao salvar dados.",
                    errors.toString(),
                    request.getRequestURI());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponseDTO<>(
                    false,
                    null,
                    err));
        }
        return internalErrorException(ex, request);
    }

    /**
     * Handles any other unexpected exception and maps it to a generic error
     * response.
     *
     * @param e       The unexpected Exception instance.
     * @param request The HttpServletRequest.
     * @return ResponseEntity containing the error response.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> dataIntegrityViolation(DataIntegrityViolationException e,
                                                                           HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Data integrity violation",
                e.getMostSpecificCause().getMessage(),
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<ErrorDTO>> internalErrorException(Exception e, HttpServletRequest request) {

        var err = new ErrorDTO(
                ZonedDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected problem occurred.",
                e.getMessage(),
                request.getRequestURI());

        logger.error("An unexpected problem occurred. ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                false,
                null,
                err));
    }

    private static String buildFieldPath(List<JsonMappingException.Reference> path) {
        StringBuilder sb = new StringBuilder();
        for (JsonMappingException.Reference ref : path) {
            if (ref.getFieldName() != null) {
                if (!sb.isEmpty()) sb.append('.');
                sb.append(ref.getFieldName());
            }
            if (ref.getIndex() >= 0) {
                sb.append('[').append(ref.getIndex()).append(']');
            }
        }
        return !sb.isEmpty() ? sb.toString() : "request body";
    }

    private static <T extends Throwable> T findCauseInChain(Throwable ex, Class<T> type) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (type.isInstance(t)) return type.cast(t);
        }
        return null;
    }
}

