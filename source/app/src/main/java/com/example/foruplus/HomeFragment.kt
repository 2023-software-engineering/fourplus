package com.example.foruplus

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var dayText: TextView
    private lateinit var editTextSchedule: EditText
    private lateinit var buttonSave: Button

    private lateinit var buttonEdit: Button
    private lateinit var buttonDelete: Button

    private var selectedDate: String = ""
    private var isEditMode: Boolean = false

    private val scheduleMap: MutableMap<String, String> = mutableMapOf()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        calendarView = view.findViewById(R.id.calendarview)
        dayText = view.findViewById(R.id.tv_date)

        editTextSchedule = view.findViewById(R.id.editTextSchedule)
        buttonSave = view.findViewById(R.id.buttonSave)

        buttonEdit = view.findViewById(R.id.buttonEdit)
        buttonDelete = view.findViewById(R.id.buttonDelete)

        buttonEdit.visibility = View.GONE
        buttonDelete.visibility = View.GONE

        // 날짜 형태
        val dateFormat: DateFormat = SimpleDateFormat("yyyy년MM월dd일")

        // date 타입
        val date: Date = Date(calendarView.date)

        selectedDate = dateFormat.format(date)

        // 현재 날짜 담기
        dayText.text = dateFormat.format(date)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date: Date = GregorianCalendar(year, month, dayOfMonth).time
            selectedDate = dateFormat.format(date)
            dayText.text = selectedDate
            loadSchedule(selectedDate)
            toggleEditMode(!scheduleMap.containsKey(selectedDate))
        }

        buttonSave.setOnClickListener {
            val schedule = editTextSchedule.text.toString()
            saveSchedule(selectedDate, schedule)
            Toast.makeText(context, "일정이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            toggleEditMode(false)
            loadSchedule(selectedDate)
        }

        buttonEdit.setOnClickListener {
            toggleEditMode(true)
            loadSchedule(selectedDate)
        }

        buttonDelete.setOnClickListener {
            deleteSchedule(selectedDate)
            Toast.makeText(context, "일정이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            toggleEditMode(true) // 수정: 삭제 버튼을 누르면 입력 가능한 상태로 변경
            loadSchedule(selectedDate)
        }

        return view
    }

    private fun saveSchedule(date: String, schedule: String) {
        scheduleMap[date] = schedule
        val sharedPreferences =
            requireContext().getSharedPreferences("SchedulePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(date, schedule)
        editor.apply()
    }

    private fun deleteSchedule(date: String) {
        scheduleMap.remove(date)
        val sharedPreferences =
            requireContext().getSharedPreferences("SchedulePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(date)
        editor.apply()
    }

    private fun loadSchedule(date: String) {
        val schedule = scheduleMap[date]
        editTextSchedule.setText(schedule)
    }

    private fun clearSchedule() {
        editTextSchedule.text.clear()
    }

    private fun toggleEditMode(editMode: Boolean) {
        isEditMode = editMode
        editTextSchedule.isEnabled = editMode
        buttonSave.visibility =
            if (editMode || !scheduleMap.containsKey(selectedDate)) View.VISIBLE else View.GONE
        buttonEdit.visibility =
            if (editMode || !scheduleMap.containsKey(selectedDate)) View.GONE else View.VISIBLE
        buttonDelete.visibility =
            if (editMode || !scheduleMap.containsKey(selectedDate)) View.GONE else View.VISIBLE

        if (editMode) {
            editTextSchedule.requestFocus()
        } else {
            editTextSchedule.clearFocus()
        }
    }
}
