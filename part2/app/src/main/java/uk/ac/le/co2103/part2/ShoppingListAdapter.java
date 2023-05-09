package uk.ac.le.co2103.part2;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {

    private List<ShoppingList> shoppingLists;

    public ShoppingListAdapter(List<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ShoppingListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        holder.shoppingListNameTextView.setText(shoppingList.getName());
        if (shoppingList.getImage() != null) {
            Uri imageUri = Uri.parse(shoppingList.getImage());
            holder.shoppingListImageView.setImageURI(imageUri);
        }
    }

    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public void addShoppingList(ShoppingList shoppingList) {
        shoppingLists.add(shoppingList);
        notifyItemInserted(shoppingLists.size() - 1);
    }

    static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        private TextView shoppingListNameTextView;
        private ImageView shoppingListImageView;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingListNameTextView = itemView.findViewById(R.id.textView);
            shoppingListImageView = itemView.findViewById(R.id.imageView);
        }
    }
}

