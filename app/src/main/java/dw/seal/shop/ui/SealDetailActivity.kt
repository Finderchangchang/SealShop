package dw.seal.shop.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import dw.seal.shop.BaseActivity

import dw.seal.shop.R
import dw.seal.shop.model.SignetModel
import kotlinx.android.synthetic.main.activity_seal_detail.*

/**
 * 印章详情页
 * */
class SealDetailActivity : BaseActivity() {
    var model: SignetModel? = null
    override fun initViews() {
        setContentView(R.layout.activity_seal_detail)
        model = intent.getSerializableExtra("model") as SignetModel
    }

    override fun initEvents() {
        title_bar.setLeftClick { finish() }
        shop_name_tv.text = "单位名称：" + model!!.corpName
        shop_type_tv.text = "单位类型：" + model!!.corpTypeName
        seal_content_tv.text = "印章内容：" + model!!.signetContent
        seal_id_tv.text = "印章编号：" + model!!.signetId
        seal_state_tv.text = "印章状态：" + model!!.signetStatusName
        seal_type_tv.text = "印章类型：" + model!!.signetTypeName
        beian_type_tv.text = "备案类型：" + model!!.signetTypeName
        var time = model!!.signetCreateTime
        if (!TextUtils.isEmpty(time)) {
            time = time.substring(0, 10)
        }
        beian_time_tv.text = "备案时间：" + time
    }
}
