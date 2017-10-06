package dw.seal.shop

import android.app.ProgressDialog
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

/**
 * BaseActivity声明相关通用方法
 *
 *
 * Created by LiuWeiJie on 2015/7/22 0022.
 * Email:1031066280@qq.com
 */
abstract class BaseActivity : AppCompatActivity() {
    internal var dialog: ProgressDialog? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        initViews()
        initEvents()
    }

    abstract fun initViews()

    abstract fun initEvents()

    private var toast: Toast? = null

    fun toast(msg: String) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast!!.show()
    }
}
