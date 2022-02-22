package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.CardioActivity

@Dao
interface CardioActivityDao {
    @Query("SELECT * FROM CardioActivity")
    fun loadAll(): List<CardioActivity>

    @Query("SELECT * FROM CardioActivity WHERE id=:cardioActivityId")
    fun loadCardioActivity(cardioActivityId: Long): CardioActivity

    @Query("SELECT * FROM CardioActivity WHERE id=:cardioActivityId")
    fun loadLiveCardioActivity(cardioActivityId: Long): LiveData<CardioActivity>

    @Insert(onConflict = IGNORE)
    fun insertCardioActivity(cardioActivity: CardioActivity): Long

    @Update(onConflict = REPLACE)
    fun updateCardioActivity(cardioActivity: CardioActivity)

    @Delete
    fun deleteCardioActivity(cardioActivity: CardioActivity)
}
