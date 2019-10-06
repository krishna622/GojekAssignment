package com.krishna.gojekassignment.ui.adapter

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.graphics.toColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.krishna.gojekassignment.R
import com.krishna.gojekassignment.ui.model.TrendDataItem
import kotlinx.android.synthetic.main.trending_list_item.view.*
import java.lang.Exception

class MyTrendingRecyclerViewAdapter() :
    RecyclerView.Adapter<MyTrendingRecyclerViewAdapter.ViewHolder>() {
    private var mValues: List<TrendDataItem> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.trending_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(mValues.isEmpty()){
            setProgressBarAnimation(holder.placeHolderFirstRow)
            setProgressBarAnimation(holder.placeHolderSecondRow)
        }else {
            val item = mValues[position]
            holder.mAvatarProgress.visibility = View.GONE
            holder.placeHolderFirstRow.visibility = View.GONE
            holder.placeHolderSecondRow.visibility = View.GONE
            holder.placeHolderFirstRow.clearAnimation()
            holder.placeHolderSecondRow.clearAnimation()
            holder.mAvatarImg.visibility = View.VISIBLE
            holder.mName.visibility = View.VISIBLE
            holder.mAuthor.visibility = View.VISIBLE

            holder.mAuthor.text = item.author
            holder.mName.text = item.name
            holder.mLanguage.text = item.language

            holder.mStars.text = item.stars.toString()
            holder.mFork.text = item.forks.toString()
            val desc = "${item.description}(${item.url})"
            holder.mDescription.text = desc
            Glide
                .with(holder.mView)
                .load(item.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mAvatarImg);

            holder.mBottomView.visibility = if (item.isExpend) View.VISIBLE else View.GONE
            with(holder.mView) {
                setOnClickListener {
                    changeStateOfItemsInLayout(item, position)
                    notifyDataSetChanged()
                }
            }

            try {
                var drawable = context.resources.getDrawable(R.drawable.circle)
                var transparentDrawable = context.resources.getDrawable(R.drawable.transparent)
                var color = if (item.languageColor != null) item.languageColor else "#00ADD8"
                drawable.setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN)
                holder.mLanguage.setCompoundDrawablesWithIntrinsicBounds(
                    drawable,
                    transparentDrawable,
                    transparentDrawable,
                    transparentDrawable
                )
            } catch (ex: Exception) {
                holder.mLanguage.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.circle,
                    0,
                    0,
                    0
                )
            }
        }
    }

    override fun getItemCount(): Int = if(mValues.size==0) 8 else mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mAuthor: TextView = mView.author
        val mName: TextView = mView.name
        val mBottomView: Group = mView.group_bottom
        val mAvatarImg: ImageView = mView.imageView
        val mLanguage: TextView = mView.language
        val mStars: TextView = mView.rated
        val mFork: TextView = mView.fork
        val mDescription: TextView = mView.description
        val mAvatarProgress = mView.imageView1
        val placeHolderFirstRow = mView.place_holder_first_row
        val placeHolderSecondRow = mView.place_holder_second_row
    }

    private fun changeStateOfItemsInLayout(trendDataItem: TrendDataItem, p: Int) {
        for (i in 0 until mValues.size) {
            if (i == p) {
                trendDataItem.isExpend = !trendDataItem.isExpend
                continue
            }
            with(mValues) { get(i).isExpend = false }
        }
    }

    fun setTrendingData(trendDataItem: List<TrendDataItem>) {
        this.mValues = trendDataItem
        notifyDataSetChanged()
    }

    private fun setProgressBarAnimation(progressBar: ProgressBar) {
        val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100)
        animation.duration = 1000
        animation.repeatCount = ObjectAnimator.INFINITE// 3.5 second
        animation.repeatMode = ObjectAnimator.REVERSE
        animation.interpolator = LinearInterpolator()
        animation.start()
    }
}
