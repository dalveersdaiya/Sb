package `in`.ajm.sb_library.flashbar.view

import `in`.ajm.sb_library.R
import `in`.ajm.sb_library.flashbar.util.NavigationBarPosition
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

internal class ShadowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    internal fun applyShadow(type: ShadowType) {
        when (type) {
            NavigationBarPosition.TOP -> setShadow(R.drawable.shadow_top)
            NavigationBarPosition.BOTTOM -> setShadow(R.drawable.shadow_bottom)
        }
    }

    private fun setShadow(@DrawableRes id: Int) {
        val shadow = ContextCompat.getDrawable(context, id)
        if (SDK_INT >= JELLY_BEAN) {
            this.background = shadow
        } else {
            this.setBackgroundDrawable(shadow)
        }
    }

    enum class ShadowType {
        TOP, BOTTOM
    }
}