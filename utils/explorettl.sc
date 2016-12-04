// script to check output of TTL building.
import scala.io.Source

val f = "all.ttl"

 val ttl = Source.fromFile(f).getLines.toVector

 println("Lines: " + ttl.size)
 println("Distinct: " + ttl.distinct.size)
 val grouped = ttl.groupBy(w => w)

 val stmntCounts = grouped.map { case (k,v) => (k,v.size)}.toSeq.sortBy(-1 * _._2)


// Eliminate statements with issues already filed:
val noCDepth = freqs.filterNot(_._1.contains("citationDepth"))
