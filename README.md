# CodeKata05

This repository is my solution for [CodeKata 05](http://codekata.com/kata/kata05-bloom-filters/).

Load given dictionary and evaluate accuracy using original data set.
Also, search appropriate bloom filter length for given k (number of hash functions) and target accuracy.

Note:
Since bloom filter is probablistic approach, accuracy for bloom filter length is non-convex.
So, binary search result should be referred as roughly one.

## How to Run

```
kotlinc -script script.kts
```
