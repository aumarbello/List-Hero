package com.aumarbello.motion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.aumarbello.motion.R
import com.aumarbello.motion.data.Link
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_thread.view.*

class LinkAdapter(private val links: List<Link>, private val openDetails: (Link, View) -> Unit) :
    RecyclerView.Adapter<LinkAdapter.LinkHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_thread, parent, false)

        return LinkHolder(view)
    }

    override fun getItemCount() = links.size

    override fun onBindViewHolder(holder: LinkHolder, position: Int) {
        val item = links[position]

        holder.itemView.setOnClickListener {
            openDetails(item, holder.itemView)
        }

        holder.bindItem(item)
    }

    inner class LinkHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(link: Link) = with(itemView) {
            if (link.thumbnail != null) {
                Picasso.get().load(link.thumbnail).into(ivThumbnail)
            } else {
                ivThumbnail.visibility = View.GONE
            }

            tvTitle.text = link.title
            tvSubReddit.text = link.subreddit

            if (link.flair != null) {
                tvFlair.text = link.flair
                tvFlair.visibility = View.VISIBLE
            } else {
                tvFlair.visibility = View.GONE
            }

            tvOriginalContent.isVisible = link.isOriginalContent
            tvDomain.text = link.domain
            tvVoteCount.text = link.votes
            tvCommentCount.text = link.comments
        }
    }
}