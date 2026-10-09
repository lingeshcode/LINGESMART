package com.lingesh.lingeshmart.dto;

public class ApiResponseDTO<T> {

    private boolean success;
    private T data;
    private ApiError error;

    public static class ApiError {
        private String code;
        private String message;

        public ApiError() {}

        public ApiError(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public ApiResponseDTO() {}

    public static <T> ApiResponseDTO<T> success(T data) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(true);
        response.setData(data);
        response.setError(null);
        return response;
    }

    public static <T> ApiResponseDTO<T> error(String code, String message) {
        ApiResponseDTO<T> response = new ApiResponseDTO<>();
        response.setSuccess(false);
        response.setData(null);
        response.setError(new ApiError(code, message));
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
