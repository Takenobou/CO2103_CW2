package uk.ac.le.co2103.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class AddShoppingListActivity extends AppCompatActivity {

    public static final String EXTRA_SHOPPING_LIST = "uk.ac.le.co2103.part2.EXTRA_SHOPPING_LIST";

    private EditText editTextShoppingListName;
    private Button buttonSaveShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shopping_list);

        editTextShoppingListName = findViewById(R.id.editText_shopping_list_name);
        buttonSaveShoppingList = findViewById(R.id.button_save_shopping_list);

        buttonSaveShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveShoppingList();
            }
        });
    }

    private void saveShoppingList() {
        String shoppingListName = editTextShoppingListName.getText().toString().trim();

        if (shoppingListName.isEmpty()) {
            Toast.makeText(this, "Please enter a shopping list name", Toast.LENGTH_SHORT).show();
            return;
        }

        String listId = UUID.randomUUID().toString();
        ShoppingList shoppingList = new ShoppingList(listId, shoppingListName, null);

        Intent data = new Intent();
        data.putExtra(EXTRA_SHOPPING_LIST, shoppingList);
        setResult(RESULT_OK, data);
        finish();
    }
}
