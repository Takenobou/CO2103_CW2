package uk.ac.le.co2103.part2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Product> products;
    private Context context;

    public void setOnProductChangeListener(OnProductChangeListener onProductChangeListener) {
        this.onProductChangeListener = onProductChangeListener;
    }


    // Interface for updating product
    public interface OnProductChangeListener {
        void onProductChange(Product product, int position);
    }

    public OnProductChangeListener onProductChangeListener = new OnProductChangeListener() {
        @Override
        public void onProductChange(Product product, int position) {
            // Update the product in the list
            products.set(position, product);
            // Notify the adapter about the change
            notifyItemChanged(position);
        }
    };

    public ProductListAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productQuantity;
        public TextView productUnit;

        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productName);
            productQuantity = view.findViewById(R.id.productQuantity);
            productUnit = view.findViewById(R.id.productUnit);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productUnit.setText(product.getUnit());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setItems(new String[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Edit
                                Intent intent = new Intent(context, UpdateProductActivity.class);
                                intent.putExtra("product", product); // pass product to UpdateProductActivity
                                intent.putExtra("position", position); // pass product position to UpdateProductActivity
                                ((ShoppingListActivity) context).startActivityForResult(intent, 1);
                                break;
                            case 1: // Delete
                                products.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

}


