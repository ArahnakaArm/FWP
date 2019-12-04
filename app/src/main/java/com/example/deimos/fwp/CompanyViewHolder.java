package com.example.deimos.fwp;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class CompanyViewHolder extends GroupViewHolder {
    private TextView mTextView;
    private ImageView arrow;
    public CompanyViewHolder(View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.textView);
        arrow = itemView.findViewById(R.id.arrowexpand);
    }
    public void bind(Company company){
        mTextView.setText(company.getTitle());

    }
    @Override
    public void expand() {

        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(250);
        rotate.setFillAfter(true);
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_around_center_point);
        arrow.startAnimation(rotate);
    }

    @Override
    public void collapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(250);
        rotate.setFillAfter(true);
        Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.rotate_around_center_point);
        arrow.startAnimation(rotate);
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

}
