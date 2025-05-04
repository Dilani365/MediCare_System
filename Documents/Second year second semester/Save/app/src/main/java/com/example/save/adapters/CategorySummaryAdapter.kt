package com.example.save.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.save.R


//recyclerview


class CategorySummaryAdapter : ListAdapter<CategorySummaryAdapter.CategorySummary, CategorySummaryAdapter.ViewHolder>(CategoryDiffCallback()) {

    data class CategorySummary(
        val category: String,
        val amount: Double,
        val percentage: Double,
        val currency: String
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_summary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewCategory: TextView = itemView.findViewById(R.id.textViewCategory)
        private val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
        private val textViewPercentage: TextView = itemView.findViewById(R.id.textViewPercentage)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

        fun bind(item: CategorySummary) {
            textViewCategory.text = item.category
            textViewAmount.text = when (item.currency) {
                "USD" -> "$%.2f"
                "EUR" -> "€%.2f"
                "GBP" -> "£%.2f"
                "JPY" -> "¥%.0f"
                "AUD" -> "A$%.2f"
                "CAD" -> "C$%.2f"
                "LKR" -> "Rs %.2f"
                else -> "$%.2f"
            }.format(item.amount)
            textViewPercentage.text = "%.1f%%".format(item.percentage)
            progressBar.progress = item.percentage.toInt()
        }
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<CategorySummary>() {
        override fun areItemsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: CategorySummary, newItem: CategorySummary): Boolean {
            return oldItem == newItem
        }
    }
} 