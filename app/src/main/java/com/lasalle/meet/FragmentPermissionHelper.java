package com.lasalle.meet;

import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class FragmentPermissionHelper {
    public void startPermissionRequest(AppCompatActivity appCompatActivity, FragmentPermissionInterface fragmentPermissionInterface, String manifest) {
        ActivityResultLauncher<String> requestPermissionLauncher =
                appCompatActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), fragmentPermissionInterface::onGranted);
        requestPermissionLauncher.launch(
                manifest);

    }
}
