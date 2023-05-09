package uk.ac.le.co2103.part2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateProductActivity extends AppCompatActivity {
    private Product product;
    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinner;
    private int productPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        // Get the product from the intent
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        productPosition = intent.getIntExtra("position", -1);

        // Bind the views
        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinner = findViewById(R.id.spinner);
        Button buttonIncrement = findViewById(R.id.buttonIncrement);
        Button buttonDecrement = findViewById(R.id.buttonDecrement);

        // Populate fields with product details
        editTextName.setText(product.getName());
        editTextQuantity.setText(String.valueOf(product.getQuantity()));

        // Add onClickListeners to increment and decrement buttons
        buttonIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementQuantity(view);
            }
        });

        buttonDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrementQuantity(view);
            }
        });
    }

    public void incrementQuantity(View view) {
        // Increment product quantity and update TextView
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        editTextQuantity.setText(String.valueOf(quantity + 1));
    }

    public void decrementQuantity(View view) {
        // Decrement product quantity and update TextView
        int quantity = Integer.parseInt(editTextQuantity.getText().toString());
        if (quantity > 0) {
            editTextQuantity.setText(String.valueOf(quantity - 1));
        }
    }

    public void saveProduct(View view) {
        // Update product with new details
        product.setName(editTextName.getText().toString());
        product.setQuantity(Integer.parseInt(editTextQuantity.getText().toString()));
        product.setUnit(spinner.getSelectedItem().toString());

        // Create an intent and set the result to allow the ShoppingListActivity to receive the updated product
        Intent resultIntent = new Intent();
        resultIntent.putExtra("product", product);
        resultIntent.putExtra("position", productPosition);
        setResult(RESULT_OK, resultIntent);

        // Show a toast and finish the activity, so that it redirects back to the ShoppingListActivity
        Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
        finish();
    }

}
