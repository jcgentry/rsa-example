import java.math.BigInteger
import java.util.Random

private const val RSA_BIT_LENGTH = 2048
private val E: BigInteger = BigInteger.valueOf(65537)

typealias PublicKey = Pair<BigInteger, BigInteger>
typealias PrivateKey = Pair<BigInteger, BigInteger>

typealias CypherText = String


fun generateKeys(): Pair<PublicKey, PrivateKey> {
    val randomNumberGenerator = Random()
    val p = BigInteger.probablePrime(RSA_BIT_LENGTH, randomNumberGenerator)
    val q = BigInteger.probablePrime(RSA_BIT_LENGTH, randomNumberGenerator)
    val n = p * q
    val publicKey = PublicKey(n, E)
    val d = privateExponent(E, p, q)
    val privateKey = PrivateKey(n, d)
    return Pair(publicKey, privateKey)
}

fun encrypt(m: String, key: PublicKey): CypherText {
    val (n, e) = key
    return (m.messageToNumber().modPow(e, n).toString())
}

fun decrypt(c: CypherText, key: PrivateKey): String {
    val (n, d) = key
    val cNum = BigInteger(c)
    val mAsNum = cNum.modPow(d, n)
    return mAsNum.numberToMessage()
}

private fun privateExponent(e: BigInteger, p: BigInteger, q: BigInteger): BigInteger =
    with(totient(p, q)) { BigInteger.valueOf(e.toLong()).modInverse(this) }

private fun totient(p: BigInteger, q: BigInteger) = (p - BigInteger.ONE) * (q - BigInteger.ONE)

private fun BigInteger.numberToMessage(): String {
    val stringRep = this.toString()

    /* Pad with zeroes */
    val sizeOfFirst = stringRep.length % 16
    val paddedStringRep = "0".repeat(16 - sizeOfFirst) + stringRep

    /* Turn into characters */
    return paddedStringRep
        .chunked(16)
        .map { it.toInt() }
        .map { it.toChar() }
        .joinToString(separator = "")
}


private fun String.messageToNumber() =
    this
        .map { it.code }
        .map { it.toString() }
        .map { it.padStart(16, '0') }
        .joinToString(separator = "")
        .let { BigInteger(it) }


