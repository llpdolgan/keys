package com.example.keysmanager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {
    private EditText etTitle, etUsername, etPassword, etNotes;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int passwordId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Инициализация элементов интерфейса
        etTitle = findViewById(R.id.etTitle);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etNotes = findViewById(R.id.etNotes);
        btnSave = findViewById(R.id.btnSave);

        dbHelper = new DatabaseHelper(this);

        // Проверяем, редактируем ли существующий пароль
        if (getIntent().hasExtra("id")) {
            passwordId = getIntent().getIntExtra("id", -1);
            PasswordEntry entry = dbHelper.getPasswordById(passwordId);
            if (entry != null) {
                etTitle.setText(entry.getTitle());
                etUsername.setText(entry.getUsername());
                try {
                    etPassword.setText(EncryptionUtils.decrypt(entry.getEncryptedPassword()));
                } catch (Exception e) {
                    Toast.makeText(this, "Error decrypting password", Toast.LENGTH_SHORT).show();
                }
                etNotes.setText(entry.getNotes());
            }
        }

        // Обработчик кнопки сохранения
        btnSave.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String title = etTitle.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }

        try {
            String encryptedPassword = EncryptionUtils.encrypt(password);
            PasswordEntry entry = new PasswordEntry(title, username, encryptedPassword, notes);

            if (passwordId == -1) {
                // Добавление нового пароля
                long result = dbHelper.addPassword(entry);
                if (result != -1) {
                    Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error saving password", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Обновление существующего пароля
                entry.setId(passwordId);
                int result = dbHelper.updatePassword(entry);
                if (result > 0) {
                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Encryption error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

