name := "LearningSpark"

version := "1.0"

fork := true

// only relevant for Java sources --
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.11.12"

scalacOptions ++= Seq("-unchecked", "-deprecation")

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.2"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.2.2"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.2"

libraryDependencies += "org.apache.spark" %% "spark-hive" % "2.2.2"

libraryDependencies += "org.apache.spark" %% "spark-graphx" % "2.2.2"

libraryDependencies += "org.apache.tinkerpop" % "gremlin-driver" % "3.3.4"

libraryDependencies += "org.apache.tinkerpop" % "gremlin-core" % "3.3.4"

libraryDependencies += "org.apache.tinkerpop" % "gremlin-server" % "3.3.4"

libraryDependencies += "org.apache.tinkerpop" % "tinkergraph-gremlin" % "3.3.4"

//<dependency>
//  <groupId>org.apache.tinkerpop</groupId>
//  <artifactId>tinkergraph-gremlin</artifactId>
//  <version>3.3.4</version>
//</dependency>
libraryDependencies += "org.janusgraph" % "janusgraph-core" % "0.3.1"
//
//libraryDependencies += "org.janusgraph" % "janusgraph-cassandra" % "0.3.1"
//
//libraryDependencies += "org.janusgraph" % "janusgraph-es" % "0.3.1"



// needed to make the hiveql examples run at least on Linux
javaOptions in run += "-XX:MaxPermSize=128M"

scalacOptions += "-target:jvm-1.8"

// note: tested directly using sbt with -java-home pointing to a JDK 1.8