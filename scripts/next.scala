// https://gist.github.com/ThiporKong/4399695
def tsort[A](edges: Traversable[(A, A)]): Iterable[A] = {
  @annotation.tailrec
  def tsort0(toPreds: Map[A, Set[A]], done: Iterable[A]): Iterable[A] = {
    val (noPreds, hasPreds) = toPreds.partition { _._2.isEmpty }
    if (noPreds.isEmpty) {
      if (hasPreds.isEmpty) done else sys.error(hasPreds.toString)
    } else {
      val found = noPreds.map { _._1 }
      tsort0(hasPreds.mapValues { _ -- found }, done ++ found)
    }
  }


  val toPred = edges.foldLeft(Map[A, Set[A]]()) { (acc, e) =>
      acc + (e._1 -> acc.getOrElse(e._1, Set())) + (e._2 -> (acc.getOrElse(e._2, Set()) + e._1))
  }
  tsort0(toPred, Seq())
}

val data = List(
  "actorTests"        -> List("testkit"),
  "agent"             -> List("actor", "testkit"),
  "camel"             -> List("actor", "testkit", "slf4j"),
  "cluster"           -> List("remote", "remoteTests", "testkit"),
  "clusterMetrics"    -> List("cluster"),
  "clusterSharding"   -> List("cluster", "distributedData", "persistence", "clusterTools"),
  "clusterTools"      -> List("cluster"),
  "contrib"           -> List("remote", "remoteTests", "cluster", "clusterTools", "persistence"),
  "distributedData"   -> List("cluster"),
  "multiNodeTestkit"  -> List("remote", "testkit"),
  "osgi"              -> List("actor"),
  "persistence"       -> List("actor", "testkit", "protobuf"),
  "persistenceQuery"  -> List("stream", "persistence", "streamTestkit"),
  "persistenceShared" -> List("persistence", "testkit", "remote", "protobuf"),
  "persistenceTck"    -> List("persistence", "testkit"),
  "remote"            -> List("actor", "stream", "actorTests", "testkit", "streamTestkit", "protobuf"),
  "remoteTests"       -> List("actorTests", "remote", "streamTestkit", "multiNodeTestkit"),
  "slf4j"             -> List("actor", "testkit"),
  "stream"            -> List("actor", "protobuf"),
  "streamTestkit"     -> List("stream", "testkit"),
  "streamTests"       -> List("streamTestkit", "remote", "stream"),
  "streamTestsTck"    -> List("streamTestkit", "stream"),
  "testkit"           -> List("actor"),
  "actorTyped"        -> List("actor"),
  "persistenceTyped"  -> List("actorTyped", "persistence", "persistenceQuery", "actorTypedTests", "actorTestkitTyped"),
  "clusterTyped"      -> List("persistenceTyped", "clusterSharding", "actorTestkitTyped", "actorTypedTests", "persistenceTyped", "remoteTests"),
  "streamTyped"       -> List("actorTyped", "stream", "streamTestkit", "actorTestkitTyped", "actorTypedTests"),
  "actorTestkitTyped" -> List("actorTyped", "testkit"),
  "actorTypedTests"   -> List("actorTyped", "actorTestkitTyped")
)

val edges =
  data.flatMap{ case (source, destinations) =>
    destinations.map(destination => (destination, source) )
  }

tsort(edges)

/*
actor
protobuf
actorTyped
stream
osgi
testkit
persistence
actorTests
agent
streamTestkit
actorTestkitTyped
slf4j
streamTestsTck
actorTypedTests
persistenceQuery
persistenceTck
remote
camel
persistenceTyped
streamTests
streamTyped
multiNodeTestkit
persistenceShared
remoteTests
cluster
clusterTools
distributedData
clusterMetrics
clusterSharding
contrib
clusterTyped
*/
