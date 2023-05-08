package uk.ac.le.co2103.part2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ShoppingListAdapter shoppingListAdapter;
    private ActivityResultLauncher<Intent> createListLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_main);

        final FloatingActionButton button = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingListAdapter = new ShoppingListAdapter(new ArrayList<>());
        recyclerView.setAdapter(shoppingListAdapter);

        createListLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                ShoppingList shoppingList = (ShoppingList) result.getData().getSerializableExtra(CreateListActivity.EXTRA_SHOPPING_LIST);
                shoppingListAdapter.addShoppingList(shoppingList);
            }
        });

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
            createListLauncher.launch(intent);
        });
    }
}
