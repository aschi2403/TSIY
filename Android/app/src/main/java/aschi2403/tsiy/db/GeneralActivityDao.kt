package aschi2403.tsiy.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import aschi2403.tsiy.model.GeneralActivity

@Dao
interface GeneralActivityDao {
    @Query("SELECT * FROM GeneralActivity")
    fun loadAll(): List<GeneralActivity>

    @Query("SELECT * FROM GeneralActivity WHERE id=:generalActivityId")
    fun loadGeneralActivity(generalActivityId: Long): GeneralActivity

    @Query("SELECT * FROM GeneralActivity WHERE id=:generalActivityId")
    fun loadLiveGeneralActivity(generalActivityId: Long): LiveData<GeneralActivity>

    @Insert(onConflict = IGNORE)
    fun insertGeneralActivity(generalActivity: GeneralActivity): Long

    @Update(onConflict = REPLACE)
    fun updateGeneralActivity(generalActivity: GeneralActivity)

    @Delete
    fun deleteGeneralActivity(generalActivity: GeneralActivity)
}