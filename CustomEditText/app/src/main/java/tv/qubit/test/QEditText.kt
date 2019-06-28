package tv.qubit.test

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.google.android.material.resources.MaterialResources
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE

class QEditText : LinearLayout {

    private var mRootContainer: View
    private var mTextInputLayout: TextInputLayout
    private var mTextInputEditText: TextInputEditText
    private var mErrorTextView: TextView
    private var mBorderView: LinearLayout
    private var mOnFocusChangeListener: OnFocusChangeListener? = null
    private val mColorEditTextIconFocused: ColorStateList
    private val mColorEditTextIconDefault: ColorStateList
    private var mEndDrawable: Drawable? = null
    private var mEndTintColor: ColorStateList? = null

    var error: String?
        set(value) {
            mErrorTextView.visibility = when (value) {
                null -> View.GONE
                else -> View.VISIBLE
            }

            mErrorTextView.text = value
            updateBorderColor()
        }
        get() {
            return mErrorTextView.text.toString()
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        mRootContainer = inflate(context, R.layout.qedittext_view, this)
        mTextInputLayout = mRootContainer.findViewById(R.id.textLayout)
        mTextInputEditText = mRootContainer.findViewById(R.id.editText)
        mErrorTextView = mRootContainer.findViewById(R.id.errorTextView)
        mBorderView = mRootContainer.findViewById(R.id.borderView)



        mColorEditTextIconFocused =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorEditTextIconFocused))
        mColorEditTextIconDefault =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorEditTextIconDefault))

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.QEditText, 0, 0
        ).apply {

            try {

                if (hasValue(R.styleable.QEditText_hint)) {
                    mTextInputLayout.hint = getString(R.styleable.QEditText_hint)
                }

                if (hasValue(R.styleable.QEditText_text)) {
                    mTextInputEditText.setText(getString(R.styleable.QEditText_text))
                }

                setPasswordVisibilityToggleEnabled(getBoolean(R.styleable.QEditText_passwordToggleEnabled, false))

                if (hasValue(R.styleable.QEditText_startIconDrawable)) {
                    mTextInputLayout.startIconDrawable = getDrawable(R.styleable.QEditText_startIconDrawable)
                }

                if (hasValue(R.styleable.QEditText_endIconTint)) {
                    mEndTintColor = ColorStateList.valueOf(R.styleable.QEditText_endIconTint)
                }

                if (hasValue(R.styleable.QEditText_endIconDrawable)) {
                    mEndDrawable = getDrawable(R.styleable.QEditText_endIconDrawable)
                    setEndDrawableVisibility(getBoolean(R.styleable.QEditText_endIconVisible,false))
                }

                if (hasValue(R.styleable.QEditText_android_imeOptions)) {
                    mTextInputEditText.imeOptions =
                        getInt(R.styleable.QEditText_android_imeOptions, EditorInfo.IME_NULL)
                }

                if (hasValue(R.styleable.QEditText_android_inputType)) {
                    mTextInputEditText.inputType = getInt(R.styleable.QEditText_android_inputType, EditorInfo.TYPE_NULL)
                }

                mTextInputEditText.setOnFocusChangeListener { v, hasFocus ->
                    mOnFocusChangeListener?.onFocusChange(v, hasFocus)
                    mTextInputLayout.setStartIconTintList(
                        if (hasFocus) mColorEditTextIconFocused else mColorEditTextIconDefault
                    )

                    updateBorderColor()

                }

            } finally {
                recycle()
            }
        }
    }

    private fun updateBorderColor() {
        if (error.isNullOrEmpty()) {
            mBorderView.background = context.getDrawable(
                if (mTextInputEditText.isFocused) R.drawable.bg_qedittext_border_focused else R.drawable.bg_qedittext_border
            )

        } else {
            mBorderView.background = context.getDrawable(R.drawable.bg_qedittext_border_error)
        }
    }

    fun setPasswordVisibilityToggleEnabled(enabled: Boolean) {
        if (enabled && mTextInputLayout.endIconMode != END_ICON_PASSWORD_TOGGLE) {
            mTextInputLayout.endIconMode = END_ICON_PASSWORD_TOGGLE
        } else if (!enabled) {
            mTextInputLayout.endIconMode = END_ICON_NONE
        }
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener) {
        mOnFocusChangeListener = l
    }

    fun setEndDrawableVisibility(visible: Boolean) {
        if (visible && mTextInputLayout.endIconMode != TextInputLayout.END_ICON_CUSTOM) {
            mTextInputLayout.endIconDrawable = mEndDrawable

            if(mEndDrawable != null){
                if(mEndTintColor != null) {
                    mTextInputLayout.setEndIconTintList(mEndTintColor)
                    mTextInputLayout.setEndIconTintMode(PorterDuff.Mode.ADD)
                }
                mTextInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
            }else{
                mTextInputLayout.endIconMode = END_ICON_NONE
            }

        } else if (!visible) {
            mTextInputLayout.endIconMode = END_ICON_NONE
        }
    }

    fun setEndIconDrawable(drawable: Drawable?){
        mEndDrawable = drawable
        setEndDrawableVisibility(true)
    }

    fun setEndIconTintList(color: ColorStateList){
        mEndTintColor = color
        setEndDrawableVisibility(true)
    }

}