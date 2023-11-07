package unit.chip.libchip.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import unit.chip.libchip.R


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class DotProgressBar(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private var mDotSize = 0f
    private var mSpacing = 0f
    private var mJumpingSpeed = 0
    private var mEmptyDotsColor = 0
    private var mActiveDotColor = 0
    private var mActiveDot: Drawable? = null
    private var mInactiveDot: Drawable? = null
    private var isInProgress: Boolean
    private var isActiveDrawable = false
    private var isInactiveDrawable = false
    private var mActiveDotIndex = 0
    private var mNumberOfDots = 0
    private var mPaint: Paint? = null
    private var mPaddingLeft = 0
    private lateinit var mHandler: Handler
    private val mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (mNumberOfDots != 0) mActiveDotIndex = (mActiveDotIndex + 1) % mNumberOfDots
            this@DotProgressBar.invalidate()
            mHandler.postDelayed(this, mJumpingSpeed.toLong())
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until mNumberOfDots) {
            val x = (paddingLeft + mPaddingLeft + mSpacing / 2 + i * (mSpacing + mDotSize)).toInt()
            if (isInactiveDrawable) {
                mInactiveDot!!.setBounds(
                    x,
                    paddingTop,
                    (x + mDotSize).toInt(),
                    paddingTop + mDotSize.toInt()
                )
                mInactiveDot!!.draw(canvas)
            } else {
                mPaint!!.color = mEmptyDotsColor
                canvas.drawCircle(
                    x + mDotSize / 2,
                    paddingTop + mDotSize / 2, mDotSize / 2, mPaint!!
                )
            }
        }
        if (isInProgress) {
            val x =
                (paddingLeft + mPaddingLeft + mSpacing / 2 + mActiveDotIndex * (mSpacing + mDotSize)).toInt()
            if (isActiveDrawable) {
                mActiveDot!!.setBounds(
                    x,
                    paddingTop,
                    (x + mDotSize).toInt(),
                    paddingTop + mDotSize.toInt()
                )
                mActiveDot!!.draw(canvas)
                if (mActiveDotIndex > 0 && mActiveDotIndex < 8) {
                    for (j in 0 until mActiveDotIndex) {
                        val x1 =
                            (paddingLeft + mPaddingLeft + mSpacing / 2 + j * (mSpacing + mDotSize)).toInt()
                        mActiveDot!!.setBounds(
                            x1,
                            paddingTop, (x1 + mDotSize).toInt(), paddingTop + mDotSize.toInt()
                        )
                        mActiveDot!!.draw(canvas)
                    }
                }
            } else {
                mPaint!!.color = mActiveDotColor
                canvas.drawCircle(
                    x + mDotSize / 2,
                    paddingTop + mDotSize / 2, mDotSize / 2, mPaint!!
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthWithoutPadding = parentWidth - paddingLeft - paddingRight
        val heigthWithoutPadding = parentHeight - paddingTop - paddingBottom

        //setMeasuredDimension(parentWidth, calculatedHeight);
        val calculatedHeight = paddingTop + paddingBottom + mDotSize.toInt()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(parentWidth, calculatedHeight)
        //        mNumberOfDots = /*calculateDotsNumber(widthWithoutPadding)*/;
    }

    private fun calculateDotsNumber(width: Int): Int {
        val number = (width / (mDotSize + mSpacing)).toInt()
        mPaddingLeft = (width % (mDotSize + mSpacing) / 2).toInt()
        //setPadding(getPaddingLeft() + (int) mPaddingLeft, getPaddingTop(), getPaddingRight() + (int) mPaddingLeft, getPaddingBottom());
        return number
    }

    fun startProgress() {
        isInProgress = true
        mActiveDotIndex = -1
        mHandler.removeCallbacks(mRunnable)
        mHandler.post(mRunnable)
    }

    fun stopProgress() {
        isInProgress = false
        mHandler.removeCallbacks(mRunnable)
        invalidate()
    }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DottedProgressBar,
            0, 0
        )
        isInProgress = false
        mHandler = Handler()
        try {
//            mEmptyDotsColor = a.getColor(R.styleable.DottedProgressBar_emptyDotsColor, Color.WHITE);
//            mActiveDotColor = a.getColor(R.styleable.DottedProgressBar_activeDotColor, Color.BLUE);
            val value = TypedValue()
            a.getValue(R.styleable.DottedProgressBar_activeDot, value)
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // It's a color
                isActiveDrawable = false
                mActiveDotColor = resources.getColor(value.resourceId)
            } else if (value.type == TypedValue.TYPE_STRING) {
                // It's a reference, hopefully to a drawable
                isActiveDrawable = true
                mActiveDot = resources.getDrawable(value.resourceId)
            }
            a.getValue(R.styleable.DottedProgressBar_inactiveDot, value)
            if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // It's a color
                isInactiveDrawable = false
                mEmptyDotsColor = resources.getColor(value.resourceId)
            } else if (value.type == TypedValue.TYPE_STRING) {
                // It's a reference, hopefully to a drawable
                isInactiveDrawable = true
                mInactiveDot = resources.getDrawable(value.resourceId)
            }
            mDotSize = a.getDimensionPixelSize(R.styleable.DottedProgressBar_dotSize, 5).toFloat()
            mSpacing = a.getDimensionPixelSize(R.styleable.DottedProgressBar_spacing, 10).toFloat()
            mActiveDotIndex = a.getInteger(R.styleable.DottedProgressBar_activeDotIndex, 0)
            mNumberOfDots = a.getInteger(R.styleable.DottedProgressBar_numberOfDots, 0)
            mJumpingSpeed = a.getInt(R.styleable.DottedProgressBar_jumpingSpeed, 500)
            mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mPaint!!.setStyle(Paint.Style.FILL)
        } finally {
            a.recycle()
        }
    }
}