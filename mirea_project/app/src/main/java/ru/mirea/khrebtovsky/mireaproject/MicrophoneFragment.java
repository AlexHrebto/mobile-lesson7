package ru.mirea.khrebtovsky.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MicrophoneFragment extends Fragment {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 102;
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    private ProgressBar soundLevelProgressBar;
    private AudioRecord audioRecord;
    private boolean isRecording = false;

    public MicrophoneFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);
        soundLevelProgressBar = view.findViewById(R.id.soundLevelProgressBar);
        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        stopRecording();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
            audioRecord.startRecording();
            isRecording = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRecording) {
                        updateSoundLevel();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private void updateSoundLevel() {
        short[] buffer = new short[1024];
        int bytesRead = audioRecord.read(buffer, 0, buffer.length);
        if (bytesRead > 0) {
            long sum = 0;
            for (int i = 0; i < bytesRead; i++) {
                sum += buffer[i] * buffer[i];
            }
            final double amplitude = Math.sqrt(sum / bytesRead);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    int progress = (int) (amplitude / Short.MAX_VALUE * 100);
                    soundLevelProgressBar.setProgress(progress);
                }
            });
        }
    }

    private void stopRecording() {
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            }
        }
    }
}
