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
import com.google.android.gms.ads.*
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


class MainActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadBannerAd()
        loadInterAd()
        val pic = findViewById(R.id.imageView2)as ImageView
        Picasso.get().load("https://msmeafricaonline.com/wp-content/uploads/2020/07/MTN-BIZ-APP.jpg").into(pic)
        val btnshare = findViewById(R.id.btnShare)as Button
        btnshare.setOnClickListener {
            showInterAd()
        }
    }
    private  fun showInterAd(){
        if (mInterstitialAd != null){

            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun equals(other: Any?): Boolean {
                    return super.equals(other)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    val pic = findViewById(R.id.imageView2)as ImageView
                    Picasso.get().load("https://msmeafricaonline.com/wp-content/uploads/2020/07/MTN-BIZ-APP.jpg").into(pic)
                    val image: Bitmap? = getBimapFromView(pic)
                    val share = Intent(Intent.ACTION_SEND)
                    share.type = "image/"
                    share.putExtra(Intent.EXTRA_STREAM, getImageUrl(this@MainActivity,image!!))
                    startActivity(Intent.createChooser(share,"Share Via"))
                }
                override fun onAdImpression() {
                    super.onAdImpression()
                }
            }
mInterstitialAd?.show(this)
    }
        else{
            val pic = findViewById(R.id.imageView2)as ImageView
            Picasso.get().load("https://msmeafricaonline.com/wp-content/uploads/2020/07/MTN-BIZ-APP.jpg").into(pic)
            val image: Bitmap? = getBimapFromView(pic)
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/"
            share.putExtra(Intent.EXTRA_STREAM, getImageUrl(this,image!!))
            startActivity(Intent.createChooser(share,"Share Via"))
        }
    }
    private fun loadInterAd() {
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-8412818820026459/8279303591", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }
    private fun loadBannerAd() {
        MobileAds.initialize(this){}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
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