package dw.seal.shop.ui

import android.content.Context
import android.content.Intent
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import dw.seal.shop.callback.DialogCallback
import dw.seal.shop.callback.JsonCallback
import dw.seal.shop.callback.LzyResponse
import dw.seal.shop.method.Utils
import dw.seal.shop.model.Seal
import dw.seal.shop.model.SealModel
import dw.seal.shop.model.SignetModel
import okhttp3.Call
import okhttp3.Response
import java.lang.Exception

/**
 * Created by Administrator on 2017/9/7.
 */
interface ISealCheck {
    /**
     * 获得印章列表信息
     * @param list 印章列表数据
     * */
    fun all_seal(list: ArrayList<SignetModel>)

    fun show_seal(model: SignetModel)

}

class SealCheckListener(main: ISealCheck,context: SealCheckActivity) {
    var main = main//获得接口
    var context=context;
    /**
     * 根据备案入网证明的id获得印章列表
     * @param id 备案入网证明id
     * */
    fun getSealsById(id: String) {
        var list: ArrayList<SignetModel> = ArrayList()
        OkGo.post("http://www.hbyzcx.com/Views/Handler/GetCorpSignets.ashx?CorpId=" + id)//
                .execute(object : DialogCallback<LzyResponse<SignetModel>>(context) {
                    override fun onSuccess(stringLzyResponse: LzyResponse<SignetModel>, call: Call, response: Response) {
//                        if (stringLzyResponse.isSuccess) {
                        list = stringLzyResponse.signet as ArrayList<SignetModel>
                        main.all_seal(list)
//                        }
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })

    }

    fun getSignetId(id: String) {

        var list: ArrayList<SignetModel> = ArrayList()
        OkGo.post("http://www.hbyzcx.com/Views/Handler/GetCorpSignets.ashx?SignetId=" + id)//
                .execute(object : JsonCallback<LzyResponse<SignetModel>>() {
                    override fun onSuccess(stringLzyResponse: LzyResponse<SignetModel>, call: Call, response: Response) {
                        //if (stringLzyResponse.isSuccess) {
                        list = stringLzyResponse.signet as ArrayList<SignetModel>
                        if (list != null && list.size > 0) {
                            main.show_seal(list[0])
                        }
                        //}
                    }

                    override fun onError(call: Call?, response: Response?, e: Exception?) {
                        super.onError(call, response, e)
                    }
                })

    }
}