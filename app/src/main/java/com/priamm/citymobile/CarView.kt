package com.priamm.citymobile

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import kotlin.math.abs
import kotlin.math.atan2

class CarView : FrameLayout {

    private lateinit var paint: Paint
    lateinit var car: Car
    private lateinit var path: Path
    private var isCarMoving: Boolean = false

    private val ROTATE_SPEED = 360f / 2000f
    private val MOVE_SPEED = 500f / 2000f

    private val carPosition: Point
        get() {
            val x = (car.x + car.width / 2).toInt()
            val y = (car.y + car.height / 2).toInt()
            return Point(x, y)
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        isClickable = true
        setWillNotDraw(false)
        clipChildren = false

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (!isCarMoving) {
                    val endCarPosition = Point(event.x.toInt(), event.y.toInt())
                    moveCar(endCarPosition)
                    isCarMoving = true
                }
            }
            true
        }

        paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
        paint.color = Color.BLACK
    }

    fun addCar(car: Car) {

        removeAllViews()
        this.car = car

        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        params.gravity = Gravity.CENTER or Gravity.BOTTOM

        val bottomMargin =  (64 * Resources.getSystem().displayMetrics.density).toInt()

        params.setMargins(
                0,
                0,
                0,
                 bottomMargin
        )

        car.layoutParams = params
        addView(car)
    }

    private fun moveCar(endCarPosition: Point) {
        val startCarPosition = carPosition

        val startCarAngle = car.rotation % 360
        val endCarAngle = getCarAngle(
                startCarPosition.x.toFloat(),
                startCarPosition.y.toFloat(),
                endCarPosition.x.toFloat(),
                endCarPosition.y.toFloat()
        )

        startCarMovingAnimation(startCarPosition, endCarPosition, startCarAngle, endCarAngle)
    }

    private fun startCarMovingAnimation(startCarPosition: Point,
                                        endCarPosition: Point,
                                        startCarAngle: Float,
                                        endCarAngle: Float) {
        var endCarAngle = endCarAngle
        val angleDelta = abs(endCarAngle - startCarAngle)
        if (endCarAngle > startCarAngle) {
            if (angleDelta > 180) {
                endCarAngle = startCarAngle - (360 - angleDelta)
            }
        } else if (startCarAngle > endCarAngle) {
            if (angleDelta > 180) {
                endCarAngle = startCarAngle + (360 - angleDelta)
            }
        }

        val rotateAnimator = ObjectAnimator.ofFloat(car, "rotation", startCarAngle, endCarAngle)
        rotateAnimator.duration = (abs(endCarAngle - startCarAngle) / ROTATE_SPEED).toLong()

        path = Path()

        path.moveTo(startCarPosition.x.toFloat(), startCarPosition.y.toFloat())
        path.lineTo(endCarPosition.x.toFloat(), endCarPosition.y.toFloat())

        invalidate()

        val moveAnimator = createMovePathAnimator(path)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(rotateAnimator, moveAnimator)
        animatorSet.start()
    }

    private fun createMovePathAnimator(path: Path): ValueAnimator {
        val pathAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)

        val pathMeasure = PathMeasure(path, false)
        pathAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            var point = FloatArray(2)
            var tan = FloatArray(2)

            override fun onAnimationUpdate(animation: ValueAnimator) {
                val animatedFraction = animation.animatedFraction
                pathMeasure.getPosTan(pathMeasure.length * animatedFraction, point, tan)
                car.x = point[0] - car.width / 2
                car.y = point[1] - car.height / 2
            }
        })

        pathAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                isCarMoving = false
            }

            override fun onAnimationCancel(animation: Animator) {
                isCarMoving = false
            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

        pathAnimator.interpolator = AccelerateDecelerateInterpolator()
        pathAnimator.duration = (pathMeasure.length / MOVE_SPEED).toLong()
        return pathAnimator
    }


    private fun getCarAngle(startX: Float, startY: Float, endX: Float, endY: Float): Float {

        val x1 = 0f
        val y1 = 100f
        val x2 = endX - startX
        val y2 = -(endY - startY)

        val dotProduct = x1 * x2 + y1 * y2
        val determinant = x1 * y2 - y1 * x2
        val angleRad = atan2(determinant.toDouble(), dotProduct.toDouble())
        val carAngle = ((360 - Math.toDegrees(angleRad)) % 360).toFloat()
        return carAngle
    }


}