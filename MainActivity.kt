package com.thabang.picshare

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.io.ByteArrayOutputStream
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView



class MainActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this){}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val pic = findViewById(R.id.imageView2)as ImageView
        Picasso.get().load("https://msmeafricaonline.com/wp-content/uploads/2020/07/MTN-BIZ-APP.jpg").into(pic)
        val btnshare = findViewById(R.id.btnShare)as Button
        btnshare.setOnClickListener {

            val image: Bitmap? = getBimapFromView(pic)
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/"
            share.putExtra(Intent.EXTRA_STREAM, getImageUrl(this,image!!))
            startActivity(Intent.createChooser(share,"Share Via"))
        }

    }

    private fun getBimapFromView(view: ImageView): Bitmap?{
        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    private fun getImageUrl(inContext: Context, inImage: Bitmap):Uri?{
        val byte = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100, byte)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver,inImage, "Title", null)
        return Uri.parse(path)
    }
}

