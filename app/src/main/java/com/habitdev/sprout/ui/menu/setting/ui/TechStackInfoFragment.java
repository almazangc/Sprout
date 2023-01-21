package com.habitdev.sprout.ui.menu.setting.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.habitdev.sprout.database.note.NoteViewModel;
import com.habitdev.sprout.database.note.model.Note;
import com.habitdev.sprout.databinding.FragmentTechStackInfoBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class TechStackInfoFragment extends Fragment {

    private FragmentTechStackInfoBinding binding;

    private static final String FILE_NAME = "notes.json";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 14;
    private static File file;
    private NoteViewModel noteViewModel;

    public interface OnReturnSetting {
        void returnFromTechStackInfoToSetting();
    }

    private OnReturnSetting mOnReturnSetting;

    public void setmOnReturnSetting(OnReturnSetting mOnReturnSetting) {
        this.mOnReturnSetting = mOnReturnSetting;
    }

    public TechStackInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTechStackInfoBinding.inflate(inflater, container, false);
        noteViewModel = new ViewModelProvider(requireActivity()).get(NoteViewModel.class);
        openRepository();
        sendMail();
        readEULA();
        exportNote();
        onBackPress();
        return binding.getRoot();
    }

    private void exportNote() {
        binding.exportNoteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDatabase();
                openFileWithDialog(file);
            }
        });
    }

    private void exportDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Export Note Data")
                .setMessage("Are you sure you want to export your note data to a .json file?")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        } else {
                            // Continue with creating the directory and file
                            exportData();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void exportData() {
        new ExportDataTask(noteViewModel).execute();
    }

    private class ExportDataTask extends AsyncTask<Void, Void, String> {

        private final NoteViewModel noteViewModel;

        ExportDataTask(NoteViewModel noteViewModel) {
            this.noteViewModel = noteViewModel;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Gson gson = new Gson();
            List<Note> notes = noteViewModel.getNoteList();
            Type type = new TypeToken<List<Note>>() {
            }.getType();
            return gson.toJson(notes, type);
        }

        @Override
        protected void onPostExecute(String jsonString) {
            file = createFile(FILE_NAME, jsonString);
            openFileWithDialog(file);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, continue with creating the directory and file
                    exportData();
                } else {
                    // Permission denied, show an explanation to the user
                    Toast.makeText(getContext(), "Permission denied, unable to export data", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    private File createFile(String fileName, String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            Log.e("tag", "jsonString is empty or null");
            return null;
        }

        File appDir = requireActivity().getExternalFilesDir(null);
        File file;
        if (appDir != null) {
            File dir = new File(appDir, "notes");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, fileName);
        } else {
            appDir = getContext().getFilesDir();
            file = new File(appDir, fileName);
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(jsonString.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void openFileWithDialog(File file) {
        if (file == null) {
            Log.d("tag", "file is null");
            return;
        }

        if (!file.exists()) {
            Log.d("tag", "file does not exist");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(getContext(), "com.habitdev.sprout", file);
        intent.setDataAndType(uri, "text/plain");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    public void openRepository() {
        binding.githubRepository.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGitHubURL("https://github.com/DOMO-Dom/Sprout");
            }
        });
    }

    public void sendMail() {
        binding.sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:sproutdev.technology@gmail.com"));
                startActivity(intent);
            }
        });
    }

    private void readEULA() {
        binding.readEula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGitHubURL("https://github.com/DOMO-Dom/Sprout/blob/main/app/src/main/assets/sprout_generated_eula.md");
            }
        });
    }

    /**
     * Open the link provided on Github Application if installed,
     * Otherwise opens in default browser
     *
     * @param url link of the site
     */
    private void openGitHubURL(String url) {
        Intent githubIntent = requireActivity().getPackageManager().getLaunchIntentForPackage("com.github.android");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resolveInfoList = requireActivity().getPackageManager().queryIntentActivities(browserIntent, 0);
        if (githubIntent != null) {
            // Create a chooser dialog to allow user to select between GitHub app and browser
            Intent chooserIntent = Intent.createChooser(githubIntent, "Open with");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, resolveInfoList.toArray(new Parcelable[0]));
            startActivity(chooserIntent);
        } else {
            startActivity(browserIntent);
        }
    }


    private void onBackPress() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mOnReturnSetting != null) mOnReturnSetting.returnFromTechStackInfoToSetting();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}