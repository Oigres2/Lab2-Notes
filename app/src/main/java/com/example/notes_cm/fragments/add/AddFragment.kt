package com.example.notes_cm.fragments.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notes_cm.R
import com.example.notes_cm.data.entities.Note
import com.example.notes_cm.data.vm.NoteViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date
import java.util.*
import java.text.SimpleDateFormat

class AddFragment : Fragment() {
    private lateinit var mNoteViewModel: NoteViewModel
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        ViewModelProvider(this)[NoteViewModel::class.java].also { this.mNoteViewModel = it }

        val button = view.findViewById<Button>(R.id.save)
        button.setOnClickListener {
            addNote()
        }

        val backButton = view.findViewById<Button>(R.id.backToList)
        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }

        val selectDateButton = view.findViewById<Button>(R.id.btn_select_date)
        selectDateButton.setOnClickListener {
            showDatePickerDialog()
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

    private fun addNote() {
        val noteText = view?.findViewById<EditText>(R.id.addNote)?.text.toString()

        if (noteText.isEmpty()) {
            Toast.makeText(view?.context, getString(R.string.note_empty_error), Toast.LENGTH_LONG).show()
        } else {
            val note = Note(0, noteText, selectedDate)
            mNoteViewModel.addNote(note)
            Toast.makeText(requireContext(), getString(R.string.note_saved_success), Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }
    }
}