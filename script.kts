import java.io.File
import java.security.MessageDigest

val md5 = MessageDigest.getInstance("MD5")
val dictionary = File("dictionary.txt").readLines()
val checkWords = File("random5-chars.txt").readLines()

class BloomFilter(
  val k: Int,
  val m: Int
){
  val bloom = Array(m){false}

  // load dictionary and save result to bloom[m] using hashed indices
  fun load() {
    for (word in dictionary) {
      val indices = getHashedIndices(word)
      for (index in indices) {
        bloom[index] = true
      }
    }
  }

  fun evaluate(): Float {
    var correctCount = 0
    for (word in checkWords) {
      val bloomFilterResult = infer(word)
      val groundTruth = dictionary.contains(word)
      if(bloomFilterResult == groundTruth) {
        correctCount += 1
      }
    }
    return correctCount.toFloat() / checkWords.size
  }
  
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
      }
  
      // index should be in (0 to m)
      result.add((index % m).toInt())
  
      // "NMZ" is not contained in dictionary, so it is suit for prefix
      target += "NMZ"
    }
  
    return result.toList()
  }
  
  // check if input is contained using BloomFilter
  fun infer(input: String): Boolean {
    val indices = getHashedIndices(input)
    return indices.all { index ->
      bloom[index]
    }
  }
}

fun findMinimumLengthForAccuracy(
  k: Int,
  targetAccuracy: Float
){
  var ok = 15_000_000 // considering maximum memory size 
  var ng = 1
  while(ok - ng > 1) {
    val m = (ok+ng)/2
    val bf = BloomFilter(k,m)
    bf.load()
    val accuracy = bf.evaluate()
    if(accuracy >= targetAccuracy) ok = m
    else ng = m

    println("k = $k, m = $m achieves accuracy $accuracy")
  }
  println("k = $k, m = $ok achieves accuracy $targetAccuracy")
  println("====================================================")
}

val kList = listOf(3, 5, 10, 50)
val accuracyList = listOf(0.99f, 0.999f)

for (k in kList) {
  for (accuracy in accuracyList) {
    findMinimumLengthForAccuracy(k, accuracy)
  }
}
