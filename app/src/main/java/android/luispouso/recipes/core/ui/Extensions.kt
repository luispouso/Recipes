package android.luispouso.recipes.core.ui

import android.app.Activity
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun EditText.textWatcherListener(
    beforeTextChanged: (() -> Unit)? = null,
    onTextChanged: (() -> Unit)? = null,
    afterTextChanged: (() -> Unit)? = null
) {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged?.invoke()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged?.invoke()
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged?.invoke()
        }
    }
    addTextChangedListener(textWatcher)
}

fun Fragment.showToast(message: String) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImageWithGlide(uri: Uri) {
    Glide.with(this)
        .load(uri)
        .centerCrop()
        .circleCrop()
        .into(this)
}