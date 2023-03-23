package com.example.qzone9square

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.lang.Integer.min

class NineSquareImageView : ViewGroup {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : this(context, attributeSet, defStyleAttr, 0)
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attributeSet, defStyleAttr, defStyleRes)

    val horizontalSpacing = 24
    val verticalSpacing = 24
    val defaultWidth = 400
    val defaultHeight = 400
    var itemWidth = 0
    var itemHeight = 0
    var column = 0
    var row = 0

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentRow = 0
        var currentColumn = 0
        println("$childCount")
        for (i in 0 until childCount) {
            if (i == 9)
                break
            val child = getChildAt(i)
            val x = currentColumn * (itemWidth + horizontalSpacing) + paddingLeft
            val y = currentRow * (itemHeight + verticalSpacing) + paddingTop
            child.layout(x, y, x + itemWidth, y + itemHeight)
            currentColumn++
            if (currentColumn == column) {
                currentRow++
                currentColumn = 0
            }
        }
    }

    fun getRatio(childWidth : Double, childHeight :Double) =
        kotlin.math.min(itemWidth / childWidth, itemHeight / childHeight)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var widthSize = min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec))
        var heightSize = widthSize
        println("$childCount Size = $widthSize")
        if(childCount == 1) {
            measureChildren(
                MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST)
            )
            itemHeight = heightSize
            itemWidth = widthSize

            println("item $itemWidth $itemHeight")
            val singleImageView = getChildAt(0)
            var childWidth = singleImageView.measuredWidth.toDouble()
            var childHeight = singleImageView.measuredHeight.toDouble()
            println("child $childWidth $childHeight")

            val ratio = getRatio(childWidth,childHeight)
            println("$ratio")

            childWidth *= ratio
            childHeight *= ratio
            println("child $childWidth $childHeight")
            itemWidth = childWidth.toInt()
            itemHeight = childHeight.toInt()
            println("child $itemWidth $itemHeight")

            measureChildren(
                MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
            )
            println("${getChildAt(0).measuredWidth} ${getChildAt(0).measuredHeight}")

            widthSize = itemWidth + paddingLeft + paddingRight
            heightSize = itemHeight + paddingTop + paddingEnd
            setMeasuredDimension(widthSize, heightSize)
            println("par height = $heightSize")

        } else {
            println("Here")
            column = if (childCount == 4) 2 else min(childCount, 3)
            row = min((childCount - 1) / column + 1, 3)

            println("row = $row column = $column")

            itemWidth = (widthSize - paddingLeft - paddingRight - (column - 1) * horizontalSpacing) / column
            itemHeight = itemWidth

            measureChildren(
                MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY)
            )

            widthSize = paddingLeft + paddingRight + itemWidth * column + horizontalSpacing * (column - 1)
            heightSize = paddingTop + paddingEnd + itemHeight * row + verticalSpacing * (row - 1)

            println("Size = $widthSize")
            setMeasuredDimension(widthSize, heightSize)
        }
    }

}