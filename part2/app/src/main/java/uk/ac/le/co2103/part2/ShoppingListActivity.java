package uk.ac.le.co2103.part2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private RecyclerView productRecyclerView;
    private ProductListAdapter productAdapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Intent intent = getIntent();
        int listId = intent.getIntExtra("listId", -1);

        ShoppingList shoppingList = findShoppingListById(listId);
        List<Product> products = shoppingList != null ? shoppingList.getProducts() : new ArrayList<>();

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productAdapter = new ProductListAdapter(products, this);

        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productRecyclerView.setAdapter(productAdapter);

        FloatingActionButton fabAddProduct = findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addProductIntent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
                addProductIntent.putExtra("listId", listId);
                startActivity(addProductIntent);
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Get the updated product and position from the intent
            Product updatedProduct = (Product) data.getSerializableExtra("product");
            int updatedPosition = data.getIntExtra("position", -1);

            // Update the product in the list and notify the adapter
            if (updatedPosition != -1) {
                productAdapter.onProductChangeListener.onProductChange(updatedProduct, updatedPosition);
            }
        }
    }


}