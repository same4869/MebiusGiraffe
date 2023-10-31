package com.pokemon.mebius.giraffe.base.ui.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pokemon.mebius.commlib.utils.dp2px

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

class GiraffeSimpleCardDecoration(
    val horizontalGap: Int = CARD_GAP,
    val verticalGap: Int = CARD_GAP
) : RecyclerView.ItemDecoration() {

    companion object {
        val CARD_GAP = 10.dp2px
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        outRect.top = verticalGap
        if (params.spanIndex == 0) {
            outRect.left = horizontalGap
            outRect.right = horizontalGap / 2
        } else {
            outRect.right = horizontalGap
            outRect.left = horizontalGap / 2
        }
    }
}