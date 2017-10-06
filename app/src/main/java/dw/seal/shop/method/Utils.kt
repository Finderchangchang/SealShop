package dw.seal.shop.method

import dw.seal.shop.App

import android.content.Context.MODE_PRIVATE

/**
 * Created by Administrator on 2016/5/19.
 */
object Utils {


    /***
     * MD5加码 生成32位md5码
     */
    fun string2MD5(inStr: String): String {
        var md5: java.security.MessageDigest? = null
        try {
            md5 = java.security.MessageDigest.getInstance("MD5")
        } catch (e: Exception) {
            println(e.toString())
            e.printStackTrace()
            return ""
        }

        val charArray = inStr.toCharArray()
        val byteArray = ByteArray(charArray.size)

        for (i in charArray.indices)
            byteArray[i] = charArray[i].toByte()
        val md5Bytes = md5!!.digest(byteArray)
        val hexValue = StringBuffer()
        for (i in md5Bytes.indices) {
            val `val` = md5Bytes[i].toInt() and 0xff
            if (`val` < 16)
                hexValue.append("0")
            hexValue.append(Integer.toHexString(`val`))
        }
        return hexValue.toString()

    }

    fun setDialog(cont: String, sure: dw.seal.shop.method.Utils.setSure, cancle: dw.seal.shop.method.Utils.setCancle): android.app.Dialog {
        return dw.seal.shop.method.Utils.setDialog("提示", cont, "确定", "取消", sure, cancle)
    }

    fun setDialog(cont: String, sure: dw.seal.shop.method.Utils.setSure): android.app.Dialog {
        return dw.seal.shop.method.Utils.setDialog("提示", cont, "确定", "取消", sure, null)
    }

    /**
     * 设置TotalListView(自定义)的高度

     * @param listView
     */
    fun setListViewHeight(listView: android.widget.ListView) {
        // 获取ListView对应的Adapter
        val listAdapter = listView.adapter ?: return
        var totalHeight = 0
        for (i in 0..listAdapter.count - 1) { // listAdapter.getCount()返回数据项的数目
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0) // 计算子项View 的宽高
            totalHeight += listItem.measuredHeight // 统计所有子项的总高度
        }
        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.layoutParams = params
    }

    fun setDialog(title: String, cont: String, sure_str: String, cancle_str: String, sure: dw.seal.shop.method.Utils.setSure, cancle: dw.seal.shop.method.Utils.setCancle?): android.app.Dialog {
        val localBuilder1 = android.app.AlertDialog.Builder(App.context)
                .setTitle(title).setMessage(cont)
        return localBuilder1.setPositiveButton(cancle_str
        ) { dialog, which ->
            cancle?.click(null)
        }.setNegativeButton(sure_str) { paramDialogInterface, paramInt -> sure.click(null) }.create()
    }

    interface setSure {
        fun click(view: android.view.View?)
    }

    interface setCancle {
        fun click(view: android.view.View?)
    }

    fun isMobileNo(mobiles: String): Boolean {
        val p = java.util.regex.Pattern.compile("[1][34578]\\d{9}")
        val m = p.matcher(mobiles)
        return m.matches()
    }

    fun putCache(map: Map<String, String>) {
        val sp = dw.seal.shop.App.Companion.context!!.getSharedPreferences("waichangepwd", MODE_PRIVATE)
        val editor = sp.edit()
        rx.Observable.from(map.keys)
                .map<Any> { maa ->
                    editor.putString(maa, map[maa])
                    null
                }
                .subscribe { nv -> editor.commit() }
    }

    fun putCache(key: String, `val`: String) {
        val sp = dw.seal.shop.App.Companion.context!!.getSharedPreferences("grclass", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString(key, `val`)
        editor.commit()
    }

    fun putBooleanCache(key: String, `val`: Boolean) {
        val sp = dw.seal.shop.App.Companion.context!!.getSharedPreferences("grclass", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(key, `val`)
        editor.commit()
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态

     * @return true 表示网络可用
     */
    // 当前网络是连接的
    // 当前所连接的网络可用
    val isNetworkAvailable: Boolean
        get() {
            val connectivity = dw.seal.shop.App.Companion.context!!
                    .getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.activeNetworkInfo
                if (info != null && info.isConnected) {
                    if (info.state == android.net.NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
            return false
        }

    val isWifiEnabled: Boolean
        get() {
            val mgrConn = dw.seal.shop.App.Companion.context!!
                    .getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val mgrTel = dw.seal.shop.App.Companion.context!!
                    .getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            return mgrConn.activeNetworkInfo != null && mgrConn
                    .activeNetworkInfo.state == android.net.NetworkInfo.State.CONNECTED || mgrTel
                    .networkType == android.telephony.TelephonyManager.NETWORK_TYPE_UMTS
        }

    fun getCache(key: String): String {
        val sharedPreferences = dw.seal.shop.App.Companion.context!!.getSharedPreferences("grclass", MODE_PRIVATE)
        return sharedPreferences.getString(key, "")
    }

    fun getBooleanCache(key: String): Boolean {
        val sharedPreferences = dw.seal.shop.App.Companion.context!!.getSharedPreferences("grclass", MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }


    /*
    * 获取当前程序的版本号
    */
    //获取packagemanager的实例
    //getPackageName()是你当前类的包名，0代表是获取版本信息
    val version: String
        get() {
            val packageManager = dw.seal.shop.App.Companion.context!!.packageManager
            var packInfo: android.content.pm.PackageInfo? = null
            try {
                packInfo = packageManager.getPackageInfo(dw.seal.shop.App.Companion.context!!.packageName, 0)
            } catch (e: android.content.pm.PackageManager.NameNotFoundException) {

            }

            return packInfo!!.versionName
        }

    /**
     * 加载当前时间。
     * 1.同一年的显示格式 05-11  07:45
     * 2.前一年或者更多格式 2015-11-12

     * @param old
     * *
     * @return 需要显示的处理结果
     */
    fun loadTime(old: String): String {
        val old_year = old.substring(0, 4)//获得old里面的年
        val now_year = java.text.SimpleDateFormat("yyyy").format(java.util.Date()).substring(0, 4)//获得当前的年
        if (old_year == now_year) {//两者为同一年
            return old.substring(5, 16)
        } else {
            return old.substring(0, 10)
        }
    }

    /**
     * 检查网络连接状态

     * @return 连接成功
     */
    val isNetworkConnected: Boolean
        get() {
            val mConnectivityManager = dw.seal.shop.App.Companion.context!!
                    .getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
            return false
        }

    fun compressImage(image: android.graphics.Bitmap): android.graphics.Bitmap {

        val baos = java.io.ByteArrayOutputStream()
        image.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset()//重置baos即清空baos
            image.compress(android.graphics.Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        val isBm = java.io.ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        val bitmap = android.graphics.BitmapFactory.decodeStream(isBm, null, null)//把ByteArrayInputStream数据生成图片
        return bitmap
    }

    /**
     *                                                                       
     *    * @param bitmap      原图
     *    * @param edgeLength  希望得到的正方形部分的边长
     *    * @return  缩放截取正中部分后的位图。
     *    
     */

    fun centerSquareScaleBitmap(bitmap: android.graphics.Bitmap?, edgeLength: Int): android.graphics.Bitmap? {
        if (null == bitmap || edgeLength <= 0) {
            return null
        }
        var result: android.graphics.Bitmap = bitmap
        val widthOrg = bitmap.width
        val heightOrg = bitmap.height
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            val longerEdge = (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg)).toInt()
            val scaledWidth = if (widthOrg > heightOrg) longerEdge else edgeLength
            val scaledHeight = if (widthOrg > heightOrg) edgeLength else longerEdge
            val scaledBitmap: android.graphics.Bitmap
            try {
                scaledBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
            } catch (e: Exception) {
                return null
            }

            //从图中截取正中间的正方形部分。
            val xTopLeft = (scaledWidth - edgeLength) / 2
            val yTopLeft = (scaledHeight - edgeLength) / 2
            try {
                result = android.graphics.Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength)
                scaledBitmap.recycle()
            } catch (e: Exception) {
                return null
            }

        }
        return result
    }


    /**
     * 获取当前应用的版本号：
     */
    // 获取packagemanager的实例
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    val versionName: String
        get() {
            val Version = "[Version:num]-[Registe:Mobile]"
            val packageManager = dw.seal.shop.App.Companion.context!!.packageManager
            val packInfo: android.content.pm.PackageInfo
            try {
                packInfo = packageManager.getPackageInfo(dw.seal.shop.App.Companion.context!!.packageName, 0)
                val version = packInfo.versionName
                return Version.replace("num", version)
            } catch (e: android.content.pm.PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return Version.replace("num", "1.0")
        }

    /**
     * 获得当前系统时间

     * @return String类型的当前时间
     */
    //设置日期格式
    val normalTime: String
        get() {
            val df = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return df.format(java.util.Date())
        }

    /**
     * 获得当前时间 yyyy/MM/dd HH:mm:ss

     * @return String类型的当前时间
     */
    //设置日期格式
    val now: String
        get() {
            val df = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return df.format(java.util.Date())
        }

    //将20160302210101转换为yyyy-MM-dd HH:mm:ss
    fun DataTimeTO(time: String): String {
        val df = java.text.SimpleDateFormat("yyyyMMddHHmmss")
        val dfstr = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")//设置日期格式

        var date: java.util.Date? = null
        try {
            date = df.parse(time)
            return dfstr.format(date)
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
            return ""
        }

    }

    /**
     * 读取xml文件

     * @param FileName 文件名
     * *
     * @return 文件内容
     */
    fun getAssetsFileData(FileName: String): String {
        var str = ""
        try {
            val `is` = dw.seal.shop.App.Companion.context!!.assets.open(FileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            str = String(buffer)
        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }

        return str
    }

    //base64 string转换为bitmap
    fun getBitmapByte(str: String): android.graphics.Bitmap {
        var bitmap: android.graphics.Bitmap? = null
        try {
            val buffer = android.util.Base64.decode(str.toByteArray(), android.util.Base64.DEFAULT)
            if (buffer != null && buffer.size > 0) {
                bitmap = android.graphics.BitmapFactory.decodeByteArray(buffer, 0, buffer.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap!!
    }

    /**
     * 获得屏幕高度宽度

     * @return Point对象 point.x宽度。point.y高度
     */
    val scannerPoint: android.graphics.Point
        get() {
            val windowManager = dw.seal.shop.App.Companion.context!!.getSystemService(android.content.Context.WINDOW_SERVICE) as android.view.WindowManager
            val point = android.graphics.Point()
            windowManager.defaultDisplay.getSize(point)
            return point
        }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getViewPoint(view: android.view.View): android.graphics.Point {
        val point = android.graphics.Point()
        view.display.getSize(point)
        return point
    }

    //获取当前时间的hhmmssfff
    //yyyymmddhhmmssfff
    val qingqiuma: String
        get() {
            val ts = java.sql.Timestamp(System.currentTimeMillis())
            println(ts.toString())
            var str = ts.toString().replace(":", "").replace("", "").replace("-", "").replace(" ", "")
            if (str.length < 16) {
                str = str.substring(0)
            } else if (str.length < 17) {
                str = str.substring(1)
            } else {
                str = str.substring(2)
            }
            return str
        }

    /**
     * 比较时间的大小str1小返回true

     * @param str1   起始时间
     * *
     * @param str2   结束时间
     * *
     * @param islong true,长时间串
     * *
     * @return
     */
    fun DateCompare(str1: String, str2: String, islong: Boolean): Boolean {
        val df: java.text.DateFormat
        if (islong) {
            df = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        } else {
            df = java.text.SimpleDateFormat("yyyy-MM-dd")
        }
        val c1 = java.util.Calendar.getInstance()
        val c2 = java.util.Calendar.getInstance()
        try {
            c1.time = df.parse(str1)
            c2.time = df.parse(str2)
        } catch (e: java.text.ParseException) {
            System.err.println("格式不正确")
            return false
        }

        val result = c1.compareTo(c2)
        if (result == 0) {
            //System.out.println("c1相等c2");
            return true
        } else if (result >= 0) {
            return false
            //System.out.println("c1小于c2");
        } else {
            // System.out.println("c1大于c2");
            return true
        }
    }


    fun getAssetsFileData(context: android.content.Context, FileName: String): String {
        var str = ""
        try {
            val `is` = context.assets.open(FileName)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            str = String(buffer)
        } catch (e: java.io.IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return str
    }

    /**
     * 对URL进行编码操作

     * @param text
     * *
     * @return
     */
    fun URLEncodeImage(text: String): String {
        if (dw.seal.shop.method.Utils.isEmptyString(text))
            return ""
        try {
            return java.net.URLEncoder.encode(text, "UTF-8")
        } catch (e: java.io.UnsupportedEncodingException) {
            return ""
        }

    }

    /**
     * 判断字符串是否为空,为空返回空串
     * http://bbs.3gstdy.com

     * @param text
     * *
     * @return
     */
    fun URLEncode(text: String): String {
        if (dw.seal.shop.method.Utils.isEmptyString(text))
            return ""
        if (text == "null")
            return ""
        return text
    }

    /**
     * 判断字符串是否为空
     * http://bbs.3gstdy.com

     * @param str
     * *
     * @return
     */
    fun isEmptyString(str: String?): Boolean {
        return str == null || str.length == 0
    }

    /**
     * 将图片bitmap转换为base64字符串
     * http://bbs.3gstdy.com

     * @param bitmap
     * *
     * @return 根据url读取出的图片的Bitmap信息
     */
    fun encodeBitmap(bitmap: android.graphics.Bitmap): String {
        try {
            val baos = java.io.ByteArrayOutputStream()
            bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 80, baos)
            return android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT)
                    .trim { it <= ' ' }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    val allChar = "0123456789"

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)

     * @param length 随机字符串长度
     * *
     * @return 随机字符串
     */
    fun getRandomChar(length: Int): String {
        val sb = StringBuffer()
        val random = java.util.Random()
        for (i in 0..length - 1) {
            sb.append(dw.seal.shop.method.Utils.allChar[random.nextInt(dw.seal.shop.method.Utils.allChar.length)])
        }
        return sb.toString()
    }


    /**
     * 获取系统的当前日期，格式为YYYYMMDD
     */
    val systemNowDate: String
        get() {
            val calendar = java.util.Calendar.getInstance()
            val year = calendar.get(java.util.Calendar.YEAR)
            val monthOfYear = calendar.get(java.util.Calendar.MONTH) + 1
            var monthStr = monthOfYear.toString()
            if (monthStr.length < 2) {
                monthStr = "0" + monthStr
            }
            val dayOfMonth = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            var dayStr = dayOfMonth.toString()
            if (dayStr.length < 2) {
                dayStr = "0" + dayStr
            }
            return year.toString() + monthStr + dayStr
        }

    /**
     * 带参数的跳页

     * @param cla      需要跳转到的页面
     * *
     * @param listener 传参的接口
     */
    @JvmOverloads fun IntentPost(cla: Class<*>, listener: dw.seal.shop.method.Utils.putListener? = null) {
        val intent = android.content.Intent()
        intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        intent.setClass(dw.seal.shop.App.Companion.context, cla)
        listener?.put(intent)
        dw.seal.shop.App.Companion.context!!.startActivity(intent)
    }

    /**
     * 判断ip地址是否符合格式（10.0.3.2）

     * @param ip 需要检测的ip地址
     * *
     * @return 是否符合规定，true为符合。
     */
    fun checkIP(ip: String): Boolean {
        if (dw.seal.shop.method.Utils.getContainSize(ip, "") == 3 && ip.length >= 7) {
            return true
        } else {
            return false
        }
    }

    /**
     * 获得key在val中存在的个数

     * @param val 字符串
     * *
     * @param key 包含在key中的某字符
     * *
     * @return 存在的个数
     */
    fun getContainSize(`val`: String, key: String): Int {
        if (`val`.contains(key)) {
            val length = `val`.length - `val`.replace(key, "").length
            if (length > 0) {
                return length
            }
        }
        return 0
    }

    /**
     * 加载本地图片
     * http://bbs.3gstdy.com

     * @param url
     * *
     * @return 根据url读取出的图片的Bitmap信息
     */
    fun getBitmapByFile(url: String?): android.graphics.Bitmap? {
        if (url !== "" && url != null) {
            try {
                val fis = java.io.FileInputStream(url)
                return android.graphics.BitmapFactory.decodeStream(fis)
            } catch (e: java.io.FileNotFoundException) {
                return null
            }

        } else {
            return null
        }
    }

    /**
     * @param bitmap     原图
     * *
     * @param edgeLength 希望得到的正方形部分的边长
     * *
     * @return 缩放截取正中部分后的位图。
     */
    fun centerImageBitmap(bitmap: android.graphics.Bitmap?, edgeLength: Int): android.graphics.Bitmap? {
        if (null == bitmap || edgeLength <= 0) {
            return null
        }
        var result: android.graphics.Bitmap = bitmap
        val widthOrg = bitmap.width
        val heightOrg = bitmap.height
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            val longerEdge = (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg)).toInt()
            val scaledWidth = if (widthOrg > heightOrg) longerEdge else edgeLength
            val scaledHeight = if (widthOrg > heightOrg) edgeLength else longerEdge
            val scaledBitmap: android.graphics.Bitmap
            try {
                scaledBitmap = android.graphics.Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
            } catch (e: Exception) {
                return null
            }

            //从图中截取正中间的正方形部分。
            val xTopLeft = (scaledWidth - edgeLength) / 2
            val yTopLeft = (scaledHeight - edgeLength) / 2
            try {
                result = android.graphics.Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength)
                scaledBitmap.recycle()
            } catch (e: Exception) {
                return null
            }

        }
        return result
    }

    /**
     * 截取指定字符串并添加并在后面添加...

     * @param val    截取前的字符串
     * *
     * @param length 截取字符长度
     * *
     * @return 处理之后的结果
     */
    fun cutStringToDian(`val`: String, length: Int): String {
        if (`val`.length >= length) {
            return `val`.substring(0, length) + "..."
        } else {
            return `val`
        }
    }

    //得到手机的imei
    val imei: String
        get() = (dw.seal.shop.App.Companion.context!!
                .getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager).deviceId

    /**
     * 获得当前手机的手机号码

     * @return
     */
    val phoneNum: String
        get() {
            val phoneMgr = dw.seal.shop.App.Companion.context!!.getSystemService(android.content.Context.TELEPHONY_SERVICE) as android.telephony.TelephonyManager
            try {
                return phoneMgr.line1Number
            } catch (ex: Exception) {
                return ""
            }

        }

    /**
     * 跳页传参的接口
     */
    interface putListener {
        fun put(intent: android.content.Intent)
    }
}
/**
 * 不带参数的跳页

 * @param cla 需要跳转到的页面
 */
