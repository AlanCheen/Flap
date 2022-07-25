package me.yifeiyuan.flap.ext

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 程序亦非猿 on 2021/9/22.
 *
 * Flap Github: <a>https://github.com/AlanCheen/Flap</a>
 * @author 程序亦非猿 [Follow me](<a> https://github.com/AlanCheen</a>)
 * @since 2021/9/22
 * @since 3.0
 */

fun RecyclerView.setOnItemLongClickListener(listener: (childView: View, position: Int) -> Unit) {

    addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

        val gestureDetector =
                GestureDetector(context, object : GestureDetector.OnGestureListener {

                    override fun onDown(e: MotionEvent?): Boolean = false

                    override fun onShowPress(e: MotionEvent?) {
                    }

                    override fun onSingleTapUp(e: MotionEvent?): Boolean {
                        return true
                    }

                    override fun onScroll(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            distanceX: Float,
                            distanceY: Float
                    ): Boolean = false

                    override fun onLongPress(e: MotionEvent?) {
                        e?.let {
                            findChildViewUnder(e.x, e.y)?.let {
                                listener.invoke(it, getChildAdapterPosition(it))
                            }
                        }
                    }

                    override fun onFling(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                    ): Boolean = false
                })

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(e)
        }
    })
}

fun RecyclerView.setOnItemClickListener(listener: (childView: View, position: Int) -> Unit) {

    addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {

        val gestureDetector =
                GestureDetector(context, object : GestureDetector.OnGestureListener {

                    override fun onDown(e: MotionEvent?): Boolean = false

                    override fun onShowPress(e: MotionEvent?) {
                    }

                    override fun onSingleTapUp(e: MotionEvent?): Boolean {
                        e?.let {
                            findChildViewUnder(e.x, e.y)?.let {
                                listener.invoke(it, getChildAdapterPosition(it))
                                return true
                            }
                        }
                        return false
                    }

                    override fun onScroll(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            distanceX: Float,
                            distanceY: Float
                    ): Boolean = false

                    override fun onLongPress(e: MotionEvent?) {
                    }

                    override fun onFling(
                            e1: MotionEvent?,
                            e2: MotionEvent?,
                            velocityX: Float,
                            velocityY: Float
                    ): Boolean = false
                })

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            return gestureDetector.onTouchEvent(e)
        }
    })
}