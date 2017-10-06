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
import dw.seal.shop.ui.TabActivity

/**
 * Created by Administrator on 2017/6/20.
 */
class MyFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.frag_my, container, false)
        val textView = rootView.findViewById(R.id.section_label) as TextView
        val btn = rootView.findViewById(R.id.close_btn) as Button
        val num = arguments.getInt(ARG_SECTION_NUMBER)
        var frag_ll = rootView.findViewById(R.id.frag_ll) as LinearLayout
        var title_iv = rootView.findViewById(R.id.title_iv) as ImageView
        textView.text = getString(R.string.section_format, num)
        /**
         * 用来适配5.0以下版本效果
         * */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            frag_ll.fitsSystemWindows = true
            frag_ll.clipToPadding = false
        }
        when (num) {
            3 -> {
                title_iv.setBackgroundColor(TabActivity.main!!.resources.getColor(R.color.colorAccent))
                btn.visibility = View.VISIBLE
                btn.setOnClickListener {
                    TabActivity.main!!.finish()
                    startActivity(Intent(TabActivity.main, TabActivity::class.java))
                }
            }
            2 -> {
                title_iv.setBackgroundColor(TabActivity.main!!.resources.getColor(R.color.design_bottom_navigation_shadow_color))
                btn.visibility = View.GONE
            }
            else -> {
                title_iv.setBackgroundColor(TabActivity.main!!.resources.getColor(R.color.colorPrimary))
                btn.visibility = View.GONE
            }
        }
        return rootView
    }

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(sectionNumber: Int): MyFragment {
            val fragment = MyFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }
}