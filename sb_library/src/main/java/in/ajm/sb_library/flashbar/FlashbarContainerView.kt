package `in`.ajm.sb_library.flashbar

import `in`.ajm.sb_library.flashbar.Flashbar.Companion.DURATION_INDEFINITE
import `in`.ajm.sb_library.flashbar.anim.FlashAnim
import `in`.ajm.sb_library.flashbar.anim.FlashAnimBarBuilder
import `in`.ajm.sb_library.flashbar.anim.FlashAnimIconBuilder
import `in`.ajm.sb_library.flashbar.util.*
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.HapticFeedbackConstants.VIRTUAL_KEY
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.RelativeLayout

/**
 * Container withView matching the height and width of the parent to hold a FlashbarView.
 * It will occupy the entire screens size but will be completely transparent. The
 * FlashbarView inside is the only visible component in it.
 */
internal class FlashbarContainerView(context: Context)
    : RelativeLayout(context), SwipeDismissTouchListener.DismissCallbacks {

    internal lateinit var parentFlashbar: Flashbar

    private lateinit var flashbarView: FlashbarView

    private lateinit var enterAnimBuilder: FlashAnimBarBuilder
    private lateinit var exitAnimBuilder: FlashAnimBarBuilder
    private lateinit var vibrationTargets: List<Flashbar.Vibration>

    private var onBarShowListener: Flashbar.OnBarShowListener? = null
    private var onBarDismissListener: Flashbar.OnBarDismissListener? = null
    private var onTapOutsideListener: Flashbar.OnTapListener? = null
    private var overlayColor: Int? = null
    private var iconAnimBuilder: FlashAnimIconBuilder? = null

    private var duration = DURATION_INDEFINITE
    private var isBarShowing = false
    private var isBarShown = false
    private var isBarDismissing = false
    private var barDismissOnTapOutside: Boolean = false
    private var showOverlay: Boolean = false
    private var overlayBlockable: Boolean = false

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                val rect = Rect()
                flashbarView.getHitRect(rect)

                // Checks if the tap was outside the bar
                if (!rect.contains(event.x.toInt(), event.y.toInt())) {
                    onTapOutsideListener?.onTap(parentFlashbar)

                    if (barDismissOnTapOutside) {
                        dismissInternal(Flashbar.DismissEvent.TAP_OUTSIDE)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun onSwipe(isSwiping: Boolean) {
        isBarDismissing = isSwiping
        if (isSwiping) {
            onBarDismissListener?.onDismissing(parentFlashbar, true)
        }
    }

    override fun onDismiss(view: View) {
        (parent as? ViewGroup)?.removeView(this@FlashbarContainerView)
        isBarShown = false

        flashbarView.stopIconAnimation()

        if (vibrationTargets.contains(Flashbar.Vibration.DISMISS)) {
            performHapticFeedback(VIRTUAL_KEY)
        }

        onBarDismissListener?.onDismissed(parentFlashbar, Flashbar.DismissEvent.SWIPE)
    }

    internal fun attach(flashbarView: FlashbarView) {
        this.flashbarView = flashbarView
    }

    internal fun construct() {
        isHapticFeedbackEnabled = true

        if (showOverlay) {
            setBackgroundColor(overlayColor!!)

            if (overlayBlockable) {
                isClickable = true
                isFocusable = true
            }
        }

        addView(flashbarView)
    }

    internal fun addParent(flashbar: Flashbar) {
        this.parentFlashbar = flashbar
    }

    internal fun adjustOrientation(activity: Activity) {
        val flashbarContainerViewLp = RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        val navigationBarPosition = activity.getNavigationBarPosition()
        val navigationBarSize = activity.getNavigationBarSizeInPx()

        when (navigationBarPosition) {
            NavigationBarPosition.LEFT -> flashbarContainerViewLp.leftMargin = navigationBarSize
            NavigationBarPosition.RIGHT -> flashbarContainerViewLp.rightMargin = navigationBarSize
            NavigationBarPosition.BOTTOM -> flashbarContainerViewLp.bottomMargin = navigationBarSize
        }

        layoutParams = flashbarContainerViewLp
    }


    internal fun show(activity: Activity) {
        if (isBarShowing || isBarShown) return

        val activityRootView = activity.getRootView() ?: return

        // Only add the withView to the parent once
        if (this.parent == null) activityRootView.addView(this)

        activityRootView.afterMeasured {
            val enterAnim = enterAnimBuilder.withView(flashbarView).build()
            enterAnim.start(object : FlashAnim.InternalAnimListener {
                override fun onStart() {
                    isBarShowing = true
                    onBarShowListener?.onShowing(parentFlashbar)
                }

                override fun onUpdate(progress: Float) {
                    onBarShowListener?.onShowProgress(parentFlashbar, progress)
                }

                override fun onStop() {
                    isBarShowing = false
                    isBarShown = true

                    flashbarView.startIconAnimation(iconAnimBuilder)

                    if (vibrationTargets.contains(Flashbar.Vibration.SHOW)) {
                        performHapticFeedback(VIRTUAL_KEY)
                    }

                    onBarShowListener?.onShown(parentFlashbar)
                }
            })

            handleDismiss()
        }
    }

    internal fun dismiss() {
        dismissInternal(Flashbar.DismissEvent.MANUAL)
    }

    internal fun isBarShowing() = isBarShowing

    internal fun isBarShown() = isBarShown

    internal fun setDuration(duration: Long) {
        this.duration = duration
    }

    internal fun setBarShowListener(listener: Flashbar.OnBarShowListener?) {
        this.onBarShowListener = listener
    }

    internal fun setBarDismissListener(listener: Flashbar.OnBarDismissListener?) {
        this.onBarDismissListener = listener
    }

    internal fun setBarDismissOnTapOutside(dismiss: Boolean) {
        this.barDismissOnTapOutside = dismiss
    }

    internal fun setOnTapOutsideListener(listener: Flashbar.OnTapListener?) {
        this.onTapOutsideListener = listener
    }

    internal fun setOverlay(overlay: Boolean) {
        this.showOverlay = overlay
    }

    internal fun setOverlayColor(color: Int) {
        this.overlayColor = color
    }

    internal fun setOverlayBlockable(blockable: Boolean) {
        this.overlayBlockable = blockable
    }

    internal fun setEnterAnim(builder: FlashAnimBarBuilder) {
        this.enterAnimBuilder = builder
    }

    internal fun setExitAnim(builder: FlashAnimBarBuilder) {
        this.exitAnimBuilder = builder
    }

    internal fun enableSwipeToDismiss(enable: Boolean) {
        this.flashbarView.enableSwipeToDismiss(enable, this)
    }

    internal fun setVibrationTargets(targets: List<Flashbar.Vibration>) {
        this.vibrationTargets = targets
    }

    internal fun setIconAnim(builder: FlashAnimIconBuilder?) {
        this.iconAnimBuilder = builder
    }

    private fun handleDismiss() {
        if (duration != DURATION_INDEFINITE) {
            postDelayed({ dismissInternal(Flashbar.DismissEvent.TIMEOUT) }, duration)
        }
    }

    private fun dismissInternal(event: Flashbar.DismissEvent) {
        if (isBarDismissing || isBarShowing || !isBarShown) {
            return
        }

        val exitAnim = exitAnimBuilder.withView(flashbarView).build()
        exitAnim.start(object : FlashAnim.InternalAnimListener {
            override fun onStart() {
                isBarDismissing = true
                onBarDismissListener?.onDismissing(parentFlashbar, false)
            }

            override fun onUpdate(progress: Float) {
                onBarDismissListener?.onDismissProgress(parentFlashbar, progress)
            }

            override fun onStop() {
                isBarDismissing = false
                isBarShown = false

                if (vibrationTargets.contains(Flashbar.Vibration.DISMISS)) {
                    performHapticFeedback(VIRTUAL_KEY)
                }

                onBarDismissListener?.onDismissed(parentFlashbar, event)

                post { (parent as? ViewGroup)?.removeView(this@FlashbarContainerView) }
            }
        })
    }
}
