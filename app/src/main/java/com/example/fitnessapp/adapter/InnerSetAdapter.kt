import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.WorkoutSet
import com.example.fitnessapp.data.WorkoutViewModel
import kotlinx.coroutines.Job

class InnerSetAdapter(
    private var list: List<WorkoutSet>,
    private val viewModel: WorkoutViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<InnerSetAdapter.InnerViewHolder>() {


    private var kgDebounceJob: Job? = null
    private var repDebounceJob: Job? = null
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

            editTextKg.removeTextChangedListener(editTextKg.tag as? TextWatcher)
            editTextRep.removeTextChangedListener(editTextRep.tag as? TextWatcher)

            val kgTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty()) {
                        set.currentKg = p0.toString().toIntOrNull() ?: 0
                        viewModel.addSet(set)
                    }
                }
                override fun afterTextChanged(p0: Editable?) {}
            }

            editTextKg.addTextChangedListener(kgTextWatcher)
            editTextKg.tag = kgTextWatcher

            val repsTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0 != null && p0.isNotEmpty()) {
                        set.currentReps = p0.toString().toIntOrNull() ?: 0
                        viewModel.addSet(set)
                    }
                }
                override fun afterTextChanged(p0: Editable?) {}
            }

            editTextRep.addTextChangedListener(repsTextWatcher)
            editTextRep.tag = repsTextWatcher
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

////        holder.prevSet.text = "Previous Set: ${currentSet.prevSet}"
//        holder.editTextKg.setText(currentSet.currentKg.toString())
//        holder.editTextRep.setText(currentSet.currentReps.toString())
//        holder.completedCheckBox.isChecked = currentSet.isCompleted
//
//
//        holder.editTextKg.setOnKeyListener{ v, keyCode, event ->
//            if(keyCode == KeyEvent.KEYCODE_ENTER){
//                val kgValue = holder.editTextKg.text.toString().toIntOrNull() ?: 0
//                if (currentSet.currentKg != kgValue) {
//                    currentSet.currentKg = kgValue
//                    viewModel.updateCurrentSet(currentSet)
//                }
//
//                return@setOnKeyListener true
//            }
//            false
//        }
//
////        holder.editTextKg.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
////
////            override fun afterTextChanged(s: Editable?) {
////                kgDebounceJob?.cancel()
////                kgDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
////                    delay(300)
////                    val kgValue = s?.toString()?.toIntOrNull() ?: 0
////                    if (currentSet.currentKg != kgValue) {
////                        currentSet.currentKg = kgValue
////                        viewModel.updateCurrentSet(currentSet)
////                    }
////                }
////            }
////        })
//
//
////        holder.editTextKg.addTextChangedListener(object : TextWatcher {
////            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
////                //TODO Not needed for this case, but required to implement TextWatcher interface
////            }
////
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                //TODO Not needed for this case, but required to implement TextWatcher interface
////            }
////
////            override fun afterTextChanged(s: Editable?) {
////                val kgValue = s?.toString()?.toIntOrNull() ?: 0
////                if (currentSet.currentKg != kgValue) {
////                    currentSet.currentKg = kgValue
////                    viewModel.updateCurrentSet(currentSet)
////                }
////            }
////        })
//
//        holder.editTextRep.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                repDebounceJob?.cancel()
//                repDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
//                    delay(300)
//                    val repValue = s?.toString()?.toIntOrNull() ?: 0
//                    if (currentSet.currentReps != repValue) {
//                        currentSet.currentReps = repValue
//                        viewModel.updateCurrentSet(currentSet)
//                    }
//                }
//            }
//        })
//
////        holder.editTextRep.addTextChangedListener {
////            repDebounceJob?.cancel()
////            repDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
////                delay(300)
////                val repValue = it.toString().toIntOrNull() ?: 0
////                currentSet.currentReps = repValue
////                viewModel.updateCurrentSet(currentSet)
////            }
////        }
//
//        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
//            currentSet.isCompleted = isChecked
//            viewModel.updateCurrentSet(currentSet)
//        }
    }

    fun setData(newData: List<WorkoutSet>) {
        list = newData
        notifyDataSetChanged()
    }
}

