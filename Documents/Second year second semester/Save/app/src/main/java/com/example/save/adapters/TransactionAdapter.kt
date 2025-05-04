package com.example.save.adapters

//display data from recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.save.databinding.ItemTransactionBinding
import com.example.save.models.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(transaction: Transaction) {
            binding.textViewTitle.text = transaction.title
            binding.textViewAmount.text = String.format("%.2f", transaction.amount)
            binding.textViewCategory.text = transaction.category
            binding.textViewDate.text = dateFormat.format(transaction.date)
            binding.textViewType.text = transaction.type.name

            binding.root.setOnClickListener { onItemClick(transaction) }
            binding.buttonDelete.setOnClickListener { onDeleteClick(transaction) }
        }
    }

    //update RecyclerView efficiently
    private class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }
} 