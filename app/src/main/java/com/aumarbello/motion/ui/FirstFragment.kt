package com.aumarbello.motion.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.aumarbello.motion.R
import com.aumarbello.motion.data.Link
import com.google.android.material.transition.Hold
import kotlinx.android.synthetic.main.fragment_first.*
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(R.layout.fragment_first) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       rvThreadList.adapter = LinkAdapter(getLinks(), this::openDetails)
    }

    private fun openDetails(link: Link, view: View) {
        (requireActivity() as MainActivity).openDetails(view, link)
    }

    private fun getLinks(): List<Link> {
        val inputStream = requireContext().assets.open("data.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)

        inputStream.read(buffer)
        inputStream.close()

        val payload = JSONObject(String(buffer))
        val children = payload.getJSONObject("data").getJSONArray("children")
        val items = arrayListOf<Link>()
        var pos = 0
        while (pos < children.length()){
            val jsonObject = children.getJSONObject(pos).getJSONObject("data")

            val link = Link(
                id = jsonObject.getString("name"),
                thumbnail =      jsonObject.getString("thumbnail").enforceNullability(),
                title = jsonObject.getString("title"),
                subreddit = jsonObject.getString("subreddit"),
                flair = jsonObject.getString("link_flair_text").enforceNullability(),
                isOriginalContent = jsonObject.getBoolean("is_original_content"),
                domain = jsonObject.getString("domain"),
                votes = jsonObject.getInt("score").formatCount(),
                comments = jsonObject.getInt("num_comments").formatCount()
            )

            items.add(link)
            pos++
        }

        return items
    }

    private fun Int.formatCount(): String {
        return toLong().formatCount()
    }

    private fun Long.formatCount(): String {
        val suffixes = TreeMap<Long, String>().apply {
            put(1_000L, "k")
            put(1_000_000L, "M")
        }

        return when {
            this == Long.MIN_VALUE -> return (this + 1).formatCount()
            this < 0 -> "-${(this.unaryMinus().formatCount())}"
            this < 1000 -> toString()
            else -> {
                val entry = suffixes.floorEntry(this) ?: return ""
                val dividedBy = entry.key
                val suffix = entry.value
                val truncatedVal = this / (dividedBy / 10)
                val hasDecimal =
                    (truncatedVal < 100) && (truncatedVal / 10f) == (truncatedVal / 10).toFloat()

                val decimalValue =
                    if (hasDecimal) "${(truncatedVal / 10f)}" else "${(truncatedVal / 10)}"

                return "$decimalValue$suffix"
            }
        }
    }

    private fun String.enforceNullability(): String? {
        return if (this == "null")
            null
        else
            this
    }
}
