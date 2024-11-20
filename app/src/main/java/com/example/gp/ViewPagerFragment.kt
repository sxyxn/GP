package com.example.gp

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

class ViewPagerFragment : Fragment(R.layout.viewpager) {

    private lateinit var viewPager: ViewPager2
    private lateinit var seekBar: SeekBar
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var isLastSlide = false // 마지막 슬라이드 여부 저장

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        seekBar = view.findViewById(R.id.seekBar)

        val images = listOf(R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3)
        viewPagerAdapter = ViewPagerAdapter(images)
        viewPager.adapter = viewPagerAdapter

        val totalSlides = viewPagerAdapter.itemCount
        seekBar.max = 100 // SeekBar 최대값 설정 (0 ~ 100)

        // ViewPager2 페이지 스크롤 시 SeekBar 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                // SeekBar 진행도 업데이트
                val progress = ((position + positionOffset) / (totalSlides - 1) * 100).toInt()
                seekBar.progress = progress
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 현재 페이지가 마지막 슬라이드인지 확인
                isLastSlide = position == totalSlides - 1
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                // 마지막 슬라이드에서 추가 이동 감지
                if (isLastSlide && state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    navigateToLoginScreen()
                }
            }
        })

        // SeekBar 변경 시 ViewPager2 이동
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // 진행도를 페이지 인덱스로 변환하여 ViewPager2로 이동
                    val position = (progress / 100f * (totalSlides - 1)).toInt()
                    viewPager.setCurrentItem(position, true)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    // 로그인 화면으로 이동하는 함수
    private fun navigateToLoginScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}
