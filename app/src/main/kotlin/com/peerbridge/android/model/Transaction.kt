package com.peerbridge.android.model

import android.text.format.DateUtils
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.room.*
import com.peerbridge.android.crypto.Encryption
import com.peerbridge.android.crypto.KeyPair
import com.peerbridge.android.crypto.PublicKey
import com.peerbridge.android.crypto.sha256
import com.peerbridge.android.http.Endpoints
import com.peerbridge.android.http.HttpClient
import com.peerbridge.android.serialization.ByteArrayBase64Serializer
import com.peerbridge.android.ui.context.LocalDatabase
import com.peerbridge.secp256k1.Hex
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity
@Serializable
@Suppress("ArrayInDataClass")
data class Transaction(
    @PrimaryKey val id: String,
    val sender: String,
    val receiver: String,
    val balance: Int = 0,
    val timeUnixNano: Long = System.currentTimeMillis(),
    val fee: Int = 0,
    @Serializable(with = ByteArrayBase64Serializer::class) val data: ByteArray? = null,
    val signature: String,
    val blockID: String? = null
) {
    constructor(id: String, sender: String, receiver: String, balance: Int = 0, timeUnixNano: Long = System.currentTimeMillis(), fee: Int = 0, data: String? = null, signature: String, blockID: String? = null) : this(id, sender, receiver, balance, timeUnixNano, fee, data?.let { Base64.decode(it, Base64.DEFAULT) }, signature, blockID)

    val date: Date
        get() = Date(timeUnixNano / 1_000_000L) // Date is expecting milliseconds

    val relativeTime: String
        get() = DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString()

    fun decryptData(keyPair: KeyPair): ByteArray? = data?.let {
        val publicKey = if (sender == keyPair.publicKeyHex) receiver else sender
        val secret = Encryption.secret(keyPair.privateKeyHex, publicKey) ?: return null
        Encryption.decrypt(it, secret)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    companion object {
        suspend fun create(keyPair: KeyPair, receiver: PublicKey, data: ByteArray? = null): Transaction? {
            val id = ""
            val sender = keyPair.publicKeyHex
            val balance = 0
            val timeUnixNano: Long = Date().time * 1000L
            val fee = 0
            val encryptedData = data?.let {
                val secret = Encryption.secret(keyPair.privateKeyHex, receiver.value) ?: return null
                Encryption.encrypt(it, secret)
            }
            val signature = Hex.encodeToString("id:$id|sender:$sender|receiver:$receiver|balance:$balance|timeUnixNano:$timeUnixNano|data:${encryptedData?.let { Hex.encodeToString(it) }}|fee:$fee".sha256)
            val transaction = Transaction(
                id = id,
                sender = sender,
                receiver = receiver.value,
                balance = balance,
                timeUnixNano = timeUnixNano,
                fee = fee,
                data = encryptedData,
                signature = signature
            )
            val payload = Json.encodeToString(mapOf("transaction" to transaction))
            val response = HttpClient.post("${Endpoints.main}/blockchain/transaction/create", payload)
            return Json.decodeFromString(response.body!!.string())
        }

        suspend fun fetch(publicKeyHex: String): Collection<Transaction> {
            val response = HttpClient.get("${Endpoints.main}/blockchain/accounts/transactions/get?account=$publicKeyHex")
            return Json.decodeFromString<List<Transaction>>(response.body!!.string())
        }
    }
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `transaction`")
    fun getAll(): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE sender LIKE :publicKeyHex OR receiver LIKE :publicKeyHex ORDER BY timeUnixNano ASC")
    fun findByPublicKey(publicKeyHex: String): Flow<List<Transaction>>

    @Query("SELECT * FROM `transaction` WHERE sender LIKE :publicKeyHex OR receiver LIKE :publicKeyHex ORDER BY timeUnixNano DESC LIMIT 1")
    fun findLastByPublicKey(publicKeyHex: String): Flow<Transaction>

    @Query("SELECT sender FROM `transaction` WHERE receiver LIKE :publicKeyHex UNION SELECT receiver FROM `transaction` WHERE sender LIKE :publicKeyHex")
    fun findContactsByPublicKey(publicKeyHex: String): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT * FROM `transaction` WHERE id LIKE :id)")
    fun hasTransaction(id: String): Boolean

    @Insert
    suspend fun insert(vararg transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    suspend fun update(publicKeyHex: String) {
        val transactions = Transaction.fetch(publicKeyHex)
        for (transaction in transactions) {
            if (!hasTransaction(transaction.id)) {
                insert(transaction)
            }
        }
    }
}

fun Flow<Transaction>.asMessage(keyPair: KeyPair) : Flow<Message> = map { transaction ->
    Message(transaction.decryptData(keyPair))
}

fun Flow<List<Transaction>>.asMessages(keyPair: KeyPair) : Flow<List<Message>> = map { transactions ->
    transactions.map { transaction ->
        Message(transaction.decryptData(keyPair))
    }
}
