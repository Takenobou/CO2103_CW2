package uk.ac.le.co2103.part2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private List<ShoppingList> shoppingLists;
    private Context context;

    public ShoppingListAdapter(List<ShoppingList> shoppingLists, Context context) {
        this.shoppingLists = shoppingLists;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingList shoppingList = shoppingLists.get(position);
        holder.shoppingListName.setText(shoppingList.getName());
        if (shoppingList.getImageUri() != null && !shoppingList.getImageUri().isEmpty()) {
            holder.shoppingListImage.setImageURI(Uri.parse(shoppingList.getImageUri()));
        } else {
            holder.shoppingListImage.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShoppingListActivity.class);
                intent.putExtra("listId", shoppingList.getListId());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Shopping List")
                        .setMessage("Are you sure you want to delete this shopping list?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the shopping list and notify adapter
                                shoppingLists.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Shopping list deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true; // returning true instead of false, it's because we're done with the event and we don't want other listeners to handle it
            }
        });
    }


    @Override
    public int getItemCount() {
        return shoppingLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView shoppingListName;
        public ImageView shoppingListImage;

        public ViewHolder(View itemView) {
            super(itemView);
            shoppingListName = itemView.findViewById(R.id.shoppingListName);
            shoppingListImage = itemView.findViewById(R.id.shoppingListImage);
        }
    }
}
