package com.example.deimos.fwp

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.viewpager.widget.PagerAdapter
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide


class SlidingImage_Adapter(private val context: Context, private val urls: Array<Any>,private val urls2: Array<Any>,private val urls3: Array<Any>) : androidx.viewpager.widget.PagerAdapter() {
    private val inflater: LayoutInflater


    init {
        inflater = LayoutInflater.from(context)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return urls.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false)!!

        val imageView = imageLayout
                .findViewById<View>(R.id.image) as ImageView
        imageView.setOnClickListener {
           //d("Sli",urls[position])
            val newArray = urls2[position].toString()
            val intent = Intent(context, GalleryInfo::class.java)
            intent.putExtra("ID",newArray)
            context?.startActivity(intent)
        }

        Glide.with(context)
                .load(urls[position])
                .into(imageView)


        val text = imageLayout.findViewById<View>(R.id.textslide) as TextView
            text.setText(urls3[position].toString())
        view.addView(imageLayout, 0)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }


}