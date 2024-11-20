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

    private var isLastSlide = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        seekBar = view.findViewById(R.id.seekBar)

        val images = listOf(R.drawable.slide_1, R.drawable.slide_2, R.drawable.slide_3)
        viewPagerAdapter = ViewPagerAdapter(images)
        viewPager.adapter = viewPagerAdapter

        val totalSlides = viewPagerAdapter.itemCount
        seekBar.max = 100 // SeekBar 최대값 설정 (0 ~ 100)


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)


                val progress = ((position + positionOffset) / (totalSlides - 1) * 100).toInt()
                seekBar.progress = progress
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                isLastSlide = position == totalSlides - 1
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)


                if (isLastSlide && state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    navigateToLoginScreen()
                }
            }
        })

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val position = (progress / 100f * (totalSlides - 1)).toInt()
                    viewPager.setCurrentItem(position, true)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }


    private fun navigateToLoginScreen() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}
