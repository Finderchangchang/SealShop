package dw.seal.shop.ui

import android.content.Intent
import android.graphics.Color

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import android.widget.TextView
import dw.seal.shop.BaseActivity
import dw.seal.shop.R
import kotlinx.android.synthetic.main.activity_launcher.*
import android.view.WindowManager
import android.os.Build
import android.widget.ImageView
import android.widget.LinearLayout


/**
 * 启动页--左右滑动的效果
 * */
class LauncherActivity : BaseActivity() {
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun initViews() {
        setContentView(R.layout.activity_launcher)
        main = this
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        container!!.adapter = mSectionsPagerAdapter
    }

    companion object {
        var main: LauncherActivity? = null
    }

    override fun initEvents() {
        /**
         * 设置状态栏透明效果
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            val localLayoutParams = window.attributes
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags
        }
    }

    class PlaceholderFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.frag_launcher, container, false)
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
                    title_iv.setBackgroundColor(main!!.resources.getColor(R.color.colorAccent))
                    btn.visibility = View.VISIBLE
                    btn.setOnClickListener {
                        main!!.finish()
                        startActivity(Intent(main, TabActivity::class.java))
                    }
                }
                2 -> {
                    title_iv.setBackgroundColor(main!!.resources.getColor(R.color.design_bottom_navigation_shadow_color))
                    btn.visibility = View.GONE
                }
                else -> {
                    title_iv.setBackgroundColor(main!!.resources.getColor(R.color.colorPrimary))
                    btn.visibility = View.GONE
                }
            }
            return rootView
        }

        companion object {
            private val ARG_SECTION_NUMBER = "section_number"
            fun newInstance(sectionNumber: Int): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return PlaceholderFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "0"
                1 -> return "1"
                2 -> return "2"
            }
            return null
        }
    }
}
