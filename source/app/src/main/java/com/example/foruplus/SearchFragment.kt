package com.example.foruplus

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment() {

    private lateinit var scheduleTextView: TextView
    private lateinit var selectYearButton: Button
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        scheduleTextView = view.findViewById(R.id.tv_schedule)
        selectYearButton = view.findViewById(R.id.btn_select_year)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectYearButton.setOnClickListener {
            showDatePickerDialog()
        }

        displaySchedule()
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker, selectedYear: Int, selectedMonth: Int, _: Int ->
                // Update calendar with selected year and month
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                displaySchedule()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun displaySchedule() {
        val sharedPreferences = requireContext().getSharedPreferences("SchedulePrefs", Context.MODE_PRIVATE)
        val scheduleMap = sharedPreferences.all as Map<String, String>
        val sortedScheduleMap = scheduleMap.toSortedMap()

        val selectedYear = calendar.get(Calendar.YEAR)
        val selectedMonth = calendar.get(Calendar.MONTH) + 1 // Month is zero-based, so add 1

        val scheduleText = StringBuilder()

        for ((date, schedule) in sortedScheduleMap) {
            val dateFormat = SimpleDateFormat("yyyy년MM월dd일", Locale.getDefault())
            val parsedDate = dateFormat.parse(date)
            val scheduleYear = parsedDate?.let { it.year + 1900 }
            val scheduleMonth = parsedDate?.let { it.month + 1 } // Month is zero-based, so add 1

            if (scheduleYear == selectedYear && scheduleMonth == selectedMonth) {
                scheduleText.append("$date: $schedule\n")
            }
        }

        if (scheduleText.isEmpty()) {
            scheduleText.append("해당하는 일정이 존재하지 않습니다.")
        }

        scheduleTextView.text = scheduleText.toString()
    }
}
