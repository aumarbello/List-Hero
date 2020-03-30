package com.aumarbello.motion.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.aumarbello.motion.R
import com.aumarbello.motion.data.Link
import com.google.android.material.transition.MaterialContainerTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(R.layout.fragment_second) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform(requireContext()).apply {
            scrimColor = Color.TRANSPARENT
            containerColor = Color.TRANSPARENT
        }

        sharedElementReturnTransition = MaterialContainerTransform(requireContext()).apply {
            scrimColor = Color.TRANSPARENT
            containerColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val link = arguments?.getSerializable("item_link") as Link? ?: return
        ViewCompat.setTransitionName(view, link.id)

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
