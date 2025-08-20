package com.example.truongthanhhoa_2123110457.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.truongthanhhoa_2123110457.models.CartItem;
import com.example.truongthanhhoa_2123110457.managers.CartManager;
import com.example.truongthanhhoa_2123110457.models.Product;
import com.example.truongthanhhoa_2123110457.R;

import java.text.DecimalFormat;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private OnCartUpdateListener updateListener;

    // ✅ Interface để gọi lại hàm cập nhật tổng tiền từ OrderActivity
    public interface OnCartUpdateListener {
        void onCartUpdated();
    }

    public CartItemAdapter(Context context, List<CartItem> cartItems, OnCartUpdateListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.updateListener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng một layout item riêng cho giỏ hàng
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Product product = cartItem.getProduct();

        // Đổ dữ liệu vào các view
        holder.cartTitle.setText(product.getTitle());
        double vndPrice = product.getPrice() * 25000;
        DecimalFormat df = new DecimalFormat("#,###đ");
        holder.cartPrice.setText(df.format(vndPrice));
        holder.txtQuantity.setText(String.valueOf(cartItem.getQuantity()));
        Glide.with(context).load(product.getImage()).into(holder.cartImage);

        // ✅ Gán trạng thái đã chọn cho CheckBox
        holder.checkBoxItem.setChecked(cartItem.isSelected());

        // ✅ Xử lý sự kiện khi CheckBox thay đổi trạng thái
        holder.checkBoxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartItem.setSelected(isChecked);
            updateListener.onCartUpdated(); // Cập nhật lại tổng tiền
        });

        // ✅ Sự kiện click nút cộng
        holder.btnPlus.setOnClickListener(v -> {
            CartManager.getInstance().increaseQuantity(cartItem);
            notifyItemChanged(position); // Cập nhật lại item
            updateListener.onCartUpdated(); // Cập nhật tổng tiền
        });

        // ✅ Sự kiện click nút trừ
        holder.btnMinus.setOnClickListener(v -> {
            CartManager.getInstance().decreaseQuantity(cartItem);
            if (cartItem.getQuantity() == 0) {
                // Nếu sản phẩm bị xóa, cập nhật toàn bộ danh sách
                notifyDataSetChanged();
            } else {
                notifyItemChanged(position); // Cập nhật lại item
            }
            updateListener.onCartUpdated(); // Cập nhật tổng tiền
        });

        // ✅ Sự kiện click nút xóa
        holder.btnRemoveItem.setOnClickListener(v -> {
            CartManager.getInstance().removeItem(cartItem);
            notifyDataSetChanged(); // Cập nhật toàn bộ danh sách
            updateListener.onCartUpdated(); // Cập nhật tổng tiền
            Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView cartTitle, cartPrice, txtQuantity;
        ImageView cartImage, btnPlus, btnMinus, btnRemoveItem;
        CheckBox checkBoxItem; // ✅ Khai báo CheckBox

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cartTitle = itemView.findViewById(R.id.cartTitle);
            cartPrice = itemView.findViewById(R.id.cartPrice);
            cartImage = itemView.findViewById(R.id.cartImage);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemoveItem = itemView.findViewById(R.id.btnRemoveItem);
            checkBoxItem = itemView.findViewById(R.id.checkBoxItem); // ✅ Ánh xạ CheckBox
        }
    }
}