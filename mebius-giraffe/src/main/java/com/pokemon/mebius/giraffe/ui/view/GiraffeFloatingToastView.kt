package com.pokemon.mebius.giraffe.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GiraffeFloatingToastView(context: Context) : FrameLayout(context),
    GiraffeToast.ToastProvider {

    companion object {
        private const val TOAST_REMOVE_DELAY = 5 * 1000L
    }

    private val toastAdapter = ToastAdapter(::onToastCreate)

    /**
     * toast需要保证在主线程
     */
    private val toastHandler = Handler(Looper.getMainLooper())

    private var isShow = false

    private val mWindowManager: WindowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    private val mParams: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams()
    }

    private val toastListView by lazy {
        RecyclerView(context).apply {
            adapter = toastAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private val hidePanelTask = Runnable {
        if (toastAdapter.itemCount == 0) {
            visibility = View.GONE
        }
    }

    init {
        addView(toastListView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    private fun onToastCreate(key: String) {
        toastHandler.postDelayed({
            callToastRemove(key)
        }, TOAST_REMOVE_DELAY)
    }

    private fun callToastRemove(key: String) {
        toastAdapter.removeToast(key)
        toastHandler.removeCallbacks(hidePanelTask)
        toastHandler.postDelayed(hidePanelTask, 1000L)
    }

    /**
     * 回调触发时可能在非UI线程，所以需要全都放在post中
     */
    override fun onNewToast(toastList: ArrayList<GiraffeToast.ToastInfo>) {
        toastHandler.post {
            visibility = View.VISIBLE
            toastAdapter.addToast(toastList)
            toastListView.scrollToPosition(0)
        }
    }

    fun show() {
        if (isShow) return

        isShow = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }

        val fullWidth = resources.displayMetrics.widthPixels
        val fullHeight = resources.displayMetrics.heightPixels

        mParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        mParams.format = PixelFormat.RGBA_8888
        mParams.gravity = Gravity.END or Gravity.TOP
        mParams.width = fullWidth / 2
        mParams.height = fullHeight / 3
        try {
            mWindowManager.addView(this, mParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GiraffeToast.bindProvider(this)
        GiraffeToast.showToast("Giraffe Toast 已启动")
    }

    fun hide() {
        isShow = false
        mWindowManager.removeView(this)
        GiraffeToast.unbindProvider()
    }

    private class ToastItemInfo(val key: String, val value: GiraffeToast.ToastInfo)

    private class ToastAdapter(
        private val onToastCreate: (key: String) -> Unit
    ) : RecyclerView.Adapter<ToastViewHolder>() {

        private val data = ArrayList<ToastItemInfo>()

        fun addToast(values: List<GiraffeToast.ToastInfo>) {
            val now = System.currentTimeMillis()
            for (index in values.indices) {
                val key = "$now-$index"
                data.add(0, ToastItemInfo(key, values[index]))
                onToastCreate(key)
            }
            notifyItemRangeInserted(0, values.size)
        }

        fun removeToast(key: String) {
            for (index in data.indices) {
                // 默认为key不可能重复
                if (data[index].key == key) {
                    data.removeAt(index)
                    notifyItemRemoved(index)
                    return
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToastViewHolder {
            return ToastViewHolder.create(parent)
        }

        override fun onBindViewHolder(holder: ToastViewHolder, position: Int) {
            holder.bind(data[position].value)
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    private class ToastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun create(group: ViewGroup): ToastViewHolder {
                return ToastViewHolder(createToastView(group.context))
            }

            fun createToastView(context: Context): View {
                return FrameLayout(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    addView(TextView(context).apply {
                        val marginV = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 5F, context.resources.displayMetrics
                        ).toInt()
                        val marginH = TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10F, context.resources.displayMetrics
                        ).toInt()
                        layoutParams = LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER_VERTICAL or Gravity.END
                            setMargins(marginH, marginV, marginH, marginV)
                        }
                        background = ToastBackground(context)
                        setTextColor(Color.WHITE)
                        setPadding(marginH, marginV, marginH, marginV)
                    })
                }
            }
        }

        fun bind(value: GiraffeToast.ToastInfo) {
            itemView.let { group ->
                if (group is ViewGroup) {
                    group.getChildAt(0)?.let { textView ->
                        if (textView is TextView) {
                            textView.text = value.toastValue
                        }
                    }
                }
            }
        }

    }

    private class ToastBackground(context: Context) : Drawable() {

        private val paint = Paint().apply {
            isAntiAlias = true
            color = 0x88000000.toInt()
        }

        private val radius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 5F, context.resources.displayMetrics
        )

        private val boundsF = RectF()

        override fun draw(canvas: Canvas) {
            canvas.drawRoundRect(boundsF, radius, radius, paint)
        }

        override fun onBoundsChange(bounds: Rect) {
            super.onBoundsChange(bounds)
            bounds ?: return
            boundsF.set(bounds)
            invalidateSelf()
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
            paint.colorFilter = colorFilter
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

    }
}