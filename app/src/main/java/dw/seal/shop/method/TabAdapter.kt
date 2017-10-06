package dw.seal.shop.method

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import dw.seal.shop.fragment.MainFragment
import dw.seal.shop.fragment.MyFragment
import dw.seal.shop.fragment.NewsFragment
import dw.seal.shop.ui.LauncherActivity

/**
 * Created by Administrator on 2017/6/20.
 */
class TabAdapter(fm: android.support.v4.app.FragmentManager, size: Int) : android.support.v4.app.FragmentPagerAdapter(fm) {
    val size = size//当前页数
    override fun getItem(position: Int): android.support.v4.app.Fragment {
        when (position) {
            0 -> return dw.seal.shop.fragment.MainFragment()
            1 -> return dw.seal.shop.fragment.NewsFragment()
            else -> return dw.seal.shop.fragment.MyFragment()

        }
        return null!!
    }

    override fun getCount(): Int {
        return size
    }
}