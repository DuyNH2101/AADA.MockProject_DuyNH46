package com.example.aadamockproject_duynh46.presentation.profile;

import static android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import com.example.aadamockproject_duynh46.R;
import com.example.aadamockproject_duynh46.databinding.ActivityUserInformationBinding;
import com.example.aadamockproject_duynh46.domain.model.UserModel;
import com.example.aadamockproject_duynh46.framework.utils.ImageUtils;
import com.example.aadamockproject_duynh46.presentation.main.MainActivity;

import java.io.IOException;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserInformationActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_PERMISSION_CODE = 101;
    ActivityResultLauncher<Intent> cameraLauncher;
    ActivityUserInformationBinding binding;
    ActivityResultLauncher<Intent> imagePickerLauncher;

    private UserModel user = new UserModel("", "Duy Hoang", "hoangduy@gmail.com", "21/01/2004", "Male");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        profileViewModel.getMutableLiveDataUserModel().observe(this, userModel -> {
            if(userModel != null){
                user = userModel;
                binding.setUserModel(user);
                switch (user.getGender()){
                    case "Male":{
                        binding.userEditGenderRadioGroup.check(R.id.user_edit_radio_male);
                        break;
                    }
                    case "Female":{
                        binding.userEditGenderRadioGroup.check(R.id.user_edit_radio_female);
                        break;
                    }
                }
            }
        });
        binding.setUserModel(user);
        profileViewModel.getUser();
        initializeLaunchers();


        switch (user.getGender()){
            case "Male":{
                binding.userEditGenderRadioGroup.check(R.id.user_edit_radio_male);
                break;
            }
            case "Female":{
                binding.userEditGenderRadioGroup.check(R.id.user_edit_radio_female);
                break;
            }
        }

        binding.userEditImage.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, binding.userEditImage);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item->{
                int itemId = item.getItemId();
                if(itemId==R.id.camera_choice){
                    checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                    return true;
                } else if(itemId==R.id.gallery_choice){
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_PERMISSION_CODE);
                    } else {
                        checkPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, GALLERY_PERMISSION_CODE);
                    }
                    return true;
                } else{
                    return false;
                }
            });
            popupMenu.show();
        });


        binding.userEditCancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        binding.userEditDoneBtn.setOnClickListener(v->{
            user.setFullName(binding.userEditNameEditText.getText().toString().trim());
            user.setEmail(binding.userEditEmailEditText.getText().toString().trim());
            user.setBirthday(binding.userEditBirthdayEditText.getText().toString().trim());
            BitmapDrawable drawable = (BitmapDrawable) binding.userEditImage.getDrawable();
            user.setImage(ImageUtils.bitmapToBase64(drawable.getBitmap()));

            int selectedId = binding.userEditGenderRadioGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.user_edit_radio_male) {
                user.setGender("Male");
            } else if (selectedId == R.id.user_edit_radio_female) {
                user.setGender("Female");
            }

            profileViewModel.saveUser(user);


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    public void initializeLaunchers(){
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResultCallback -> {
                    if (activityResultCallback.getResultCode() == RESULT_OK) {
                        Intent intentResult = activityResultCallback.getData();
                        if (intentResult != null) {
                            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(intentResult.getExtras()).get("data");
                            assert imageBitmap != null;
                            String imageAsString = ImageUtils.bitmapToBase64(imageBitmap);
                            user.setImage(imageAsString);
                            binding.userEditImage.setImageBitmap(imageBitmap);
                        }
                    } else {
                        Toast.makeText(this, "There is something wrong with the camera", Toast.LENGTH_SHORT).show();
                    }
                });
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResultCallback -> {
                    if (activityResultCallback.getResultCode() == RESULT_OK && activityResultCallback.getData() != null) {
                        Uri selectedImageUri = activityResultCallback.getData().getData();
                        Bitmap imageBitmap;
                        try {
                            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if(imageBitmap!=null){
                            String imageAsString = ImageUtils.bitmapToBase64(imageBitmap);
                            user.setImage(imageAsString);
                            binding.userEditImage.setImageBitmap(imageBitmap);
                        }
                    } else{
                        Toast.makeText(this, "Launcher Failed", Toast.LENGTH_SHORT);
                    }
                });
    }
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            } else if(requestCode == GALLERY_PERMISSION_CODE){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(EXTERNAL_CONTENT_URI, "image/*");
                imagePickerLauncher.launch(intent);
            }
        }
    }

}