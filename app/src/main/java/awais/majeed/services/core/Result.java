package awais.majeed.services.core;


public interface Result<T> {
    void onSuccess(T data, int requestId);

    void onFailure(String message, int requestId);

    void onError(Throwable throwable, int requestId);
} // Result
