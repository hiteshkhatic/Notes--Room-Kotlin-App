package com.example.room_kt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.room_kt.Note

class NoteAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var noteList = listOf<Note>()

    fun submitList(notes: List<Note>) {
        noteList = notes
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val content = view.findViewById<TextView>(R.id.tvContent)

        init {
            view.setOnClickListener {
                onNoteClick(noteList[adapterPosition])
            }
            view.setOnLongClickListener{
                onDeleteClick(noteList[adapterPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.title.text = note.title
        holder.content.text = note.content
    }

    override fun getItemCount(): Int = noteList.size
}
