package dw.seal.shop.ui

import android.Manifest
import android.content.Intent
import android.widget.Button
import dw.seal.shop.BaseActivity

import dw.seal.shop.R
import dw.seal.shop.method.CommonAdapter
import dw.seal.shop.method.CommonViewHolder
import dw.seal.shop.model.BlueToothModel
import dw.seal.shop.model.SignetModel
import kotlinx.android.synthetic.main.activity_seal_check.*
import pub.devrel.easypermissions.EasyPermissions

/**
 * 印章查验
 * */
class SealCheckActivity : BaseActivity(), ISealCheck {
    override fun show_seal(model: SignetModel) {

    }

    override fun initViews() {
        setContentView(R.layout.activity_seal_check)
    }

    var result = false//NFC查验 true.备案入网证明查验 false
    var adapter: CommonAdapter<SignetModel>? = null
    var seal_list: ArrayList<SignetModel> = ArrayList<SignetModel>()
    var now_id = ""//当前扫描的备案入网证明的id
    override fun initEvents() {
        adapter = object : CommonAdapter<SignetModel>(this, seal_list, R.layout.item_seal) {
            override fun convert(holder: CommonViewHolder, model: SignetModel, position: Int) {
                holder.setText(R.id.title_tv, model.signetContent)
                holder.setText(R.id.faren_tv, model.signetSpecification)
                holder.setText(R.id.sp_state_tv, model.signetStatusName)
                holder.setText(R.id.time_tv, model.signetCreateTime)
                when (model.isChecked) {
                    0 -> holder.setVisible(R.id.count_tv, false)
                    1 -> holder.setImageResource(R.id.count_tv, R.drawable.success_icon)
                    2 -> holder.setImageResource(R.id.count_tv, R.drawable.error_icon)
                }
            }
        }
        main_lv.emptyView = error
        main_lv.adapter = adapter
        main_srl.setOnRefreshListener {
            SealCheckListener(this, this).getSealsById(now_id)
        }
        nfc_btn.setOnClickListener {
            startActivityForResult(Intent(this, NFCActivity::class.java).putExtra("key", result), 1)
        }
        blue_btn.setOnClickListener { startActivityForResult(Intent(this, ReadCardActivity::class.java).putExtra("key", result), 2) }
        var name = intent.getStringExtra("name")
        when (name) {
            "NFC查验" -> {
                result = true
            }
            else -> {
                now_id = intent.getStringExtra("id")
                //now_id = "130606880077"
                SealCheckListener(this, this).getSealsById(now_id)
            }
        }
        title_bar.setLeftClick { finish() }
        title_bar.setCentertv(intent.getStringExtra("name"))
        title_bar.setRightClick {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)) {//检查是否获取该权限
                startActivityForResult(Intent(this, SaoActivity::class.java).putExtra("isMain", false), 3)
            } else {
                //2.被拒绝后再次申请该权限的解释.3.参数是请求码 4.申请的权限
                EasyPermissions.requestPermissions(this, "必要的权限", 0, Manifest.permission.CAMERA)
            }
        }
    }

    /**
     * 获得印章列表信息
     * @param list 印章列表数据
     * */
    override fun all_seal(list: ArrayList<SignetModel>) {
        seal_list.clear()
        seal_list = list
        adapter!!.refresh(seal_list)
        main_srl.isRefreshing = false//
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    var model = data!!.getSerializableExtra("model") as SignetModel
                    for (key in seal_list) {
                        //两章的id相同，确认为同一章
                        if (model.signetId == key.signetId) {
                            model.isChecked = 1
                        }
                    }
                    adapter!!.refresh(seal_list)
                    main_srl.isRefreshing = false//
                }
            }
            2 -> {
            }
            3 -> {
                if (data != null) {
                    now_id = data!!.getStringExtra("id")
                    //now_id = "130606880077"
                    SealCheckListener(this, this).getSealsById(now_id)
                }
            }
        }
    }
}
