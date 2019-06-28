package tv.qubit.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText.setOnFocusChangeListener { v, hasFocus ->
            Log.e("APPXX", "focus email: " + hasFocus)
            emailEditText.error = null
            emailEditText.setEndDrawableVisibility(true)
//            emailEditText.setEndIconTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.colorGreen)))
//            emailEditText.setEndIconDrawable(getDrawable(R.drawable.ic_check))
        }

        passwordEditText.setOnFocusChangeListener { v, hasFocus ->
            Log.e("APPXX", "focus pass: " + hasFocus)
            emailEditText.error = "mail invalido"
            //emailEditText.setEndIconDrawable(null)
            emailEditText.setEndDrawableVisibility(false)
        }

        //  emailTextLayout.error = "error lorem ipsum"
    }
}
