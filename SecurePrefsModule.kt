    
/**
 * Requires dependency:
 *     //security for secured preferences
 *     implementation "androidx.security:security-crypto:1.0.0-rc02"
 */

const val STRING_CONSTANT = "STRING_CONSTANT"
const val INT_CONSTANT = "INT_CONSTANT"
const val BOOLEAN_CONSTANT = "BOOLEAN_CONSTANT"
class SecPrefs (
    context: android.content.Context
){

    private val masterKeyAlias = androidx.security.crypto.MasterKeys.getOrCreate(androidx.security.crypto.MasterKeys.AES256_GCM_SPEC)
    private val prefs = androidx.security.crypto.EncryptedSharedPreferences.create(
        "encrypted_shared_prefs",
        masterKeyAlias,
        context,
        androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Synchronized
    fun saveStringInPrefs(
        input: String
    ) {
        prefs.edit()
            .putString(STRING_CONSTANT, input)
            .apply()
    }

    @Synchronized
    fun loadStringFromPrefs(): String? {
        return prefs.getString(STRING_CONSTANT, null)
    }

    @Synchronized
    fun saveIntInPrefs(
        input: Int
    ) {
        prefs.edit()
            .putInt(INT_CONSTANT, input)
            .apply()
    }

    @Synchronized
    fun loadIntFromPrefs(): Int? {
        return prefs.getInt(INT_CONSTANT, 0)
    }

    @Synchronized
    fun saveBooleanInPrefs(
        input: Boolean
    ) {
        prefs.edit()
            .putBoolean(BOOLEAN_CONSTANT, input)
            .apply()
    }

    @Synchronized
    fun loadBooleanFromPrefs(): Boolean? {
        return prefs.getBoolean(BOOLEAN_CONSTANT, false)
    }
}
