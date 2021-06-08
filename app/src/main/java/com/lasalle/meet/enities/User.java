package com.lasalle.meet.enities;

import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lasalle.meet.apiadapter.APIService;
import com.lasalle.meet.exceptions.apierrors.APIError;
import com.lasalle.meet.exceptions.apierrors.ErrorUtils;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User implements Serializable {
    @Expose(serialize = true, deserialize = true)
    private String name;
    @Expose(serialize = true, deserialize = true)
    private String last_name;
    @Expose(serialize = true, deserialize = true)
    private String email;
    @Expose(serialize = true, deserialize = true)
    private String password;
    @Expose(serialize = true, deserialize = true)
    private String image;
    @Expose(serialize = false, deserialize = false)
    private String username;

    @Expose(serialize = false, deserialize = false)
    private int userError;

    private User(String name, String last_name, String email, String password, String image) {
        this.name = name;
        this.last_name = last_name;
        this.username = "@" + name.toLowerCase().replaceAll(" ","") + "." + last_name.toLowerCase().replaceAll(" ","");
        this.email = email;
        this.password = password;
        this.image = image;
    }

    public User() {
    }

    public void signUpUser(String email, String password, String repeatedPassword, String name, String last_name, String image) throws UserException {

        if (email.equals("")) {
            throw new UserEmailNullException();
        } else if (password.equals("")) {
            throw new UserPasswordNullException();
        }else if (!password.equals(repeatedPassword)) {
            throw new UserPasswordNotEqualException();

        } else if (password.length() < 8){
            throw new UserPasswordLowSecurityException();
        } else{

            logInUser(email, password, name, last_name, image);

            userError = 0;

            Call<User> call = APIAdapter.getApiService().postCreateUser(this);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(!response.isSuccessful()) {
                        APIError apiError = ErrorUtils.parseError(response);

                        userError = apiError.getStackTrace().getErrno();

                        return;
                    }

                    System.out.println(response.message());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }

            });

            switch (userError){
                case 1062:
                    throw new UserEmailExistException();
            }

        }
    }

    private void logInUser(String email, String password, String name, String last_name, String image) {
        this.username = "@" + name.toLowerCase() + "." + last_name.toLowerCase();
        this.email = email;
        this.password = password;
        this.name = name;
        this.last_name = last_name;
        this.image = image;
    }
}