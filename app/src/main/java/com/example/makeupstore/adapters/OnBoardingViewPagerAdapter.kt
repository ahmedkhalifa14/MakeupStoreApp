package com.example.makeupstore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.makeupstore.ui.onboarding.OnBoardingFragment.Companion.MAX_STEP
import com.example.makeupstore.R
import com.example.makeupstore.databinding.SplashScreenItemBinding

class OnBoardingViewPagerAdapter : RecyclerView.Adapter<PagerVH2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH2 {
        return PagerVH2(
            SplashScreenItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = MAX_STEP

    override fun onBindViewHolder(holder: PagerVH2, position: Int) =

        holder.itemView.run {

            with(holder) {
                if (position == 0) {
                    bindingDesign.introTitle.text = context.getString(R.string.intro_title_1)
                    bindingDesign.introDescription.text =
                        context.getString(R.string.intro_description_1)
                    bindingDesign.introImage.setImageResource(R.drawable.m4)
                }
                if (position == 1) {
                    bindingDesign.introTitle.text = context.getString(R.string.intro_title_2)
                    bindingDesign.introDescription.text =
                        context.getString(R.string.intro_description_2)
                    bindingDesign.introImage.setImageResource(R.drawable.m1)
                }
                if (position == 2) {
                    bindingDesign.introTitle.text = context.getString(R.string.intro_title_3)
                    bindingDesign.introDescription.text =
                        context.getString(R.string.intro_description_3)
                    bindingDesign.introImage.setImageResource(R.drawable.m2)
                }
                if (position == 3) {
                    bindingDesign.introTitle.text = context.getString(R.string.intro_title_4)
                    bindingDesign.introDescription.text =
                        context.getString(R.string.intro_description_4)
                    bindingDesign.introImage.setImageResource(R.drawable.m3)
                }
            }
        }
}

class PagerVH2(val bindingDesign: SplashScreenItemBinding) : RecyclerView.ViewHolder(bindingDesign.root)