import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyXAxisValueFormatter(private val months: List<String>) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        return if (index >= 0 && index < months.size) {
            months[index]
        } else {
            ""
        }
    }
}
