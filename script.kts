import java.io.File
import java.security.MessageDigest

val m = 18
val k = 3

val md5 = MessageDigest.getInstance("MD5")
val bloom = Array(m){false}

// get index list using hash function
fun getHashedIndices(input: String): List<Int> {
  var target = input
  var result = mutableListOf<Int>()

  repeat(k) {
    val digest = md5.digest(target.toByteArray())
    var index: Long = 0L

    // use first 56 bit (8bit * 7) to determine index
    for (i in 0..6) {

      // get byte value in (-128 to 127) and convert to positive range (0 to 255)
      val positiveIndex = digest[i].toInt() + 128

      // finally, index is composed of digest 0 to 6. Index in bit expression looks like below:
      // |digest 0|digest 1|digest 2|digest 3|...|digest 6|
      // |01000101|01110101|01010100|11010101|...|00010101|
      index = index * 256 + positiveIndex

      // "NMZ" is not contained in dictionary, so it is suit for prefix
      target += "NMZ"
    }

    // index should be in (0 to m)
    result.add((index % m).toInt())
  }

  return result.toList()
}

fun isContained(input: String): Boolean {
  val indices = getHashedIndices(input)
  return indices.all { index ->
    bloom[index]
  }
}
  

val dictionary = File("dictionary.txt").readLines()

// load dictionary and save result to bloom[m] using hashed indices
for (word in dictionary) {
  val indices = getHashedIndices(word)
  for (index in indices) {
    bloom[index] = true
  }
}

// check words
println("print out words both contained in random5-chars")
val checkWords = File("random5-chars.txt").readLines()
for (word in checkWords) {
  if(isContained(word)) println(word)
}

