package dw.seal.shop.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import dw.seal.shop.BaseActivity

import dw.seal.shop.R
import dw.seal.shop.method.CommonAdapter
import dw.seal.shop.method.CommonViewHolder
import dw.seal.shop.method.Utils
import dw.seal.shop.method.key
import dw.seal.shop.method.key.key_bluetooth_address
import dw.seal.shop.method.key.key_bluetooth_name
import dw.seal.shop.model.BlueToothModel
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.ArrayList

/**
 * 选择蓝牙
 * */
class ChoiceBlueToothActivity : BaseActivity() {
    internal var mBtAdapter: BluetoothAdapter? = null
    internal var list_read_card = ArrayList<BlueToothModel>()
    var adapter: CommonAdapter<BlueToothModel>? = null
    override fun initViews() {
        setContentView(R.layout.activity_setting)
        adapter = object : CommonAdapter<BlueToothModel>(this, list_read_card, R.layout.item_blue) {
            override fun convert(holder: CommonViewHolder, model: BlueToothModel, position: Int) {
                holder.setText(R.id.tv, model.name)
                holder.setVisible(R.id.check_iv, model.checked)
            }
        }
        main_lv.adapter = adapter
        main_lv.setOnItemClickListener { parent, view, position, id ->
            Utils.putCache(key_bluetooth_address, list_read_card[position].url)
            Utils.putCache(key_bluetooth_name, list_read_card[position].name)
            setResult(77)
            finish()
        }
        toolbar.setLeftClick { finish() }
    }

    override fun initEvents() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBtAdapter == null) {
            finish()
            return
        } else {//手机存在蓝牙模块
            if (!mBtAdapter!!.isEnabled) {//蓝牙未打开
                val enableIntent = Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableIntent, 1)
            } else {//蓝牙打开状态
                mBtAdapter = BluetoothAdapter.getDefaultAdapter()
                val pairedDevices = mBtAdapter!!.bondedDevices//获得本地已经绑定的蓝牙驱动集合
                for (i in pairedDevices) {
                    var checked = false
                    if (i.address.equals(Utils.getCache(key.key_bluetooth_address))) {
                        checked = true
                    }
                    list_read_card.add(BlueToothModel(i.name, i.address, checked))
                }
                adapter!!.refresh(list_read_card)
            }
        }
    }
}
