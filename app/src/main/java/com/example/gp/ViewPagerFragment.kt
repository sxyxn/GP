package com.example.gp

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class ViewPagerFragment : Fragment(R.layout.viewpager) {

    private lateinit var viewPager: ViewPager2
    private lateinit var seekBar: SeekBar
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        seekBar = view.findViewById(R.id.seekBar)

        val images = listOf(R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3)
        viewPagerAdapter = ViewPagerAdapter(images)
        viewPager.adapter = viewPagerAdapter

        val totalSlides = viewPagerAdapter.itemCount

        // ViewPager 페이지 변화에 따라 SeekBar 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                // 현재 슬라이드에 맞춰 SeekBar 진행도 업데이트
                val progress = ((position + positionOffset) / (totalSlides - 1) * 100).toInt()
                seekBar.progress = progress

                // 마지막 슬라이드에 도달하면 SeekBar를 끝으로 설정
                if (position == totalSlides - 1) {
                    seekBar.progress = seekBar.max
                }
            }
        })

        // 사용자가 SeekBar를 조작했을 때 슬라이드로 이동
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val position = (progress / 100f * (totalSlides - 1)).toInt()
                    viewPager.setCurrentItem(position, true)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // 필요 시 구현
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // 필요 시 구현
            }
        })
    }
}
