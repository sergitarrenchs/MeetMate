package com.lasalle.meet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.userexceptions.UserEmailExistException;
import com.lasalle.meet.exceptions.userexceptions.UserEmailNullException;
import com.lasalle.meet.exceptions.userexceptions.UserException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordLowSecurityException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNotEqualException;
import com.lasalle.meet.exceptions.userexceptions.UserPasswordNullException;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SignupScreen extends AppCompatActivity{
    private MaterialButton LoginOptButton;
    private MaterialButton SignUpButton;
    private MaterialButton uploadButton;

    private TextInputEditText nameSignUpText;
    private TextInputEditText surnameSignUpText;
    private TextInputEditText emailSignUpText;
    private TextInputEditText passwordSignUpText;
    private TextInputEditText passwordRepeatedSignUpText;

    private TextInputLayout nameSignUpLayout;
    private TextInputLayout surnameSignUpLayout;
    private TextInputLayout emailSignUpLayout;
    private TextInputLayout passwordSignUpLayout;
    private TextInputLayout passwordRepeatedSignUpLayout;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private ImageView imageSelected;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        imageSelected = (ImageView) findViewById(R.id.profilePicture);

        SignUpButton = (MaterialButton) findViewById(R.id.signup_button);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        LoginOptButton = (MaterialButton) findViewById(R.id.login_button);

        LoginOptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSignUpLayout.setErrorEnabled(false);
                surnameSignUpLayout.setErrorEnabled(false);
                emailSignUpLayout.setErrorEnabled(false);
                passwordSignUpLayout.setErrorEnabled(false);
                passwordRepeatedSignUpLayout.setErrorEnabled(false);
                Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                startActivity(intent);
            }
        });

        nameSignUpText = (TextInputEditText) findViewById(R.id.name_signup);
        surnameSignUpText = (TextInputEditText) findViewById(R.id.lastname_signup);
        emailSignUpText = (TextInputEditText) findViewById(R.id.username_login3);
        passwordSignUpText = (TextInputEditText) findViewById(R.id.password_editText2);
        passwordRepeatedSignUpText = (TextInputEditText) findViewById(R.id.password_editText);

        nameSignUpLayout = (TextInputLayout) findViewById(R.id.name_signup_layout);
        surnameSignUpLayout = (TextInputLayout) findViewById(R.id.lastname_signup_layout);
        emailSignUpLayout = (TextInputLayout) findViewById(R.id.username_login3_layout);
        passwordSignUpLayout = (TextInputLayout) findViewById(R.id.password_editText2_layout);
        passwordRepeatedSignUpLayout = (TextInputLayout) findViewById(R.id.password_editText_layout);

        nameSignUpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                nameSignUpLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        surnameSignUpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                surnameSignUpLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailSignUpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                emailSignUpLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordSignUpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordSignUpLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordRepeatedSignUpText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                passwordRepeatedSignUpLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        uploadButton = (MaterialButton) findViewById(R.id.upload_profile_pic_button);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new FragmentPermissionHelper().startPermissionRequest(SignupScreen.this, isGranted -> {
//                    if (isGranted) {
//                        // Permission is granted. Continue the action or workflow in your
//                        // app.
//                    } else {
//                        // Explain to the user that the feature is unavailable because the
//                        // features requires a permission that the user has denied. At the
//                        // same time, respect the user's decision. Don't link to system
//                        // settings in an effort to convince the user to change their
//                        // decision.
//                    }
//                }, Manifest.permission_group.STORAGE);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            SignupScreen.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });
    }

    private void selectImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    try {

                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        imageSelected.setImageBitmap(bitmap);

                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void createUser() {
        User user = new User();

        Intent intent = new Intent(SignupScreen.this, HomeScreen.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();

        //Get bitmap from the selected image
        Bitmap bitmap = ((BitmapDrawable) imageSelected.getDrawable()).getBitmap();

        //Compress into a less weight var
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        //Convert into base64 string
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

        try {
            user.signUpUser(emailSignUpText.getText().toString(),passwordSignUpText.getText().toString(),passwordRepeatedSignUpText.getText().toString(),
                    nameSignUpText.getText().toString(),surnameSignUpText.getText().toString(), imageString);
        } catch (UserEmailNullException e) {
            emailSignUpLayout.setError("The email is empty");
        } catch (UserPasswordNullException e) {
            passwordSignUpLayout.setError("The password is empty");
        } catch (UserPasswordNotEqualException e){
            passwordRepeatedSignUpLayout.setError("The passwords do not coincide");
        } catch (UserPasswordLowSecurityException e){
            passwordSignUpLayout.setError("The password has to be minimum of 8 characters long");
        } catch (UserEmailExistException e){
            emailSignUpLayout.setError("The email already exist");
        } catch (UserException ignored){}
    }
}
