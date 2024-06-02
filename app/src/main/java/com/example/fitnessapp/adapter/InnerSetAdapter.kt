import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.WorkoutSet
import com.example.fitnessapp.data.WorkoutViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InnerSetAdapter(
    private var list: List<WorkoutSet>,
    private val viewModel: WorkoutViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<InnerSetAdapter.InnerViewHolder>() {

    private var updateDebounceJob: Job? = null

    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSetNumber: TextView = itemView.findViewById(R.id.setNumber)
        val prevSet: TextView = itemView.findViewById(R.id.previousSetTV)
        val editTextKg: EditText = itemView.findViewById(R.id.etKG)
        val editTextRep: EditText = itemView.findViewById(R.id.etREP)
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.setCheckBox)

        fun bind(set: WorkoutSet) {
            tvSetNumber.text = set.setId.toString()
            editTextKg.setText(set.currentKg.toString())
            editTextRep.setText(set.currentReps.toString())
            completedCheckBox.isChecked = set.isCompleted

            // Remove previous text watchers if they exist
            editTextKg.removeTextChangedListener(editTextKg.tag as? TextWatcher)
            editTextRep.removeTextChangedListener(editTextRep.tag as? TextWatcher)

            val kgTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val kgValue = s?.toString()?.toIntOrNull() ?: 0
                    if (set.currentKg != kgValue) {
                        updateDebounceJob?.cancel()
                        updateDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
                            delay(300)
                            set.currentKg = kgValue
                            viewModel.updateCurrentSet(set)
                        }
                    }
                }
            }

            val repsTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val repsValue = s?.toString()?.toIntOrNull() ?: 0
                    if (set.currentReps != repsValue) {
                        updateDebounceJob?.cancel()
                        updateDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
                            delay(300)
                            set.currentReps = repsValue
                            viewModel.updateCurrentSet(set)
                        }
                    }
                }
            }

            editTextKg.addTextChangedListener(kgTextWatcher)
            editTextKg.tag = kgTextWatcher

            editTextRep.addTextChangedListener(repsTextWatcher)
            editTextRep.tag = repsTextWatcher

            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                set.isCompleted = isChecked
                viewModel.updateCurrentSet(set)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inner_rv_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val currentSet = list[position]
        holder.bind(currentSet)
    }

    fun setData(newData: List<WorkoutSet>) {
        list = newData
        notifyDataSetChanged()
    }
}

