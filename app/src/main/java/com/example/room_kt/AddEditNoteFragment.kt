package com.example.room_kt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.room_kt.databinding.FragmentAddEditNoteBinding
import kotlinx.coroutines.launch

class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {
    private lateinit var binding: FragmentAddEditNoteBinding
    private lateinit var viewModel: NoteViewModel

    val args: AddEditNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddEditNoteBinding.bind(view)

        val db = NoteDatabaseInstance.getDatabase(requireContext())
        val repo = NoteRepository(db.noteDao())
        viewModel = ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]

        // Pre-fill if updating
        if (args.noteId != -1) {
            binding.etTitle.setText(args.noteTitle)
            binding.etContent.setText(args.noteContent)
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(
                    id = if (args.noteId != -1) args.noteId else 0,
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )

                lifecycleScope.launch {
                    if (args.noteId != -1) {
                        viewModel.updateNote(note)
                    } else {
                        viewModel.insertNote(note)
                    }
                    findNavController().popBackStack()
                }
            }
        }
    }
}