package dw.seal.shop.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap

import com.uuzuche.lib_zxing.activity.CodeUtils
import com.uuzuche.lib_zxing.activity.CaptureFragment
import dw.seal.shop.BaseActivity
import dw.seal.shop.R
import kotlinx.android.synthetic.main.activity_sao.*


class SaoActivity : BaseActivity() {
    companion object {
        var main: SaoActivity? = null
    }

    var isMain = false
    override fun initViews() {
        setContentView(R.layout.activity_sao)
        main = this
        isMain = intent.getBooleanExtra("isMain", false)
        val captureFragment = CaptureFragment()
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera)

        captureFragment.analyzeCallback = analyzeCallback
        /**
         * 替换我们的扫描控件
         */
        supportFragmentManager.beginTransaction().replace(R.id.fl_my_container, captureFragment).commit()
    }

    override fun initEvents() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(77)
        finish()

    }

    var analyzeCallback: CodeUtils.AnalyzeCallback = object : CodeUtils.AnalyzeCallback {
        override fun onAnalyzeSuccess(mBitmap: Bitmap, result: String) {
            //toast(result)
            var id = ""
            var ids = result.split("=")
            if (ids.size > 1) {
                id = ids[1]
            }
            //true查询备案入网信息
            if (isMain) {
                startActivity(Intent(this@SaoActivity, SealCheckActivity::class.java)
                        .putExtra("id", id)
                        .putExtra("name", "备案入网证明查验"))
                finish()
            } else {//扫了回调
                setResult(77, Intent().putExtra("id", id))
                finish()
            }
        }

        override fun onAnalyzeFailed() {
            toast("扫码失败，请重试")
        }
    }
}
