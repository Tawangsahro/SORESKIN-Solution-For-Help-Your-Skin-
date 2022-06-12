package com.dicoding.bangkit.soreskin.util

import android.graphics.Bitmap
import android.net.Uri
import android.util.Patterns
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

object Extensions {

    fun ShapeableImageView.loadImage(image: String?) {
        Glide.with(this.context)
            .load(image)
            .into(this)
    }

    fun ShapeableImageView.loadImage(uri: Uri) {
        Glide.with(this.context)
            .load(uri)
            .into(this)
    }

    fun ShapeableImageView.loadImage(bitmap: Bitmap?) {
        Glide.with(this.context)
            .load(bitmap)
            .into(this)
    }

    fun String.isValidated(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}