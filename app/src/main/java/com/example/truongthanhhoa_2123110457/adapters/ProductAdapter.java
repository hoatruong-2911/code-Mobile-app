package com.example.truongthanhhoa_2123110457.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import com.example.truongthanhhoa_2123110457.managers.CartManager;
import com.example.truongthanhhoa_2123110457.models.Product;
import com.example.truongthanhhoa_2123110457.R;
import com.example.truongthanhhoa_2123110457.activities.DetailActivity;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.txtTitle.setText(product.getTitle());
        double usdPrice = product.getPrice(); // Giá từ API
        double vndPrice = usdPrice * 25000;   // Quy đổi sang VNĐ

        DecimalFormat df = new DecimalFormat("#,###");
        holder.txtPrice.setText(df.format(vndPrice) + "đ");

        // Load ảnh bằng Glide
        Glide.with(context)
                .load(product.getImage())
                .into(holder.imgProduct);

        // 👉 Sự kiện click nút Xem chi tiết
        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("productId", product.getId()); // truyền id sang trang chi tiết
            context.startActivity(intent);
        });

        // ✅ Thêm sự kiện click cho nút Thêm vào giỏ hàng
        // Xử lý sự kiện click vào nút thêm giỏ hàng (hoặc toàn bộ item)
        holder.btnAddToCart.setOnClickListener(v -> {
            // ✅ Sửa lỗi tại đây: Thêm tham số số lượng là 1
            CartManager.getInstance().addItem(product, 1);
            Toast.makeText(context, "Đã thêm " + product.getTitle() + " vào giỏ hàng!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnViewDetail, btnAddToCart; // ✅ Thêm btnAddToCart
        TextView txtTitle, txtPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnViewDetail = itemView.findViewById(R.id.btn_view_detail); // ánh xạ nút chi tiết
            btnAddToCart = itemView.findViewById(R.id.btn_add_cart); // ✅ Ánh xạ nút giỏ hàng
        }
    }


    // danh mục
    public void updateList(List<Product> list) {
        this.productList = list;
        notifyDataSetChanged();
    }

}