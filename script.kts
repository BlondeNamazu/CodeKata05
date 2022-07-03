import java.io.File
import java.security.MessageDigest

val md5 = MessageDigest.getInstance("MD5")

// get index list using hash function
// this result cannot produce index larger than 255 (m is limited to 256 or smaller)
fun getHashedIndices(k: Int, m: Int, input: String): List<Int> {
  var target = input
  var result = mutableListOf<Int>()

  repeat((k/16+1)) {
    val digest = md5.digest(target.toByteArray())
    for (i in 0..15) {

      // get byte value in (-128 to 127) and convert to positive range (0 to 255)
      val positiveIndex = digest[i].toInt() + 128

      // index should be in (0 to m)
      result.add(positiveIndex % m)  

      target += input
    }
  }

  return result.take(k).toList()
}

val m = 18
val k = 3

val bloom = Array(m){false}

val dictionary = File("dictionary.txt").readLines()

// load dictionary and save result to bloom[m] using hashed indices
for (word in dictionary) {
  val indices = getHashedIndices(k, m, word)
  for (index in indices) {
    bloom[index] = true
  }
}

// check words
// memo: use standard input

// getHashedIndices(k, m, dictionary[3]).forEach { println(it) }
