package com.example.basic_camera

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.basic_camera.databinding.ActivityMainBinding
import java.nio.file.Path

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realUri: String
    private val TAG = "HONGSI"
    private val TAKE_PICTURE = 1
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var mCurrentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        권한설정
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Storage WRITE not granted", Toast.LENGTH_SHORT).show()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "CAMERA not granted", Toast.LENGTH_SHORT).show()
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Storage READ not granted", Toast.LENGTH_SHORT).show()
        }

        permissions()

        binding.toCam.setOnClickListener {
            var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, TAKE_PICTURE)
        }
    }

    private fun permissions() {
        val writeEx =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readEx =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val camAcc = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        권한 설정이 하나라도 되어있지 않으면 권한 요청 -> 카메라
        if ((writeEx != PackageManager.PERMISSION_GRANTED)
            && (readEx != PackageManager.PERMISSION_GRANTED)
            && (camAcc != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        createImageUri(newFileName(), "image/jpg")?.let { uri ->
            realUri = uri.toString()
            // MediaStore.EXTRA_OUTPUT을 Key로 하여 Uri를 넘겨주면
            // 일반적인 Camera App은 이를 받아 내가 지정한 경로에 사진을 찍어서 저장시킨다.
            intent.putExtra(MediaStore.EXTRA_OUTPUT, realUri)
            startActivityForResult(intent, TAKE_PICTURE)
        }
    }

    private fun newFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
    }

    private fun createImageUri(filename: String, mimeType: String): Uri? {
        var values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        return this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    /** 카메라 및 앨범 Intent 결과
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                TAKE_PICTURE -> {
                    realUri?.let { uri ->
                        binding.photoPreview.setImageURI(uri)
                    }
                }
            }
        }
    }

//    fun startActivityForResult(intent: Intent?, requestCode: Int, resultCode: Int) {
//        if(requestCode == TAKE_PICTURE){
//            if (intent != null) {
//                if(resultCode == RESULT_OK && intent.hasExtra("data")){
//                    var bitmap: Bitmap? = intent.getExtras()?.get("data") as Bitmap
//                    if(bitmap != null){
//                        binding.photoPreview.setImageBitmap(bitmap)
//                    }
//                }
//            }
//        }
//    }
//
//    private fun File createImageFile(){
//
//    }
}

private fun ImageView.setImageURI(uri: String) {
//    TODO("Not yet implemented")
}
