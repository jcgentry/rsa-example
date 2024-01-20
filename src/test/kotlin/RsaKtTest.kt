import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class RsaKtTest {
    @Test
    fun testEncryptDecrypt() {
        val msg = "This is a test message."
        val (publicKey, privateKey) = generateKeys()
        println("Public key: $publicKey")
        println("Private key: $privateKey")
        val encrypted = encrypt(msg, publicKey)
        val decrypted = decrypt(encrypted, privateKey)
        assertEquals(msg, decrypted)
    }
}