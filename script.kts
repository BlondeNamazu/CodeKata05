import java.io.File
import java.security.MessageDigest

val md5 = MessageDigest.getInstance("MD5")

// get index list using hash function
fun getHashedIndices(k: Int, m: Int, input: String): List<Int> {
  var target = input
  var result = mutableListOf<Int>()

  repeat(k) {
    val digest = md5.digest(target.toByteArray())
    var index: Long = 0L

    // use first 56 bit (8bit * 7) to determine index
    for (i in 0..6) {

      // get byte value in (-128 to 127) and convert to positive range (0 to 255)
      val positiveIndex = digest[i].toInt() + 128

      index = index * 256 + positiveIndex

      // "NMZ" is not contained in dictionary, so it is suit for prefix
      target += "NMZ"
    }

    // index should be in (0 to m)
    result.add((index % m).toInt())
  }

  return result.toList()
}

val m = 18
val k = 3

val bloom = Array(m){false}

val dictionary = File("dictionary.txt").readLines()

/*
// load dictionary and save result to bloom[m] using hashed indices
for (word in dictionary) {
  val indices = getHashedIndices(k, m, word)
  for (index in indices) {
    bloom[index] = true
  }
}
*/

// check words
// memo: use standard input

getHashedIndices(k, m, dictionary[0]).forEach { println(it) }
getHashedIndices(k, m, dictionary[1]).forEach { println(it) }
getHashedIndices(k, m, dictionary[2]).forEach { println(it) }
getHashedIndices(k, m, dictionary[3]).forEach { println(it) }
