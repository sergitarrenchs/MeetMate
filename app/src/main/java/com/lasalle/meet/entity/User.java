package com.lasalle.meet.entity;

import com.lasalle.meet.encrypt.AES256;
import com.lasalle.meet.exception.UserException;
import com.lasalle.meet.exception.UserPasswordIncorrectException;
import com.lasalle.meet.exception.UserPasswordLowSecurityException;
import com.lasalle.meet.exception.UserPasswordNotEqualException;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String email;
    private String password;

    private User(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    // FIXME: 21/4/2021 Temporal
    private User logInUser(String username, String email, String password) {
        return new User(username, email, password);
    }

    /*public static User logInUser(){
        //TODO: connect to database, search for user, return a user
        //return new User();
        return null;
    }*/

    public User signUpUser(String username, String email, String password, String repeatedPassword) throws UserException {
        //TODO: check username exists,compare password and repeatedPassword, if not equal, show error, else add user to database

        //username.check();
        /*if(true){
            throw new UserUsernameExistException();
            //email.check()
        }else if () {
            throw new UserEmailExistsException();

        }else*/
        if (!password.equals(repeatedPassword)) {
            throw new UserPasswordNotEqualException();

        } else if (password.length() < 8) {
            throw new UserPasswordLowSecurityException();
        } else {
            // FIXME: 21/4/2021 needs to be connected instead of creating a new User
            new User(username, email, password);
            return logInUser(username, email, password);
        }
    }

    public void updatePassword(String password, String repeatedPassword, String newPassword) throws UserException {
        if (!password.equals(repeatedPassword)) {
            throw new UserPasswordNotEqualException();
        } else if (!this.password.equals(AES256.encrypt(password))) {
            throw new UserPasswordIncorrectException();
        } else if (newPassword.length() < 8) {
            throw new UserPasswordLowSecurityException();
        } else {
            //updatePassword()
            this.password = AES256.encrypt(newPassword);
        }
    }

    public void updateUserInfo(String username, String email, String password) throws UserPasswordIncorrectException {
        if (this.password.equals(AES256.encrypt(password))) {
            if (username != null) {
                this.username = username;
            }

            if (email != null) {
                this.email = email;
            }
        } else {
            throw new UserPasswordIncorrectException();
        }

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


}