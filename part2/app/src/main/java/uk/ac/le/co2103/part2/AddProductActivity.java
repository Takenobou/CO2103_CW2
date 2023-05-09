package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductActivity extends AppCompatActivity {
    private ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Get the shopping list id from the intent
        Intent intent = getIntent();
        int listId = intent.getIntExtra("listId", -1);
        shoppingList = findShoppingListById(listId);
    }

    public void addProduct(View view) {
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextQuantity = findViewById(R.id.editTextQuantity);
        Spinner spinner = findViewById(R.id.spinner);

        String productName = editTextName.getText().toString();
        int productQuantity = Integer.parseInt(editTextQuantity.getText().toString());
        String productUnit = spinner.getSelectedItem().toString();

        // Check if product already exists in shopping list
        if (shoppingList != null) {
            for (Product product : shoppingList.getProducts()) {
                if (product.getName().equals(productName)) {
                    Toast.makeText(this, "Product already exists", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Add product to shopping list
            Product newProduct = new Product(productName, productQuantity, productUnit);
            shoppingList.addProduct(newProduct);

            // Navigate back to ShoppingListActivity
            Intent intent = new Intent(this, ShoppingListActivity.class);
            intent.putExtra("listId", shoppingList.getListId());
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error finding shopping list", Toast.LENGTH_SHORT).show();
        }
    }
    private ShoppingList findShoppingListById(int listId) {
        ShoppingApplication application = (ShoppingApplication) getApplication();
        for (ShoppingList list : application.shoppingLists) {
            if (list.getListId() == listId) {
                return list;
            }
        }
        return null;
    }
}