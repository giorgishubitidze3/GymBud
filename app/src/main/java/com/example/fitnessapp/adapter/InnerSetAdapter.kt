import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
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
        val overlay : View = itemView.findViewById(R.id.overlay)

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

            // Handle Enter key on etKG to move to etRep
//            editTextKg.setOnEditorActionListener { _, actionId, event ->
//                if (actionId == EditorInfo.IME_ACTION_NEXT || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
//                    editTextRep.requestFocus()
//                    true
//                } else {
//                    false
//                }
//            }

            editTextKg.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_NULL) {
                    editTextRep.requestFocus()
                    return@setOnEditorActionListener true
                }
                false
            }

            // Handle Enter key on etRep to remove focus and mark set as completed
            editTextRep.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    // Remove focus from edit texts
                    editTextKg.clearFocus()
                    editTextRep.clearFocus()

                    // Hide keyboard
                    val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(editTextRep.windowToken, 0)

                    // Mark set as completed
                    set.isCompleted = true
                    completedCheckBox.isChecked = true
                    viewModel.updateCurrentSet(set)

                    return@setOnEditorActionListener true
                }
                false
            }

            completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (set.isCompleted != isChecked) {
                    set.isCompleted = isChecked
                    viewModel.updateCurrentSet(set)
                    overlay.alpha = if (isChecked) 1F else 0F
                }
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
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = list.size
            override fun getNewListSize() = newData.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                list[oldItemPosition].setId == newData[newItemPosition].setId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                list[oldItemPosition] == newData[newItemPosition]
        })
        list = newData
        diffResult.dispatchUpdatesTo(this)
    }
}