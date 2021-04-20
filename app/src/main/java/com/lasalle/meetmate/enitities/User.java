package com.lasalle.meetmate.enitities;

import android.graphics.Picture;

import androidx.annotation.NonNull;

import com.lasalle.meetmate.exceptions.UserException;
import com.lasalle.meetmate.exceptions.UserIncorrectPasswordException;
import com.lasalle.meetmate.exceptions.UserNotFoundException;

public class User {
    String userEmail;
    String userPassword;
    String userUsername;
    String userFullName;
    Picture userPicture;

    private User(String userEmail, String userPassword, String userFullName, String userUsername) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userFullName = userFullName;
        this.userUsername = "@" + userUsername.toLowerCase();
    }

    public static User singUp(String userEmail, String userPassword, String userFullName, String userUsername){
        return new User(userEmail, userPassword, userFullName, userUsername);
    }

    public void logIn(String inputUsername, String inputPassword) throws UserException {

        if (inputUsername.equals("root") && inputPassword.equals("toor")/*TODO: connect to api to check userName*/) {
            System.out.println("Correct user");
        }else if(inputUsername.equals("root")){  //TODO: User needs to exist
            throw new UserIncorrectPasswordException();
        }else{
            throw new UserNotFoundException();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
