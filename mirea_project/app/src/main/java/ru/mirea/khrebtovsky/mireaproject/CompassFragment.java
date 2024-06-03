package ru.mirea.khrebtovsky.mireaproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class CompassFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private TextView directionTextView;

    public CompassFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        directionTextView = view.findViewById(R.id.directionTextView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensor != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float azimuth = event.values[0];
        String direction;
        if (azimuth >= 345 || azimuth < 15) {
            direction = "Север";
        } else if (azimuth >= 15 && azimuth < 75) {
            direction = "Северо-Восток";
        } else if (azimuth >= 75 && azimuth < 105) {
            direction = "Восток";
        } else if (azimuth >= 105 && azimuth < 165) {
            direction = "Юго-Восток";
        } else if (azimuth >= 165 && azimuth < 195) {
            direction = "Юг";
        } else if (azimuth >= 195 && azimuth < 255) {
            direction = "Юго-Запад";
        } else if (azimuth >= 255 && azimuth < 285) {
            direction = "Запад";
        } else if (azimuth >= 285 && azimuth < 345) {
            direction = "Северо-Запад";
        } else {
            direction = "Неизвестно";
        }
        directionTextView.setText("Азимут" + azimuth + "\nНаправление: " + direction);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
