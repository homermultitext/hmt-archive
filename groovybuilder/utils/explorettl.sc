// Ammonite script to check the output of TTL building
// for duplicate statements.
//
// Takes one parameter: name of a file with TTL statements to examine.

import scala.io.Source


@main
def examine(f: String) = {
  val ttl = Source.fromFile(f).getLines.toVector
  println("Lines: " + ttl.size)
  println("Distinct: " + ttl.distinct.size)

  // Sorted histogram of statements:
  val grouped = ttl.groupBy(w => w)
  val stmntCounts = grouped.map { case (k,v) => (k,v.size)}.toSeq.sortBy(-1 * _._2).filter(_._2 > 1)

  println("Duplicate statements: " + stmntCounts.size)
  for (cnt <- stmntCounts) {
    println(cnt._2 + "  " + cnt._1)
  }
  // Eliminate statements with issues already filed:
  //val noCDepth = freqs.filterNot(_._1.contains("citationDepth"))

}
