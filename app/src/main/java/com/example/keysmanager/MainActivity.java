package com.example.keysmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(this);

        // Кнопка добавления нового пароля
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddEditActivity.class));
        });

        // Обработчик клика по элементу списка
        listView.setOnItemClickListener((parent, view, position, id) -> {
            PasswordEntry entry = (PasswordEntry) parent.getItemAtPosition(position);
            Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
            intent.putExtra("id", entry.getId());
            startActivity(intent);
        });

        loadPasswords();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords();
    }

    private void loadPasswords() {
        List<PasswordEntry> entries = dbHelper.getAllPasswords();
        if (entries.size() > 0) {
            ArrayAdapter<PasswordEntry> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_list_item_1, entries);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No passwords saved yet", Toast.LENGTH_SHORT).show();
        }
    }
}