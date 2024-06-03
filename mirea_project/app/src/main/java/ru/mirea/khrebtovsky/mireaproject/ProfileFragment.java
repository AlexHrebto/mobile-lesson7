package ru.mirea.khrebtovsky.mireaproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText editTextFirstName, editTextLastName, editTextAge, editTextEmail, editTextPhone, editTextDescription;
    private Button buttonSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        editTextLastName = view.findViewById(R.id.editTextLastName);
        editTextAge = view.findViewById(R.id.editTextAge);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        buttonSave = view.findViewById(R.id.buttonSave);

        loadProfile();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void loadProfile() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        editTextFirstName.setText(prefs.getString("firstName", ""));
        editTextLastName.setText(prefs.getString("lastName", ""));
        editTextAge.setText(String.valueOf(prefs.getInt("age", 0)));
        editTextEmail.setText(prefs.getString("email", ""));
        editTextPhone.setText(prefs.getString("phone", ""));
        editTextDescription.setText(prefs.getString("description", ""));
    }

    private void saveProfile() {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());
        String email = editTextEmail.getText().toString();
        String phone = editTextPhone.getText().toString();
        String description = editTextDescription.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putInt("age", age);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("description", description);
        editor.apply();

        Toast.makeText(getActivity(), "Профиль успешно сохранён", Toast.LENGTH_SHORT).show();
    }
}
