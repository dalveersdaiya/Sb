package `in`.ajm.sb_library.flashbar.anim

import `in`.ajm.sb_library.flashbar.Flashbar
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.InterpolatorRes
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator

class FlashAnimBarBuilder(context: Context) : BaseFlashAnimBuilder(context) {

    private var type: Type? = null
    private var gravity: Flashbar.Gravity? = null
    private var direction: Direction? = null

    /**
     * Specifies the duration (in millis) for the animation
     */
    override fun duration(millis: Long) = apply { super.duration(millis) }

    /**
     * Specifies accelerate interpolator for the animation
     */
    override fun accelerate() = apply { super.accelerate() }

    /**
     * Specifies decelerate interpolator for the animation
     */
    override fun decelerate() = apply { super.decelerate() }

    /**
     * Specifies accelerate-decelerate interpolator for the animation
     */
    override fun accelerateDecelerate() = apply { super.accelerateDecelerate() }

    /**
     * Specifies custom interpolator for the animation
     */
    override fun interpolator(interpolator: Interpolator) = apply { super.interpolator(interpolator) }

    /**
     * Specifies custom interpolator resource for the animation
     */
    override fun interpolator(@InterpolatorRes id: Int) = apply { super.interpolator(id) }

    /**
     * Specifies that the animation should have alpha effect
     */
    override fun alpha() = apply { super.alpha() }

    /**
     * Specifies that the bar should slide to/from the left
     */
    fun slideFromLeft() = apply {
        this.direction = Direction.LEFT
    }

    /**
     * Specifies that the bar should slide to/from the right
     */
    fun slideFromRight() = apply {
        this.direction = Direction.RIGHT
    }

    /**
     * Specifies overshoot interpolator for the animation
     */
    fun overshoot() = apply {
        this.interpolator = OvershootInterpolator()
    }

    /**
     * Specifies overshoot-anticipate interpolator for the animation
     */
    fun anticipateOvershoot() = apply {
        this.interpolator = AnticipateInterpolator()
    }

    override fun withView(view: View) = apply { super.withView(view) }

    internal fun enter() = apply {
        this.type = Type.ENTER
    }

    internal fun exit() = apply {
        this.type = Type.EXIT
    }

    internal fun fromTop() = apply {
        this.gravity = Flashbar.Gravity.TOP
    }

    internal fun fromBottom() = apply {
        this.gravity = Flashbar.Gravity.BOTTOM
    }

    internal fun build(): FlashAnim {
        requireNotNull(view, { "Target view can not be null" })

        val compositeAnim = AnimatorSet()
        val animators = linkedSetOf<Animator>()

        val translationAnim = ObjectAnimator()
        // Slide from left/right animation is not specified, default top/bottom
        // animation is applied
        if (direction == null) {
            translationAnim.propertyName = "translationY"

            when (type!!) {
                Type.ENTER -> when (gravity!!) {
                    Flashbar.Gravity.TOP -> translationAnim.setFloatValues(-view!!.height.toFloat(), 0f)
                    Flashbar.Gravity.BOTTOM -> translationAnim.setFloatValues(view!!.height.toFloat(), 0f)
                }
                Type.EXIT -> when (gravity!!) {
                    Flashbar.Gravity.TOP -> translationAnim.setFloatValues(0f, -view!!.height.toFloat())
                    Flashbar.Gravity.BOTTOM -> translationAnim.setFloatValues(0f, view!!.height.toFloat())
                }
            }
        } else {
            translationAnim.propertyName = "translationX"

            when (type!!) {
                Type.ENTER -> when (direction!!) {
                    Direction.LEFT -> translationAnim.setFloatValues(-view!!.width.toFloat(), 0f)
                    Direction.RIGHT -> translationAnim.setFloatValues(view!!.width.toFloat(), 0f)
                }
                Type.EXIT -> when (direction!!) {
                    Direction.LEFT -> translationAnim.setFloatValues(0f, -view!!.width.toFloat())
                    Direction.RIGHT -> translationAnim.setFloatValues(0f, view!!.width.toFloat())
                }
            }
        }

        translationAnim.target = view
        animators.add(translationAnim)

        if (alpha) {
            val alphaAnim = ObjectAnimator()
            alphaAnim.propertyName = "alpha"
            alphaAnim.target = view

            when (type!!) {
                Type.ENTER -> alphaAnim.setFloatValues(DEFAULT_ALPHA_START, DEFAULT_ALPHA_END)
                Type.EXIT -> alphaAnim.setFloatValues(DEFAULT_ALPHA_END, DEFAULT_ALPHA_START)
            }
            animators.add(alphaAnim)
        }

        compositeAnim.playTogether(animators)
        compositeAnim.duration = duration
        compositeAnim.interpolator = interpolator

        return FlashAnim(compositeAnim)
    }

    enum class Type { ENTER, EXIT }
    enum class Direction { LEFT, RIGHT }
}