package co.com.ocrdemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val SELECT_PICTURE_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTextExtract?.movementMethod = ScrollingMovementMethod()
        btnSelect?.setOnClickListener { requestImage() }

    }


    private fun requestImage() {
        startActivityForResult(Intent.createChooser(Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }, getString(R.string.select_picture_text)), SELECT_PICTURE_REQUEST_CODE)
    }

    private fun extractImage(uri: Uri): Bitmap? {
        return BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
    }

    private fun extractText(bitmap: Bitmap) {
        val frame = Frame.Builder()
                .setBitmap(bitmap)
                .build()
        val recognizer = TextRecognizer.Builder(this).build()
        val textBlocks = recognizer.detect(frame)
        tvTextExtract?.text = ""
        for (i in 0 until textBlocks.size()) {
            tvTextExtract?.append(textBlocks[textBlocks.keyAt(i)].value)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK
                && data !== null && data.data !== null) {
            extractImage(data.data)?.let {
                ivImageSelected.setImageBitmap(it)
                extractText(it)
            }


        }
    }
}
