package ru.chani.checkcontacts.data.sources.local.db

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "Contacts_on_Phone")
data class ContactDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @TypeConverters(NumberListConverter::class)
    val numbers: List<String>,
    val isSalamClient: String
)


@Dao
interface ContactDao {
    @Query("SELECT * FROM Contacts_on_Phone")
    suspend fun getAllContacts(): List<ContactDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactDbEntity)

    @Delete
    suspend fun deleteContact(contact: ContactDbEntity)

    @Query("SELECT * FROM Contacts_on_Phone WHERE isSalamClient = 'Client'")
    fun getClientContacts(): Flow<List<ContactDbEntity>>
}

// Converter for converting a list of strings to a string and back (list of Numbers)
class NumberListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun toString(value: List<String>): String {
        return value.joinToString(",")
    }
}


@Database(entities = [ContactDbEntity::class], version = 1)
@TypeConverters(NumberListConverter::class)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        private var INSTANCE: ContactsDatabase? = null

        fun getDatabase(context: Context): ContactsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ContactsDatabase::class.java,
                    "contacts_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


