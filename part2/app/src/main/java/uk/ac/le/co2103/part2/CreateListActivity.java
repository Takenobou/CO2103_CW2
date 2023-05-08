package uk.ac.le.co2103.part2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class CreateListActivity extends AppCompatActivity {

    public static final String EXTRA_SHOPPING_LIST = "uk.ac.le.co2103.part2.EXTRA_SHOPPING_LIST";
    private static final int PICK_IMAGE_REQUEST = 2;

    private EditText editTextListName;
    private Button buttonPickImage;
    private ImageView imageView;
    private Button buttonCreateList;

    private Uri pickedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        editTextListName = findViewById(R.id.editText_list_name);
        buttonPickImage = findViewById(R.id.button_pick_image);
        imageView = findViewById(R.id.imageView);
        buttonCreateList = findViewById(R.id.button_create_list);

        buttonPickImage.setOnClickListener(v -> pickImageFromGallery());
        buttonCreateList.setOnClickListener(v -> createShoppingList());
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void createShoppingList() {
        String shoppingListName = editTextListName.getText().toString().trim();

        if (shoppingListName.isEmpty()) {
            Toast.makeText(this, "Please enter a shopping list name", Toast.LENGTH_SHORT).show();
            return;
        }

        String listId = UUID.randomUUID().toString();
        ShoppingList shoppingList = new ShoppingList(listId, shoppingListName, pickedImageUri != null ? pickedImageUri.toString() : null);

        Intent data = new Intent();
        data.putExtra(EXTRA_SHOPPING_LIST, shoppingList);
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            pickedImageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pickedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
