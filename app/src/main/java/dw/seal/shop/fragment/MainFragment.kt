package dw.seal.shop.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dw.seal.shop.R

/**
 * Created by Administrator on 2017/6/20.
 */
class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.frag_main, container, false)
        val textView = rootView.findViewById(R.id.section_label) as TextView
        val btn = rootView.findViewById(R.id.close_btn) as Button
        var frag_ll = rootView.findViewById(R.id.frag_ll) as LinearLayout
        var title_iv = rootView.findViewById(R.id.title_iv) as ImageView
        var num = "o"
        val cc = "$num"
        textView.text = "首页$num"
        return rootView
    }
}