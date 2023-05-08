package uk.ac.le.co2103.part2;

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
    private static final int ADD_SHOPPING_LIST_REQUEST = 1;

    private RecyclerView recyclerView;
    private ShoppingListAdapter shoppingListAdapter;

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

        button.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddShoppingListActivity.class);
            startActivityForResult(intent, ADD_SHOPPING_LIST_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_SHOPPING_LIST_REQUEST && resultCode == RESULT_OK) {
            ShoppingList shoppingList = (ShoppingList) data.getSerializableExtra(AddShoppingListActivity.EXTRA_SHOPPING_LIST);
            shoppingListAdapter.addShoppingList(shoppingList);
        }
    }
}
