package com.yut.originalqualityphotoshare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context=context;
    }

    //Arrays
    private int[] slide_images = {
            R.drawable.pic_1,
            R.drawable.pic_2,
            R.drawable.pic_3
    };

    private String[] right_Text = {
            "",
            "Swipe right" +"\n"+"to receive",
            ""
            //blank strings to make the textviews invisible after swiping away from main screen
    };

    private String[] left_Text= {
            "",
            "Swipe left" +"\n"+"to send",
            ""
            //blank strings to make the textviews invisible after swiping away from main screen
    };


    @Override
    public int getCount() {
        return right_Text.length; //this works, but not the best implementation to get number of scrollable pages.
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }


    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView= (ImageView) view.findViewById(R.id.slideImageView);
        TextView RightText= (TextView) view.findViewById(R.id.rightText);
        TextView LeftText= (TextView) view.findViewById(R.id.leftText);

        slideImageView.setImageResource(slide_images[position]);
        RightText.setText(right_Text[position]);
        LeftText.setText(left_Text[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { //if error, check if non-null fixes it
        container.removeView((RelativeLayout)object);
    }
}
