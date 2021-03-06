package com.example.deimos.fwp;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ProductViewHolder extends ChildViewHolder {
    private TextView mTextView;
    public ProductViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.textView);
    }
    public void bind(Product product){
        mTextView.setText(product.getName());
    }
}
