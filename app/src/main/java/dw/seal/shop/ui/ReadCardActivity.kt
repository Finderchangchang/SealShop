package dw.seal.shop.ui

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import dw.seal.shop.BaseActivity

import dw.seal.shop.R
import dw.seal.shop.method.bluetooth_read.BluetoothChatService
import dw.seal.shop.method.bluetooth_read.CHexConver
import dw.seal.shop.method.Utils
import dw.seal.shop.method.key
import dw.seal.shop.model.SignetModel
import dw.seal.shop.model.UU
import dw.seal.shop.model.UU.HexStringToBinary
import kotlinx.android.synthetic.main.activity_read_card.*
import java.io.UnsupportedEncodingException
import kotlin.experimental.or

class ReadCardActivity : BaseActivity() {
    private var mConnectedDeviceName: String? = null
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var mChatService: BluetoothChatService? = null
    internal var Key = "FFFFFFFFFFFF"//秘钥
    internal var StartCard = "3C"//初始块
    internal var CardCount = "01"//块数量
    internal var i = 0
    internal var izt = 0
    var is_start = false
    override fun initViews() {
        setContentView(R.layout.activity_read_card)
        is_start = intent.getBooleanExtra("is_start", false)
        if (is_start) {//隐藏核验按钮
            title_bar.set_no_left_iv(true)
            hy_btn.visibility = View.INVISIBLE
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            snackbar("本机无蓝牙模块,读取证件等相关功能无法使用！")
            finish()
            return
        }
        //初始化蓝牙读取卡片信息
        mChatService = BluetoothChatService(this, mHandler)
        if (mChatService!!.getState() !== BluetoothChatService.STATE_CONNECTED) {
            val blue_address = Utils.getCache(key.key_bluetooth_address)//获得本地存储的蓝牙地址
            if (!TextUtils.isEmpty(blue_address)) {
                val device = mBluetoothAdapter!!.getRemoteDevice(blue_address)
                mChatService!!.connect(device)
            } else {
                //toast("当前设备不存在，请在设置页面重新设置读卡设备")
                startActivityForResult(Intent(this@ReadCardActivity, ChoiceBlueToothActivity::class.java), 0)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            77 -> {
                if (mChatService!!.state !== BluetoothChatService.STATE_CONNECTED) {
                    val device = mBluetoothAdapter!!.getRemoteDevice(Utils.getCache(key.key_bluetooth_address))
                    mChatService!!.connect(device)
                }
            }
        }
    }

    fun read_card() {
        var sml = "000A2000" + CardCount + StartCard + Key
        val sjy = CHexConver.calcCheckSum(sml)
        sml = "02" + sml + sjy + "03"
        izt = 1
        try {
            sendMessage(sml)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    override fun initEvents() {
        title_bar.setLeftClick { finish() }
        //跳转到设置页面，连接蓝牙
        title_bar.setRightClick {
            startActivityForResult(Intent(this@ReadCardActivity, ChoiceBlueToothActivity::class.java), 0)
        }
        title_bar.setCenterClick {
            when (title_bar.center_Tv.text) {
                "未连接" -> {
                    if (mChatService!!.getState() !== BluetoothChatService.STATE_CONNECTED) {
                        val device = mBluetoothAdapter!!.getRemoteDevice(Utils.getCache(key.key_bluetooth_address))
                        mChatService!!.connect(device)
                    }
                }
                else -> {
                    read_card()
                }
            }
        }
        hy_btn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("model", SignetModel())
            setResult(66, intent)
            finish()
        }
        floatingActionButton.setOnClickListener {
            read_card()
        }
//        open_btn.setOnClickListener {
//            if (open_btn.text == "读卡") {
//                var sml = "000A2000" + CardCount + StartCard + Key
//                val sjy = CHexConver.calcCheckSum(sml)
//                sml = "02" + sml + sjy + "03"
//                izt = 1
//                try {
//                    sendMessage(sml)
//                } catch (e: UnsupportedEncodingException) {
//                    e.printStackTrace()
//                }
//
//            } else if (open_btn.text == "连接") {
//                if (mChatService!!.getState() !== BluetoothChatService.STATE_CONNECTED) {
//                    val device = mBluetoothAdapter!!.getRemoteDevice(Utils.getCache(key.key_bluetooth_address))
//                    mChatService!!.connect(device)
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mChatService!!.stop()
    }

    override fun onStart() {
        super.onStart()
        if (!mBluetoothAdapter!!.isEnabled) {
            val enableIntent = Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT)
        } else {
            if (mChatService == null)
                mChatService = BluetoothChatService(this, mHandler)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mChatService!!.state === BluetoothChatService.STATE_NONE) {
            mChatService!!.start()
        }
    }

    /**
     * Sends a message.

     * @param message A string of text to send.
     * *
     * @throws UnsupportedEncodingException
     */
    @Throws(UnsupportedEncodingException::class)
    private fun sendMessage(message: String) {
        if (mChatService!!.getState() !== BluetoothChatService.STATE_CONNECTED) {
            snackbar("请连接设备")
            return
        }
        if (message.length > 0) {
            val send = CHexConver.hexStr2Bytes(message.toUpperCase())
            mChatService!!.write(send)
        }
    }

    internal var zzc: String = ""
    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MESSAGE_STATE_CHANGE -> when (msg.arg1) {
                    BluetoothChatService.STATE_CONNECTED -> {
//                        toolbar.setCenterTitle("连接到：" + mConnectedDeviceName)
                        title_bar.setCentertv("连接到：" + mConnectedDeviceName)
                        floatingActionButton.visibility = View.VISIBLE
                        //open_btn.text = "读卡"
                    }
                    BluetoothChatService.STATE_CONNECTING -> {//toolbar.setCenterTitle("连接中...")
                    }
                    BluetoothChatService.STATE_NONE -> {
                        if (mConnectedDeviceName != null) {
                            if (mConnectedDeviceName!!.length > 0) {
                                //toolbar.setCenterTitle("当前记录设备 ：" + mConnectedDeviceName)
                            } else {
                                //toolbar.setCenterTitle("当前记录设备 ：无")
                            }
                        }
                    }
                }
                MESSAGE_READ -> {
                    val s3 = mChatService!!.byte2HexStr(
                            msg.obj as ByteArray, msg.arg1)
                    val readMessage = StringBuilder(s3).toString()
                    zzc += readMessage
                    if (zzc.substring(0, 2) == "02" && zzc.substring(zzc.length - 2, zzc.length) == "03") {
                        if (izt == 1) {
                            var sszzc = zzc.replace(" ", "")
                            toast("读卡成功")
//                            title_tv.text = "国家标准芯片密码网络印章样章"
//                            faren_tv.text = ""
//                            //sp_state_tv.setText("已交付");
//                            time_tv.setText("2012-06-18")
                            message_ll.visibility = View.VISIBLE
                            top1_ll.visibility=View.GONE
                            //单位专用章|42行政章|国家标准芯片密码网络印章样章||保定市网络印章制作中心
                            // 2012-06-18|保定市新型芯片密码网络印章制作中心|保定市公安局|832137|
                            val cardnum = java.lang.String(HexStringToBinary(sszzc), "ascii")

                            if (sszzc.substring(6, 8) == "00") {

                                val cardnum = zzc.substring(16, sszzc.length - 4)
                                try {
                                    var cardid = java.lang.String(CHexConver.HexStringToBinary(cardnum), "ascii") as String
                                    if (cardid.contains("*") && !TextUtils.isEmpty(cardid)) {
                                        cardid = cardid.split("\\*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                                        var s = ""
                                    }
                                } catch (e: UnsupportedEncodingException) {
                                    e.printStackTrace()
                                }

                            } else {

                            }
                        } else {
                            var s = ""
                        }
                        //izt = 0
                        //zzc = ""
                    }
                    i++
                }
                MESSAGE_DEVICE_NAME -> mConnectedDeviceName = msg.data.getString(DEVICE_NAME)
                MESSAGE_TOAST -> {
                    if (msg.data.getString(TOAST) == "Unable to connect device") {
                        title_bar.setCentertv("未连接")
                        floatingActionButton.visibility = View.GONE
                        //open_btn.setText("连接")
                    }
//                    Address = ""
                    mConnectedDeviceName = ""
                }
            }
        }
    }

    companion object {
        val MESSAGE_STATE_CHANGE = 1
        val MESSAGE_READ = 2 // 对方设备的数据
        val MESSAGE_WRITE = 3 // 本机数据
        val MESSAGE_DEVICE_NAME = 4
        val MESSAGE_TOAST = 5
        val REQUEST_CONNECT_DEVICE = 1
        val REQUEST_ENABLE_BT = 2
        val DEVICE_NAME = "device_name"
        val TOAST = "toast"
    }

    fun snackbar(msg: String) {
        Snackbar.make(title_bar, msg, Snackbar.LENGTH_SHORT).show()
    }
}
