package com.lasalle.meet.enities;

import android.content.Intent;

import com.google.android.gms.common.api.PendingResult;
import com.google.gson.annotations.Expose;
import com.lasalle.meet.exceptions.apierrors.APIError;
import com.lasalle.meet.exceptions.apierrors.ErrorUtils;
import com.lasalle.meet.exceptions.messageexceptions.MessageNullException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserIncorrectCredentialsException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;
import com.lasalle.meet.exceptions.userexceptions.UserUnableDeletionException;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    @Expose(serialize = false, deserialize = true)
    private int id;

    @Expose(serialize = false, deserialize = false)
    private int userError;

    @Expose(serialize = false, deserialize = false)
    private List<User> friends;

    @Expose(serialize = false, deserialize = true)
    private String accessToken;

    private final static int NO_ERROR = 0;
    private final static int USER_NOT_FOUND = 404;
    private final static int EMAIL_EXIST = 1062;
    private final static int UNAUTHORISED = 401;
    private final static int BAD_REQUEST = 400;

    public User(String name, String last_name, String email, String password, String image) {
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

            userError = NO_ERROR;

            Call<User> call = APIAdapter.getApiService().postCreateUser(this);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(!response.isSuccessful()) {
                        APIError apiError = ErrorUtils.parseError(response);

                        userError = apiError.getStackTrace().getErrno();

                        return;
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }

            });

            switch (userError){
                case EMAIL_EXIST:
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

    public void logInUser(String email, String password) throws UserException {
        if (email.equals("")) {
            throw new UserEmailNullException();
        } else if (password.equals("")) {
            throw new UserPasswordNullException();
        }else if (password.length() < 8){
            throw new UserPasswordLowSecurityException();
        } else{
            userError = NO_ERROR;

            CountDownLatch countDownLatch = new CountDownLatch(1);

            this.email = email;
            this.password = password;

            Call<User> call = APIAdapter.getApiService().postLoginUser(User.this);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(!response.isSuccessful()) {
                        accessToken = null;
                        userError = USER_NOT_FOUND;

                    }else {
                        assert response.body() != null;
                        accessToken = response.body().accessToken;
                    }
                    countDownLatch.countDown();

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    countDownLatch.countDown();

                }
            });

            try {
                countDownLatch.await();

                if (userError == USER_NOT_FOUND) {
                    throw new UserIncorrectCredentialsException();
                }

                CountDownLatch countDownLatch2 = new CountDownLatch(1);

                Call<List<User>> userCall = APIAdapter.getApiService().searchUser(this.email, "Bearer " + this.accessToken);

                userCall.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (!response.isSuccessful()){
                            countDownLatch2.countDown();
                            return;
                        }

                        List<User> users = response.body();
                        assert users != null;
                        User.this.id = users.get(0).id;

                        User.this.logInUser(User.this.email, User.this.password, users.get(0).name, users.get(0).last_name, users.get(0).image);
                        countDownLatch2.countDown();
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        countDownLatch2.countDown();
                    }
                });

                countDownLatch2.await();

            } catch (InterruptedException e) {
                /*
                If we are cannot wait due to an error, we need to parse again
                 */
                throw new UserIncorrectCredentialsException();
            }

        }
    }

    public void logOutUser() {
        name = null;
        last_name = null;
        email = null;
        password = null;
        image = null;
        username = null;
        id = 0;
        accessToken = null;
    }

    public void deleteUser() throws UserUnableDeletionException {
        //accessToken
        Call<User> call = APIAdapter.getApiService().deleteUser("Bearer " + this.accessToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    userError = response.code();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        if (userError == UNAUTHORISED) {
            throw new UserUnableDeletionException();
        }

        logOutUser();

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return name + " " + last_name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getId() {
        return id;
    }

    public void update(@NotNull String name, @NotNull String last_name, String email, String password) throws UserException {

        if (email.equals("")) {
            throw new UserEmailNullException();
        } else if (password.equals("")) {
            throw new UserPasswordNullException();
        } else if (password.length() < 8){
            throw new UserPasswordLowSecurityException();
        } else if (!this.email.equals(email)) {

            CountDownLatch countDownLatch2 = new CountDownLatch(1);

            Call<List<User>> userCall = APIAdapter.getApiService().searchUser(this.email, "Bearer " + this.accessToken);

            userCall.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        countDownLatch2.countDown();
                        return;
                    }

                    userError = EMAIL_EXIST;
                    countDownLatch2.countDown();
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    countDownLatch2.countDown();
                }
            });

            try {
                countDownLatch2.await();

                if (userError == EMAIL_EXIST) {
                    throw new UserEmailExistException();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.username = "@" + name.toLowerCase().replaceAll(" ", "") + "." + last_name.toLowerCase().replaceAll(" ", "");

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<User> call = APIAdapter.getApiService().modifyUser(this, "Bearer " + this.accessToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    accessToken = null;
                    userError = USER_NOT_FOUND;

                } else {
                    assert response.body() != null;
                    accessToken = response.body().accessToken;
                }
                countDownLatch.countDown();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();

            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }

    }

    public List<User> getFriends() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        Call<List<User>> call = APIAdapter.getApiService().getFriends("Bearer " + this.accessToken);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                friends = response.body();
                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            friends = new ArrayList<User>();
        }

        return friends;
    }

    public void sendMessage(int otherId, String text) throws MessageNullException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        if (text.equals("")) {
            throw new MessageNullException();
        }else {
            Message messageToSend = new Message(text, id,otherId);

            Call<Message> call = APIAdapter.getApiService().postMessage(messageToSend,"Bearer " + this.accessToken);

            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    countDownLatch.countDown();
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    countDownLatch.countDown();
                }
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                throw new MessageNullException();
            }
        }
    }

    public void addFriend(User user) throws UserIncorrectCredentialsException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        userError = NO_ERROR;

        Call<User> call = APIAdapter.getApiService().postFriendRequest(user.getId(),"Bearer " + this.accessToken);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    userError = response.code();
                }

                countDownLatch.countDown();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();

            if (userError == BAD_REQUEST) {
                throw new UserIncorrectCredentialsException();
            }

        } catch (InterruptedException e) {
            throw new UserIncorrectCredentialsException();
        }
    }
}
