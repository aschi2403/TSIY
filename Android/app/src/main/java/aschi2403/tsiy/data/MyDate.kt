package aschi2403.tsiy.data

import aschi2403.tsiy.screens.fragments.AddWeightFragment
import kotlin.reflect.KFunction1

class MyDate(
    day: Int = 0,
    month: Int = 0,
    year: Int = 0,
    time_millis: Long = 0L,
    val callBack: () -> Unit
) {
    var day: Int = day
        set(value) {
            field = value
            callBack()
        }
    var month: Int = month
        set(value) {
            field = value
            callBack()
        }
    var year: Int = year
        set(value) {
            field = value
            callBack()
        }
    var time_millis: Long = time_millis
        set(value) {
            field = value
            callBack()
        }
}
