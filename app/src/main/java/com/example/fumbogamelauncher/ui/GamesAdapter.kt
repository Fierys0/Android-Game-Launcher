package com.example.fumbogamelauncher.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.databinding.ItemGameGridBinding
import com.example.fumbogamelauncher.databinding.ItemGamePosterBinding
import com.example.fumbogamelauncher.model.Game

/**
 * Adapter for displaying games in Steam/Epic style.
 */
class GamesAdapter(
    private val gameList: List<Game>,
    private val viewType: Int = TYPE_GRID
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_GRID = 0
        const val TYPE_POSTER = 1
    }

    class GridViewHolder(val binding: ItemGameGridBinding) : RecyclerView.ViewHolder(binding.root)
    class PosterViewHolder(val binding: ItemGamePosterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int = viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_POSTER) {
            PosterViewHolder(ItemGamePosterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            GridViewHolder(ItemGameGridBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val game = gameList[position]
        val context = holder.itemView.context
        val resId = if (game.imageResName != null) {
            context.resources.getIdentifier(game.imageResName, "drawable", context.packageName)
        } else 0

        if (holder is PosterViewHolder) {
            holder.binding.tvTitle.text = game.title
            if (resId != 0) holder.binding.ivCover.setImageResource(resId)
            else holder.binding.ivCover.setImageResource(R.drawable.placeholder)
        } else if (holder is GridViewHolder) {
            holder.binding.tvTitle.text = game.title
            if (resId != 0) holder.binding.ivCover.setImageResource(resId)
            else holder.binding.ivCover.setImageResource(R.drawable.placeholder)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, GameDetailActivity::class.java).apply {
                putExtra(GameDetailActivity.EXTRA_GAME, game)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = gameList.size
}
