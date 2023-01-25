package com.cozy.apps.backgroundremover

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.cozy.apps.backgroundremover.databinding.ActivityMainBinding
import com.cozy.apps.bgremover.BgRemover
import com.cozy.apps.bgremover.BgRemoverListener


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.removeBgBtn.setOnClickListener { removeBg() }
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { binding.img.setImageURI(it) }
        }.launch("image/*")
    }

    private fun removeBg() {
        val bitmap = binding.img.drawable.toBitmap()
        BgRemover().bitmapSegmentation(this, bitmap, object : BgRemoverListener {
            override fun onSuccess(bitmap: Bitmap) {
                binding.img.setImageBitmap(bitmap)
            }

            override fun onFailed(exception: Exception) {
                Toast.makeText(this@MainActivity, "Error Occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }
}