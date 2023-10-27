package com.pokemon.mebius.giraffe.base.common

import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.base.TAG_COMMON
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executor

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/8
 */

object GiraffeAsync {

    fun asyncRun(
        runnable: () -> Unit,
        executor: Executor,
        finishCallback: () -> Unit = {}
    ): Disposable {
        return Observable.create<Unit> {
            try {
                it.onNext(runnable())
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.from(executor)).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                finishCallback()
            }, {
                finishCallback()
                GiraffeLog.e(TAG_COMMON, "asyncRun error ${it.message}")
            })
    }

    fun <T> asyncRunWithResult(
        runnable: () -> T,
        executor: Executor,
        completeCallBack: (result: T) -> Unit
    ): Disposable {
        return Observable.create<T> {
            try {
                it.onNext(runnable()!!)
            } catch (e: Exception) {
                it.onError(e)
            }
            it.onComplete()
        }.subscribeOn(Schedulers.from(executor))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                completeCallBack(it)
            }, {
                GiraffeLog.e(TAG_COMMON, "asyncRun error ${it.message}")
            })
    }

}