import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.R
import com.example.fitnessapp.data.WorkoutSet
import com.example.fitnessapp.data.WorkoutViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InnerSetAdapter(
    private var list: List<WorkoutSet>,
    private val viewModel: WorkoutViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<InnerSetAdapter.InnerViewHolder>() {


    private var kgDebounceJob: Job? = null
    private var repDebounceJob: Job? = null
    inner class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val prevSet: TextView = itemView.findViewById(R.id.previousSetTV)
        val editTextKg: EditText = itemView.findViewById(R.id.etKG)
        val editTextRep: EditText = itemView.findViewById(R.id.etREP)
        val completedCheckBox: CheckBox = itemView.findViewById(R.id.setCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.inner_rv_item, parent, false)
        return InnerViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
        val currentSet = list[position]

//        holder.prevSet.text = "Previous Set: ${currentSet.prevSet}"
        holder.editTextKg.setText(currentSet.currentKg.toString())
        holder.editTextRep.setText(currentSet.currentReps.toString())
        holder.completedCheckBox.isChecked = currentSet.isCompleted



        holder.editTextKg.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                kgDebounceJob?.cancel()
                kgDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    val kgValue = s?.toString()?.toIntOrNull() ?: 0
                    if (currentSet.currentKg != kgValue) {
                        currentSet.currentKg = kgValue
                        viewModel.updateCurrentSet(currentSet)
                    }
                }
            }
        })


//        holder.editTextKg.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                //TODO Not needed for this case, but required to implement TextWatcher interface
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                //TODO Not needed for this case, but required to implement TextWatcher interface
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                val kgValue = s?.toString()?.toIntOrNull() ?: 0
//                if (currentSet.currentKg != kgValue) {
//                    currentSet.currentKg = kgValue
//                    viewModel.updateCurrentSet(currentSet)
//                }
//            }
//        })

        holder.editTextRep.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                repDebounceJob?.cancel()
                repDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(300)
                    val repValue = s?.toString()?.toIntOrNull() ?: 0
                    if (currentSet.currentReps != repValue) {
                        currentSet.currentReps = repValue
                        viewModel.updateCurrentSet(currentSet)
                    }
                }
            }
        })

//        holder.editTextRep.addTextChangedListener {
//            repDebounceJob?.cancel()
//            repDebounceJob = viewLifecycleOwner.lifecycleScope.launch {
//                delay(300)
//                val repValue = it.toString().toIntOrNull() ?: 0
//                currentSet.currentReps = repValue
//                viewModel.updateCurrentSet(currentSet)
//            }
//        }

        holder.completedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            currentSet.isCompleted = isChecked
            viewModel.updateCurrentSet(currentSet)
        }
    }

    fun setData(newData: List<WorkoutSet>) {
        list = newData
        notifyDataSetChanged()
    }
}

