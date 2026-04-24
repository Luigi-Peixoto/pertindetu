package com.ufrn.pertindetu.base.dto;

/**
 * Generic DTO used to standardize API responses.
 *
 * @param <T> the type of the response data and error
 */
public class ApiResponseDTO<T> {
    private boolean success;
    private T data;
    private T error;

    /**
     * Constructs an ApiResponseDTO with the specified values.
     *
     * @param success indicates whether the API call was successful
     * @param data the response data, if the call was successful
     * @param error the error details, if the call was not successful
     */
    public ApiResponseDTO(boolean success, T data, T error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    /**
     * Default constructor.
     */
    public ApiResponseDTO() {
    }

    /**
     * Returns whether the API call was successful.
     *
     * @return true if the call was successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets whether the API call was successful.
     *
     * @param success true if the call was successful, false otherwise
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the response data.
     *
     * @return the response data of type T
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the response data.
     *
     * @param data the response data of type T
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     * Returns the error details.
     *
     * @return the error details of type T
     */
    public T getError() {
        return error;
    }

    /**
     * Sets the error details.
     *
     * @param error the error details of type T
     */
    public void setError(T error) {
        this.error = error;
    }
}
