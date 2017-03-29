package stackoverflow

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, FunSuite}

@RunWith(classOf[JUnitRunner])
class StackOverflowSuite2 extends FunSuite with BeforeAndAfterAll {


  lazy val testObject = new StackOverflow {
    override val langs =
      List(
        "JavaScript", "Java", "PHP", "Python", "C#", "C++", "Ruby", "CSS",
        "Objective-C", "Perl", "Scala", "Haskell", "MATLAB", "Clojure", "Groovy")
    override def langSpread = 50000
    override def kmeansKernels = 45
    override def kmeansEta: Double = 20.0D
    override def kmeansMaxIterations = 120
  }
  lazy val sc = StackOverflow.sc

  lazy val postings = sc.parallelize(List(
    Posting(1, 1, None, None, 1, None),
    Posting(2, 2, None, Some(1), 100, None),
    Posting(2, 3, None, Some(1), 10, None),
    Posting(1, 4, None, None, 1, None),
    Posting(2, 5, None, Some(4), 4, None),
    Posting(1, 6, None, None, 1, None)
  ))

  test("testObject can be instantiated") {
    val instantiatable = try {
      testObject
      true
    } catch {
      case _: Throwable => false
    }
    assert(instantiatable, "Can't instantiate a StackOverflow object")
  }

  test("grouped postings") {

    val grouped = testObject.groupedPostings(postings).collect()
    grouped.foreach(println)
    assert(grouped.length == 2)
    assert(grouped.filter(_._1 == 1).flatMap(_._2).length == 2)
    assert(grouped.filter(_._1 == 4).flatMap(_._2).length == 1)
    assert(grouped.filter(_._1 == 6).flatMap(_._2).length == 0)

  }

  test("compute score") {
    val grouped = testObject.groupedPostings(postings)
    val scores = testObject.scoredPostings(grouped).collect()
    assert(scores.filter(_._1.id == 1).head._2 == 100)
    assert(scores.filter(_._1.id == 4).head._2 == 4)
  }


}
