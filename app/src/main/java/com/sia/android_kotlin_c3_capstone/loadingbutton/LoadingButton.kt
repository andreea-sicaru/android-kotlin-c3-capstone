package com.sia.android_kotlin_c3_capstone.loadingbutton

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.sia.android_kotlin_c3_capstone.R
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var valueAnimator = ValueAnimator()
    private var animateValue = 0f

    private var circleRadius = 20f
    private var circleSweep = 0f

    private var downloadText = resources.getString(R.string.download)
    private var loadingText = resources.getString(R.string.we_are_loading)
    private var baseColor = resources.getColor(R.color.colorPrimary)
    private var loadingColor = resources.getColor(R.color.colorPrimaryDark)
    private var textColor = resources.getColor(R.color.colorOnSurface)
    private var circleColor = resources.getColor(R.color.colorAccent)

    private val paint = Paint().apply {
        isAntiAlias = true
        textSize = resources.getDimension(R.dimen.buttonTextSize)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                startLoadingAnimation()
            }

            ButtonState.Clicked -> {
                buttonState = ButtonState.Loading
            }

            ButtonState.Completed -> {
                invalidate()
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            downloadText = getString(R.styleable.LoadingButton_downloadText) ?: downloadText
            loadingText = getString(R.styleable.LoadingButton_loadingColor) ?: loadingText
            baseColor = getColor(R.styleable.LoadingButton_baseColor, baseColor)
            loadingColor = getColor(R.styleable.LoadingButton_loadingColor, loadingColor)
            textColor = getColor(R.styleable.LoadingButton_textColor, textColor)
            circleColor = getColor(R.styleable.LoadingButton_circleColor, circleColor)
        }
        isClickable = true
    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState = ButtonState.Clicked
        return true
    }

    private fun startLoadingAnimation() {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 2000

            addUpdateListener { animation ->
                animateValue = animation.animatedValue as Float
                circleSweep = animateValue * 360f
                invalidate()
            }

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isClickable = false
                }

                override fun onAnimationEnd(animation: Animator) {
                    isClickable = true
                    buttonState = ButtonState.Completed
                    invalidate()
                }
            })
        }
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw the background
        paint.color = baseColor
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // Determine text here as we need it's width for circle position
        val text = if (buttonState == ButtonState.Loading) loadingText else downloadText

        // Draw loading animations
        if (buttonState == ButtonState.Loading) {
            // Draw backgroung color animation
            paint.color = loadingColor
            canvas.drawRect(0f, 0f, animateValue * widthSize, heightSize.toFloat(), paint)

            // Draw circle loading animation
            paint.color = circleColor
            val textWidth = paint.measureText(text)
            canvas.drawArc(
                widthSize / 2f - circleRadius + textWidth / 2f + 20f, // 20f padding between text & circle
                heightSize / 2f - circleRadius,
                widthSize / 2f + circleRadius + textWidth / 2f + 20f,
                heightSize / 2f + circleRadius,
                0f,
                circleSweep + 90f,
                true,
                paint
            )

        }

        // Draw the text
        paint.color = textColor
        val textX = (widthSize / 2).toFloat()
        val textY = (heightSize / 2 - (paint.descent() + paint.ascent()) / 2)
        canvas.drawText(text, textX, textY, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}