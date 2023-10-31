package com.pokemon.mebius.giraffe.ui.page

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.mebius.commlib.utils.gone
import com.pokemon.mebius.commlib.utils.isVisible
import com.pokemon.mebius.commlib.utils.show
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.ui.view.GiraffeToast
import kotlinx.android.synthetic.main.giraffe_page_toast_list.view.toastHistoryRecyclerView
import kotlinx.android.synthetic.main.giraffe_page_toast_list.view.toastHistoryRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Description:
 * @Author:         Lollipop
 * @CreateDate:     2021/10/27
 */
@SuppressLint("NotifyDataSetChanged")
class GiraffeToastHistoryPage(context: Context) : GiraffeBasePage(context) {

    override fun getLayoutResId(): Int = R.layout.giraffe_page_toast_list

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setTitle(resources.getString(R.string.giraffe_toast_history_title))
        val toastAdapter = ToastAdapter()
        toastHistoryRecyclerView.adapter = toastAdapter
        toastHistoryRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        val loadData = {
            post {
                if (GiraffeToast.history.isEmpty()) {
                    showEmptyView()
                } else {
                    hideEmptyView()
                    toastAdapter.notifyDataSetChanged()
                }
                toastHistoryRefreshLayout.isRefreshing = false
            }
        }

        toastHistoryRefreshLayout.isRefreshing = true
        loadData()

        toastHistoryRefreshLayout.setOnRefreshListener {
            loadData()
        }

    }

    private class ToastAdapter : RecyclerView.Adapter<ToastHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToastHolder {
            return ToastHolder.create(parent)
        }

        override fun onBindViewHolder(holder: ToastHolder, position: Int) {
            holder.bind(GiraffeToast.history[position])
        }

        override fun getItemCount(): Int {
            return GiraffeToast.history.size
        }

    }

    private class ToastHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun create(parent: ViewGroup): ToastHolder {
                return ToastHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.giraffe_item_toast_list, parent, false)
                )
            }
        }

        private val timeView: TextView by lazy {
            itemView.findViewById(R.id.toastHistoryTimeView)
        }

        private val valueView: TextView by lazy {
            itemView.findViewById(R.id.toastHistoryValueView)
        }

        private val expandView: TextView by lazy {
            itemView.findViewById(R.id.toastHistoryExpandView)
        }

        private val simpleDateFormat by lazy {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA)
        }

        fun bind(toast: GiraffeToast.ToastHistory) {
            timeView.text = simpleDateFormat.format(Date(toast.time))
            valueView.text = toast.info.message
            if (toast.info.expand.isNotEmpty()) {
                expandView.show()
                expandView.text = toast.info.expand
            } else {
                expandView.gone()
            }

        }

    }


}
