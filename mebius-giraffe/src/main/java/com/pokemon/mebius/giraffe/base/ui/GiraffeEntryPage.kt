package com.pokemon.mebius.giraffe.base.ui

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pokemon.mebius.commlib.utils.dp2px
import com.pokemon.mebius.commlib.utils.getDrawable
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.base.ui.view.GiraffeMainFeatureView
import com.pokemon.mebius.giraffe.base.ui.view.GiraffeSimpleCardDecoration
import com.pokemon.mebius.giraffe.config.GiraffeMainFeatureInfo
import com.pokemon.mebius.giraffe.ui.page.GiraffeBasePage
import com.pokemon.mebius.mebius_widget_recycleview.adapter.MSimpleAdapter

class GiraffeEntryPage (
    context: Context,
    val defaultSupportFeatures: ArrayList<GiraffeMainFeatureInfo>,
    rightOpeClickCallback: (() -> Unit)? = null
) : GiraffeBasePage(context) {

    private val featuresAdapter by lazy {
        MSimpleAdapter(context, defaultSupportFeatures).apply {
            registerMapping(GiraffeMainFeatureInfo::class.java, GiraffeMainFeatureView::class.java)
        }
    }

    private val rv = RecyclerView(context).apply {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutManager = GridLayoutManager(context, 2)
        addItemDecoration(GiraffeSimpleCardDecoration(5.dp2px, 5.dp2px))
        adapter = featuresAdapter
        setPadding(0, 5.dp2px, 0, 0)
    }

    private val refreshView = SwipeRefreshLayout(context).apply {
        layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                topMargin = ACTION_BAR_HEIGHT
            }
    }


    init {
        refreshView.addView(rv)

        addView(refreshView)
        setTitle("Giraffe")

        background = getDrawable(context, R.color.giraffe_white)

        if (rightOpeClickCallback != null) {
            actionBar.setRightOperate(
                R.drawable.giraffe_icon_entry_quick_function,
                rightOpeClickCallback
            )
        }

        refreshView.setOnRefreshListener {
            postDelayed({
                showToast(context.getString(R.string.giraffe_refresh_tips))
                refreshView.isRefreshing = false
            }, 1500)
        }

    }

    override fun getLayoutResId() = INVALID_RES_ID

}