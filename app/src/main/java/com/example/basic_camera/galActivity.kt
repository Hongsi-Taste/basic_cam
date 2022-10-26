package com.example.basic_camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.basic_camera.databinding.ActivityGalBinding

class galActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalBinding
    private var DEFAULT_GALLERY_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goGal.setOnClickListener {
            startDefaultGalleryApp()
        }
        binding.back.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun startDefaultGalleryApp(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        when(requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {
                data?:return
                val uri = data.data as Uri
            }

            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()
            }

        }
    }
}