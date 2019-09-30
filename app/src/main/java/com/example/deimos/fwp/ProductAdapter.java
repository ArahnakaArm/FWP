package com.example.deimos.fwp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ProductAdapter extends ExpandableRecyclerViewAdapter<CompanyViewHolder,ProductViewHolder> {
    public ProductAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }
    private CompanyViewHolder companyViewHolder;
    private Context context;
    @Override
    public CompanyViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_company,parent,false);

        return new CompanyViewHolder(v);

    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_product,parent,false);

        return new ProductViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(final ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

            final Product product = (Product) group.getItems().get(childIndex);
            holder.bind(product);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.itemView.getContext(),product.name,Toast.LENGTH_SHORT).show();
                }
            });

    }

    @Override
    public void onBindGroupViewHolder(CompanyViewHolder holder, int flatPosition, ExpandableGroup group) {
            final Company company = (Company) group;
            holder.bind(company);


    }
}
