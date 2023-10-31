package com.pokemon.mebius.giraffe.ui.page

import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.mebius.commlib.utils.hideKeyboard
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.entities.GiraffeHttpLogInfo
import com.pokemon.mebius.giraffe.base.greendao.GiraffeHttpLogInfoDao
import com.pokemon.mebius.giraffe.storage.GiraffeStorage
import com.pokemon.mebius.giraffe.ui.view.GiraffeHttpListPreviewView
import com.pokemon.mebius.mebius_widget_recycleview.adapter.MSimpleAdapter
import kotlinx.android.synthetic.main.giraffe_page_http_log_list.view.mHttpLogListSPL
import kotlinx.android.synthetic.main.giraffe_page_http_log_list.view.mHttpLogRv
import kotlinx.android.synthetic.main.giraffe_page_http_log_list.view.mNetSearchView

class GiraffeHttpLogListPage(context: Context) : GiraffeBasePage(context) {

    private var mIsSearching: Boolean = false
    private var mCurrentOffsetPage: Int = 0
    private val logsAdapter by lazy {
        MSimpleAdapter<GiraffeHttpLogInfo>(context).apply {
            registerMapping(GiraffeHttpLogInfo::class.java, GiraffeHttpListPreviewView::class.java)
        }
    }

    private val mHandler = WithoutLeakHandler(this)

    init {

        layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        setTitle(resources.getString(R.string.giraffe_net_title))
        actionBar.setRightOperate(
            R.drawable.giraffe_icon_entry_quick_function,
            clickCallback = {
                GiraffeStorage.clearNetData { loadOriginData() }
            }
        )
        mHttpLogRv.adapter = logsAdapter
        mHttpLogRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )

        mHttpLogRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val lm = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount: Int? = recyclerView.adapter?.itemCount
                val lastVisibleItemPosition = lm.findLastVisibleItemPosition()
                val visibleItemCount: Int = recyclerView.childCount
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard()
                }

                if (totalItemCount != null) {
                    if (lastVisibleItemPosition >= totalItemCount - 3 && visibleItemCount > 0) {
                        mCurrentOffsetPage++
                        if (mIsSearching) {
                            GiraffeStorage.queryDataByWhereOrCondition(
                                GiraffeHttpLogInfo::class.java,
                                orderDesc = true,
                                count = 100,
                                offset = mCurrentOffsetPage * 100,
                                condition = GiraffeHttpLogInfoDao.Properties.Path.like(
                                    "%" + mNetSearchView.query?.toString()?.lowercase() + "%"
                                ),
                                condition2 = GiraffeHttpLogInfoDao.Properties.Path.like(
                                    "%" + mNetSearchView.query?.toString()?.uppercase() + "%"
                                )
                            ) {
                                appendList(it)
                            }
                            return
                        }

                        //加载更多
                        GiraffeStorage.getAll(
                            GiraffeHttpLogInfo::class.java,
                            orderDesc = true,
                            count = 100,
                            offset = mCurrentOffsetPage * 100
                        ) {
                            appendList(it)
                        }
                    }
                }
            }
        })

        mHttpLogListSPL.isRefreshing = true
        loadOriginData()

        mHttpLogListSPL.setOnRefreshListener {
            mNetSearchView.setQuery("", false)
            hideKeyboard()
            loadOriginData()
        }

        initSearchView()
    }

    private fun initSearchView() {
        mNetSearchView.isIconifiedByDefault = false
        mNetSearchView.queryHint = "可以搜索接口路径关键字"
        mNetSearchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {

                    if (mHandler.hasMessages(SEARCH)) {
                        mHandler.removeMessages(SEARCH)
                    }

                    val message = Message.obtain()
                    message.what = SEARCH
                    message.obj = newText
                    mHandler.sendMessageDelayed(message, 250)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                resetOffsetPage()
                //如果newText不是长度为0的字符串
                if (!TextUtils.isEmpty(query)) {
                    GiraffeStorage.queryDataByWhereOrCondition(
                        GiraffeHttpLogInfo::class.java, orderDesc = true,
                        condition = GiraffeHttpLogInfoDao.Properties.Path.like("%" + query?.lowercase() + "%"),
                        condition2 = GiraffeHttpLogInfoDao.Properties.Path.like("%" + query?.uppercase() + "%"),
                        count = 100,
                        offset = mCurrentOffsetPage * 100
                    ) {
                        resetList(it)
                    }
                } else {
                    loadOriginData()
                }
                return false
            }
        })
    }

    private fun loadOriginData() {
        resetOffsetPage()
        mIsSearching = false
        GiraffeStorage.getAll(
            GiraffeHttpLogInfo::class.java, orderDesc = true,
            count = 100,
            offset = mCurrentOffsetPage * 100
        ) {
            mHttpLogListSPL.isRefreshing = false

            resetList(it)

            if (it.isNotEmpty()) {
                hideEmptyView()
            } else {
                showEmptyView()
            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.giraffe_page_http_log_list

    fun resetList(list: List<GiraffeHttpLogInfo>) {
        logsAdapter.data.clear()
        if (list.isNotEmpty()) {
            logsAdapter.data.addAll(list)
        }
        logsAdapter.notifyDataSetChanged()
        mHttpLogRv.smoothScrollToPosition(0)
    }

    fun appendList(list: List<GiraffeHttpLogInfo>) {
        if (list.isNotEmpty()) {
            logsAdapter.data.addAll(list)
        }
        logsAdapter.notifyDataSetChanged()
    }

    fun resetOffsetPage() {
        mCurrentOffsetPage = 0
    }

    companion object {
        const val SEARCH: Int = 100

        /**
         * desc: 解决handler内存泄漏的问题，消息的处理需要放在内部类的{@link #Handler.handleMessage}
         */
        private class WithoutLeakHandler(page: GiraffeHttpLogListPage) : Handler() {
            private var page = page

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    SEARCH -> {
                        Log.d("sadfsadfasdf", "onQueryTextChange call: $msg.obj.toString()")
                        page.resetOffsetPage()
                        //如果newText不是长度为0的字符串
                        if (!TextUtils.isEmpty(msg.obj.toString())) {
                            page.mIsSearching = true
                            GiraffeStorage.queryDataByWhereOrCondition(
                                GiraffeHttpLogInfo::class.java, orderDesc = true,
                                condition = GiraffeHttpLogInfoDao.Properties.Path.like(
                                    "%" + msg.obj.toString()?.lowercase() + "%"
                                ),
                                condition2 = GiraffeHttpLogInfoDao.Properties.Path.like(
                                    "%" + msg.obj.toString()?.uppercase() + "%"
                                ),
                                count = 100,
                                offset = page.mCurrentOffsetPage * 100
                            ) {
                                page.resetList(it)
                            }
                        } else {
                            page.loadOriginData()
                        }
                    }
                }
            }
        }
    }
}