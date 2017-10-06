package dw.seal.shop.ui

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import dw.seal.shop.BaseActivity

import dw.seal.shop.R
import kotlinx.android.synthetic.main.activity_start.*
import pub.devrel.easypermissions.EasyPermissions

/**
 * 启动页
 * */
class StartActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {
    override fun initViews() {
        setContentView(R.layout.activity_start)
        //NFC识别
        nfc_login_btn.setOnClickListener {
            startActivity(Intent(this, NFCActivity::class.java).putExtra("is_start",true))
            finish()
        }
        blue_login_btn.setOnClickListener {
            startActivity(Intent(this, ReadCardActivity::class.java).putExtra("is_start",true))
            finish()
        }
        //扫描备案入网证明
        scan_login_btn.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {//检查是否获取该权限
                skipScanner()
            } else {
                //2.被拒绝后再次申请该权限的解释.3.参数是请求码 4.申请的权限
                EasyPermissions.requestPermissions(this, "必要的权限", 0, Manifest.permission.CAMERA)
            }
        }
    }

    override fun initEvents() {

    }

    /**
     *跳转到扫一扫页面
     * */
    fun skipScanner() {
        startActivity(Intent(this, SaoActivity::class.java).putExtra("isMain", true))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //分别返回授权成功
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        skipScanner()
    }

    //授权失败的权限
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        toast("您拒绝了相机权限，无法进行扫一扫操作")
    }
}
