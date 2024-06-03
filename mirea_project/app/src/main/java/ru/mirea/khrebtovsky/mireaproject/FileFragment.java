package ru.mirea.khrebtovsky.mireaproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class FileFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private List<String> fileNames;
    private FileListAdapter adapter;

    public FileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        fileNames = new ArrayList<>();
        ListView fileList = view.findViewById(R.id.file_list);
        adapter = new FileListAdapter(requireContext(), new ArrayList<FileItem>());
        fileList.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }

        view.findViewById(R.id.fab_create_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateFileDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFiles();
    }

    private void showCreateFileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create File");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_file, null);
        final EditText fileNameInput = dialogView.findViewById(R.id.file_name_input);
        final EditText fileContentInput = dialogView.findViewById(R.id.file_content_input);
        builder.setView(dialogView);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fileName = fileNameInput.getText().toString();
                String fileContent = fileContentInput.getText().toString();
                if (!TextUtils.isEmpty(fileName)) {
                    createFile(fileName, fileContent);
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid file name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void createFile(String fileName, String fileContent) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        try {
            if (file.createNewFile()) {
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(fileContent.getBytes());
                outputStream.close();
                Toast.makeText(requireContext(), "File created: " + fileName, Toast.LENGTH_SHORT).show();
                fileNames.add(fileName);
                adapter.notifyDataSetChanged();

                String hash = hashFileContent(fileContent);
                Toast.makeText(requireContext(), "File content hash: " + hash, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "File already exists", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error creating file", Toast.LENGTH_SHORT).show();
        }
    }

    private String hashFileContent(String fileContent) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fileContent.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void loadFiles() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File[] files = path.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileContent = FileUtils.readFileContent(file);
                    String fileHash = hashFileContent(fileContent);
                    adapter.add(new FileItem(file.getName(), fileHash));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadFiles();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot access files", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
