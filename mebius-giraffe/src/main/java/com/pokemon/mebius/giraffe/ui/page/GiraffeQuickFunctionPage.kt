package com.pokemon.mebius.giraffe.ui.page

import android.content.Context
import com.pokemon.mebius.commlib.utils.onClick
import com.pokemon.mebius.giraffe.BuildConfig
import com.pokemon.mebius.giraffe.R
import com.pokemon.mebius.giraffe.storage.GiraffeStorage
import kotlinx.android.synthetic.main.giraffe_page_quick_function.view.*

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/7/6
 */

class GiraffeQuickFunctionPage(context: Context) : GiraffeBasePage(context) {
    override fun getLayoutResId(): Int = R.layout.giraffe_page_quick_function

    init {
        setTitle(resources.getString(R.string.giraffe_quick_title))
        initQuickClear()
    }

    private fun initQuickClear() {
        mGiraffeQuickPageClearAllDataBtn.onClick {
            GiraffeStorage.clearAllData()
            showToast(resources.getString(R.string.giraffe_clear_log))
        }

        mGiraffeQuickPageViewAbout.text = resources.getString(R.string.giraffe_about_log) +"(version: ${BuildConfig.VERSION_CODE})"
        mGiraffeQuickPageViewAbout.onClick {
            showToast("emmmm.....")
        }
    }
}