package com.pokemon.mebius.giraffe.ui.page

import android.content.Context
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.entities.GiraffeExceptionInfo
import com.pokemon.mebius.giraffe.storage.GiraffeStorage
import com.pokemon.mebius.giraffe.ui.view.GiraffeExceptionPreviewView
import com.pokemon.mebius.mebius_widget_recycleview.adapter.MSimpleAdapter
import kotlinx.android.synthetic.main.giraffe_page_exception_list.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class GiraffeExceptionListPage(context: Context) : GiraffeBasePage(context) {

    override fun getLayoutResId() = R.layout.giraffe_page_exception_list

    private val logsAdapter by lazy {
        MSimpleAdapter<GiraffeExceptionInfo>(context).apply {
            registerMapping(GiraffeExceptionInfo::class.java, GiraffeExceptionPreviewView::class.java)
        }
    }

    init {
        setTitle(resources.getString(R.string.giraffe_exception_title2))
        mExceptionLogRv.adapter = logsAdapter
        mExceptionLogRv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
            false
        )
        loadAllData()
        mExceptionLogListSPL.setOnRefreshListener {
            loadAllData()
        }
    }

    private fun loadAllData() {
        GiraffeStorage.getAll(GiraffeExceptionInfo::class.java, orderDesc = true) {
            mExceptionLogListSPL.isRefreshing = false
            if (it.isNotEmpty()) {
                hideEmptyView()
                logsAdapter.data.clear()
                logsAdapter.data.addAll(it)
                logsAdapter.notifyDataSetChanged()
            }else{
                showEmptyView()
            }
        }
    }

}