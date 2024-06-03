package ru.mirea.khrebtovsky.mireaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CameraFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private LinearLayout photoLayout;
    private List<Bitmap> photoList;
    private List<String> captionList;

    public CameraFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        photoLayout = view.findViewById(R.id.photoLayout);
        Button takePhotoButton = view.findViewById(R.id.takePhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        photoList = new ArrayList<>();
        captionList = new ArrayList<>();
        return view;
    }

    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String caption = "Caption";
            addPhotoWithCaption(imageBitmap, caption);
        }
    }

    private void addPhotoWithCaption(Bitmap imageBitmap, String caption) {
        View photoView = LayoutInflater.from(requireContext()).inflate(R.layout.item_photo_with_caption, null);
        ImageView imageView = photoView.findViewById(R.id.imageView);
        EditText captionEditText = photoView.findViewById(R.id.captionEditText);
        Button deleteButton = photoView.findViewById(R.id.deleteButton);

        imageView.setImageBitmap(imageBitmap);
        captionEditText.setText(caption);

        photoLayout.addView(photoView);

        photoList.add(imageBitmap);
        captionList.add(caption);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = photoLayout.indexOfChild(photoView);
                photoLayout.removeViewAt(index);
                photoList.remove(index);
                captionList.remove(index);
            }
        });
    }
}

