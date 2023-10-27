package com.pokemon.mebius.giraffe.ui

interface GiraffePageProtocol {
    var eventListener: PageEventListener?

    interface PageEventListener {
        fun onBack()
    }

    fun setEntryParams(params: Any) {
    }
}