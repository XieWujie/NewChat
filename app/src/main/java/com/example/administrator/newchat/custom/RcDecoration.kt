package com.example.administrator.newchat.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RcDecoration:RecyclerView.ItemDecoration(){

    val paint = Paint()

    init {
        paint.color = Color.parseColor("#33666666")
        paint.strokeWidth = 3f
        paint.style = Paint.Style.FILL
    }
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = 1
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val count = parent.childCount
        val left = parent.left.toFloat()
        val right = parent.right.toFloat()
        for (i in 0 until count){
            val child = parent.getChildAt(i)
            val bottom = child.bottom.toFloat()
            c.drawRect(left,bottom,right,child.bottom+1f,paint)
        }
    }
}