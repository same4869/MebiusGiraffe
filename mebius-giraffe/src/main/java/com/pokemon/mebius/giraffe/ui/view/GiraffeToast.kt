package com.pokemon.mebius.giraffe.ui.view

/**
 * Toast工具类，利用悬浮窗功能来代替Toast
 */
object GiraffeToast {
    private val historyList = ArrayList<ToastHistory>()

    private val toastInfo = ArrayList<ToastInfo>()

    private var toastProvider: ToastProvider? = null

    val history: List<ToastHistory>
        get() {
            return historyList
        }

    private fun showToast(value: ToastInfo) {
        toastInfo.add(value)
        historyList.add(0, ToastHistory(System.currentTimeMillis(), value))
        checkToast()
    }

    fun showToast(value: String) {
        showToast(ToastInfo(value, ""))
    }

    fun showToast(value: String, expand: String) {
        showToast(ToastInfo(value, expand))
    }

    fun bindProvider(provider: ToastProvider) {
        this.toastProvider = provider
        checkToast()
    }

    fun unbindProvider() {
        this.toastProvider = null
    }

    private fun checkToast() {
        toastProvider?.let {
            val tempList = ArrayList<ToastInfo>()
            tempList.addAll(toastInfo)
            toastInfo.clear()
            it.onNewToast(tempList)
        }
    }

    interface ToastProvider {
        fun onNewToast(toastList: ArrayList<ToastInfo>)
    }

    data class ToastHistory(
        val time: Long,
        val info: ToastInfo
    )

    data class ToastInfo(
        val message: String,
        val expand: String
    ) {

        val toastValue: String by lazy {
            if (expand.isEmpty()) {
                message
            } else {
                "◎$message"
            }
        }

    }
}