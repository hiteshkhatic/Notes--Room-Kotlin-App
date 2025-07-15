package com.example.room_kt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room_kt.R
import com.example.room_kt.databinding.FragmentHome2Binding
import com.example.room_kt.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home2) {
    private lateinit var adapter: NoteAdapter
    private lateinit var viewModel: NoteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHome2Binding.bind(view)

        // ✅ 1. Initialize adapter first
        adapter = NoteAdapter(
            onNoteClick = { note ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToAddEditNoteFragment(note.id, note.title, note.content)
                findNavController().navigate(action)
            },
            onDeleteClick = { note ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Note?")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes") { _, _ ->
                        lifecycleScope.launch { viewModel.deleteNote(note) }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )


        // ✅ 2. Set up RecyclerView
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.noteRecyclerView.setHasFixedSize(true)
        binding.noteRecyclerView.adapter = adapter

        // ✅ 3. Add test note AFTER adapter is initialized
        val fakeNote = Note(
            title = "Test Note",
            content = "This is a test",
            timestamp = System.currentTimeMillis()
        )
        adapter.submitList(listOf(fakeNote))

        // ✅ 4. Add note button nav
        binding.btnAddNote.setOnClickListener {
            findNavController().navigate(R.id.addEditNoteFragment)
        }

        // ✅ 5. ViewModel + data flow
        val db = NoteDatabaseInstance.getDatabase(requireContext())
        val repo = NoteRepository(db.noteDao())
        val factory = NoteViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]

        lifecycleScope.launchWhenStarted {
            viewModel.allNotes.collect { notes ->
                adapter.submitList(notes)
            }
        }
    }
}
