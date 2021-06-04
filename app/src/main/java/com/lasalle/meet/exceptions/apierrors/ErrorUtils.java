package com.lasalle.meet.exceptions.apierrors;

import com.lasalle.meet.MainActivity;
import com.lasalle.meet.apiadapter.APIService;
import com.lasalle.meet.enities.APIAdapter;
import com.lasalle.meet.enities.User;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {

    public static APIError parseError(Response<?> response) {
        Converter<ResponseBody, APIError> converter =
                APIAdapter.getRetrofit().responseBodyConverter(APIError.class, new Annotation[0]);


        APIError error;

        try {
            assert response.errorBody() != null;
            error = converter.convert(response.errorBody());
            return error;
        } catch (IOException e) {
            return new APIError();
        }
    }
}
