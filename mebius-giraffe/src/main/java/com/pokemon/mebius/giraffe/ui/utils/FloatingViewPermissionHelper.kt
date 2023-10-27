package com.pokemon.mebius.giraffe.ui.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.pokemon.mebius.giraffe.ui.utils.device.MeizuUtils
import com.pokemon.mebius.giraffe.ui.utils.device.UIDeviceUtils
import com.pokemon.mebius.giraffe.base.GiraffeLog
import com.pokemon.mebius.giraffe.ui.GiraffeUi

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/6/9
 */

object FloatingViewPermissionHelper {

    /**
     * check permission is ok?
     *
     * @param context
     * @return permission status
     */
    fun checkPermission(context: Context): Boolean {
        return commonROMPermissionCheck(context)
    }

    /**
     * apply permission
     */
    fun requestFloatingWindowPermission(context: Context) {
        if (UIDeviceUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    commonROMPermissionApplyInternal(context)
                } catch (e: Exception) {
                    GiraffeLog.getStackTraceString(e)
                }

            }
        }
    }


    /**
     * apply meizu permission
     */
    private fun meizuROMPermissionApply(context: Context) {
        MeizuUtils.applyPermission(context)
    }

    fun commonROMPermissionApplyInternal(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("package:" + context.packageName)
        context.startActivity(intent)
    }

    /**
     * show confirm diaGiraffeLog
     */
    fun showConfirmDialog(context: Activity, result: OnConfirmResult) {
        val diaGiraffeLog = AlertDialog.Builder(context)
            .setCancelable(GiraffeUi.mConfig.isDialogCancel)
            .setTitle("")
            .setMessage("开启悬浮窗权限后可以自动打开 Giraffe，是否要打开?")
            .setPositiveButton(
                "现在去开启"
            ) { diaGiraffeLog, which ->
                result.confirmResult(true)
                diaGiraffeLog.dismiss()
            }.setNegativeButton(
                "暂不开启"
            ) { diaGiraffeLog, which ->
                result.confirmResult(false)
                diaGiraffeLog.dismiss()
            }.create()
        diaGiraffeLog.show()
    }

    interface OnConfirmResult {
        fun confirmResult(confirm: Boolean)
    }

    /**
     * check meizu permission
     */
    private fun meizuPermissionCheck(context: Context): Boolean {
        return MeizuUtils.checkFloatWindowPermission(context)
    }

    /**
     * check common permission
     */
    private fun commonROMPermissionCheck(context: Context): Boolean {
        if (UIDeviceUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context)
        } else {
            var result: Boolean? = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    val clazz = Settings::class.java
                    val canDrawOverlays =
                        clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                    result = canDrawOverlays.invoke(null, context) as Boolean
                } catch (e: Exception) {
                    GiraffeLog.getStackTraceString(e)
                }

            }
            return result ?: true
        }
    }

}