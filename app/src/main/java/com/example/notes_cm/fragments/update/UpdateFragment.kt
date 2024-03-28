package com.example.notes_cm.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes_cm.R
import com.example.notes_cm.data.entities.Note
import com.example.notes_cm.data.vm.NoteViewModel
import java.util.Date
import java.util.*
import java.text.SimpleDateFormat
import com.google.android.material.datepicker.MaterialDatePicker

class UpdateFragment : Fragment() {
    private val args by navArgs<UpdateFragmentArgs>()
    private lateinit var mNoteViewModel: NoteViewModel
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mNoteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        selectedDate = args.currentNote.date

        val noteTextView = view.findViewById<TextView>(R.id.updateNote)
        noteTextView.text = args.currentNote.note

        val updateButton = view.findViewById<Button>(R.id.update)
        updateButton.setOnClickListener {
            updateNote()
        }

        val selectDateButton = view.findViewById<Button>(R.id.btn_select_date)
        selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        val deleteButton = view.findViewById<Button>(R.id.delete)
        deleteButton.setOnClickListener {
            deleteNote()
        }

        val backButton = view.findViewById<Button>(R.id.backToListFromUpdate)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }

        return view
    }

    private fun showDatePickerDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selection))
        }
        datePicker.show(childFragmentManager, "datePicker")
    }

    private fun updateNote() {
        val noteText = view?.findViewById<EditText>(R.id.updateNote)?.text.toString()

        if (noteText.isEmpty()) {
            Toast.makeText(context, getString(R.string.empty_note_error), Toast.LENGTH_LONG).show()
        } else {
            val updatedNote = Note(args.currentNote.id, noteText, selectedDate)
            mNoteViewModel.updateNote(updatedNote)
            Toast.makeText(requireContext(), getString(R.string.note_updated_success), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
    }

    private fun deleteNote() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            mNoteViewModel.deleteNote(args.currentNote)
            Toast.makeText(requireContext(), getString(R.string.note_deleted_success), Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
        builder.setTitle(getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_confirmation))
        builder.create().show()
    }
}