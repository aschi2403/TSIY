package aschi2403.tsiy.data

class MyDate(
    day: Int = 0,
    month: Int = 0,
    year: Int = 0,
    timeInMillis: Long = 0L,
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
    var millis: Long = timeInMillis
        set(value) {
            field = value
            callBack()
        }
}
