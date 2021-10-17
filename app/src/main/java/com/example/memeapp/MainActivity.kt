package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var CurrImageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }

    private fun loadMeme()
    {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null, { response ->
                CurrImageUrl = response.getString("url")
                val MemeImageView = this.findViewById<ImageView>(R.id.MemeImageView)

                Glide.with(this).load(CurrImageUrl).listener(object: RequestListener<Drawable>{

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity,"Image Stopped",Toast.LENGTH_SHORT)
                        return false
                    }
                }).into(MemeImageView)
            },
            Response.ErrorListener{
                Toast.makeText(this,"Error occured",Toast.LENGTH_SHORT)
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(JsonObjectRequest)
    }

    fun nextMeme(view: android.view.View) {
        loadMeme()
    }
    fun ShareMeme(view: android.view.View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey! Check Out this Meme i got from Reddit: $CurrImageUrl")
        val chooser = Intent.createChooser(intent,"Check the Options for Sharing the Meme.")
        startActivity(chooser)
    }
}