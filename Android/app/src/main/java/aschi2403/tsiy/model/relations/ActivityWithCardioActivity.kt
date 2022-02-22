package aschi2403.tsiy.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import aschi2403.tsiy.model.Activity
import aschi2403.tsiy.model.ActivityType
import aschi2403.tsiy.model.CardioActivity


open class ActivityWithCardioActivity {
    @Embedded
    lateinit var activity: Activity

    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    var cardioActivity: CardioActivity? = null

    @Relation(
        entity = ActivityType::class,
        entityColumn = "id",
        parentColumn = "activityTypeId"
    )
    lateinit var activityType: ActivityType

    var workoutId: Long? = null
}
