package com.github.idobashari.rating_sdk.callbacks;

import com.github.idobashari.rating_sdk.api.ErrorResponse;

public interface RatingCallback<T> {
    void onSuccess(T result);
    void onError(ErrorResponse error);
}