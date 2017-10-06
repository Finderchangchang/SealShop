package dw.seal.shop.ui

import android.content.Intent
import android.support.design.widget.BottomNavigationView
import dw.seal.shop.BaseActivity
import dw.seal.shop.R
import dw.seal.shop.method.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_tab.*
import android.support.v4.view.ViewPager
import android.view.MenuItem
import dw.seal.shop.method.TabAdapter


/**
 * 首页
 * */
class TabActivity : BaseActivity() {
    private var mSectionsPagerAdapter: TabAdapter? = null
    /**
     * 初始化页面内容
     * */
    override fun initViews() {
        setContentView(R.layout.activity_tab)
        main = this
        mSectionsPagerAdapter = TabAdapter(supportFragmentManager, 3)
        vp!!.adapter = mSectionsPagerAdapter
    }

    var prevMenuItem: MenuItem? = null

    /**
     * 加载页面逻辑
     * */
    override fun initEvents() {
        /**
         * 底部菜单点击触发事件
         * */
        navigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    vp.currentItem = 0
                    message.setText(R.string.title_home)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    vp.currentItem = 1
                    message!!.setText(R.string.title_dashboard)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
//                    vp.currentItem = 3
                    message!!.setText(R.string.title_notifications)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem!!.isChecked = false;
                } else {
                    navigation.menu.getItem(0).isChecked = false;
                }
                navigation.menu.getItem(position).isChecked = true;
                prevMenuItem = navigation.menu.getItem(position);
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        BottomNavigationViewHelper.disableShiftMode(navigation)
        /**
         * 查验按钮点击事件
         * */
        cy_btn.setOnClickListener { startActivity(Intent(this, SealCheckActivity::class.java)) }
    }

    companion object {
        var main: TabActivity? = null
    }
}
