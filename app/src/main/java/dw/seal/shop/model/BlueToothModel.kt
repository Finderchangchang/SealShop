package dw.seal.shop.model

/**
 * Created by Administrator on 2017/6/16.
 */
class BlueToothModel {
    var name = ""
    var url = ""
    var checked: Boolean = false

    constructor(name: String, url: String, checked: Boolean) {
        this.name = name
        this.url = url
        this.checked = checked
    }
}