package com.habitdev.sprout.ui.menu.setting.ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.habitdev.sprout.database.user.UserViewModel;
import com.habitdev.sprout.database.user.model.User;
import com.habitdev.sprout.databinding.FragmentProfileBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public interface OnReturnSetting {
        void returnFromProfileToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        final UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        User user = userViewModel.getUserByUID(1);

        userViewModel.getUserNickname().observe(getViewLifecycleOwner(), nickname -> {
            binding.settingProfileChangeNickname.setText(nickname);
        });

        binding.settingChangeProfilePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(requireActivity())) {
                    chooseImage(requireActivity());
                    Log.d("tag", "onClick: Change Photo");
                }
            }
        });

        binding.settingProfileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update nickname
                if (!binding.settingProfileChangeNickname.getText().toString().trim().isEmpty()) {
                    user.setNickname(binding.settingProfileChangeNickname.getText().toString().trim());
                    userViewModel.update(user);
                } else {
                    Toast.makeText(requireActivity(), "Empty Nickname", Toast.LENGTH_SHORT).show();
                }
            }
        });

        onBackPress();
        return binding.getRoot();
    }

    // function to check permission
    public static boolean checkSelfPermission(final Activity activity) {
        int WriteExternastoragePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CameraPermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        List<String> permmisionRequired = new ArrayList<>();

        if (WriteExternastoragePermission != PackageManager.PERMISSION_GRANTED) {
            permmisionRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (CameraPermission != PackageManager.PERMISSION_GRANTED) {
            permmisionRequired.add(Manifest.permission.CAMERA);
        }

        if (!permmisionRequired.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permmisionRequired.toArray(new String[permmisionRequired.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT).show();
            } else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "FlagUp Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();
            } else {
                chooseImage(requireActivity());
            }
        }
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder.
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        binding.settingProfileImgView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                binding.settingProfileImgView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromProfileToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}