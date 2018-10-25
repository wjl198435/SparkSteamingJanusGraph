package streaming

import java.io.File
import java.nio.charset.Charset

import com.google.common.io.Files
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.streaming.{Seconds, StreamingContext, Time}
import org.apache.spark.util.{IntParam, LongAccumulator}
import org.apache.spark.sql.SparkSession
import org.apache.tinkerpop.gremlin.driver.{Client, Cluster, ResultSet}

import scala.util.Random
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0
import org.apache.tinkerpop.gremlin.structure.{Graph, Property, Vertex, VertexProperty}
import org.apache.tinkerpop.gremlin.structure.io.gryo.{GryoMapper, GryoVersion}
import org.apache.tinkerpop.gremlin.structure.util.empty.EmptyGraph
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry


//import com.fasterxml.jackson.module.scala.DefaultScalaModule
//import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.databind.DeserializationFeature




/**
  * Use this singleton to get or register a Broadcast variable.
  */

case class Event(name: String, age: Int, gender: String, zip: String)

object WordBlacklist {

  @volatile private var instance: Broadcast[Seq[String]] = null

  def getInstance(sc: SparkContext): Broadcast[Seq[String]] = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          val wordBlacklist = Seq("a", "b", "c")
          instance = sc.broadcast(wordBlacklist)
        }
      }
    }
    instance
  }
}

/**
  * Use this singleton to get or register an Accumulator.
  */
object DroppedWordsCounter {

  @volatile private var instance: LongAccumulator = null

  def getInstance(sc: SparkContext): LongAccumulator = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          instance = sc.longAccumulator("WordsInBlacklistCounter")
        }
      }
    }
    instance
  }
}



/**
  * Use this singleton to get or register an Accumulator.
  */
object ErrorEventsCounter {

  @volatile private var instance: LongAccumulator = null

  def getInstance(sc: SparkContext): LongAccumulator = {
    if (instance == null) {
      synchronized {
        if (instance == null) {
          instance = sc.longAccumulator("ErrorEventsCounter")
        }
      }
    }
    instance
  }
}


/**
  * Counts words in text encoded with UTF8 received from the network every second. This example also
  * shows how to use lazily instantiated singleton instances for Accumulator and Broadcast so that
  * they can be registered on driver failures.
  *
  * Usage: eventLoadToDailyGraph <hostname> <port> <checkpoint-directory> <output-file>
  *   <hostname> and <port> describe the TCP server that Spark Streaming would connect to receive
  *   data. <checkpoint-directory> directory to HDFS-compatible file system which checkpoint data
  *   <output-file> file to which the word counts will be appended
  *
  * <checkpoint-directory> and <output-file> must be absolute paths
  *
  * To run this on your local machine, you need to first run a Netcat server
  *
  *      `$ nc -lk 9999`
  *
  * and run the example as
  *
  *      `$ ./bin/run-example org.apache.spark.examples.streaming.RecoverableNetworkWordCount \
  *              localhost 9999 ~/checkpoint/ ~/out`
  *
  * If the directory ~/checkpoint/ does not exist (e.g. running for the first time), it will create
  * a new StreamingContext (will print "Creating new context" to the console). Otherwise, if
  * checkpoint data exists in ~/checkpoint/, then it will create StreamingContext from
  * the checkpoint data.
  *
  * Refer to the online documentation for more details.
  */

//case class Person (first_name:String,last_name: String,age:String)

object eventLoadToDailyGraph {

//  def createMapper() = {
//    val mapper = new ObjectMapper()
//    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//    mapper.registerModule(DefaultScalaModule)
//    mapper
//  }

  def createContext(ip: String, port: Int, outputPath: String, checkpointDirectory: String)
  : StreamingContext = {

    // If you do not see this printed, that means the StreamingContext has been loaded
    // from the new checkpoint
    println("Creating new context")
    val outputFile = new File(outputPath)
    if (outputFile.exists()) outputFile.delete()
    val sparkConf = new SparkConf().setAppName("RecoverableNetworkWordCount").setMaster("local[*]")
    // Create the context with a 1 second batch size
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint(checkpointDirectory)

    val droppedWordsCounter = DroppedWordsCounter.getInstance(ssc.sparkContext)

    // val errorLines = ssc.sparkContext.accumulator(0)



    // Create a socket stream on target ip:port and count the
    // words in input stream of \n delimited text (eg. generated by 'nc')


    val lines = ssc.socketTextStream(ip, port)


//    import org.apache.spark.sql._
//    val schemaString = "name age"
//    val schema =
//      StructType(
//        schemaString.split(" ").map(fieldName => StructField(fieldName, StringType, true)))

   // val events = lines.flatMap(_.split("\\|"))
//    val events = lines map{l =>{
//
//      try {
//        val e = l.split("\\|")
//        Customer(e(0), e(1).toInt, e(2), e(3))
//
//      }
//      catch {
//        case e:Exception => {
//          //println("App Event Error Json format")
//          println("Error   format:" + l)
//          Some(null);
//        }
//      }
//    }}

//    val events = lines.flatMap(record => {
//
//      try {
//        val mapper = createMapper()
//        val e=record.split("\\|")
//        Some(mapper.readValue(e, classOf[Person]))
//      } catch {
//
//        case e: Exception => {
//          print("error:",record)
//          None
//        }
//
//      }})

   // val records = lines.flatMap(_.split("\\|"))


//   val events=records.map(record=>{
//           try {
////             val mapper = createMapper()
////
////             Some(mapper.readValue(record, classOf[Person]))
//             Some(Event(record(0),record(1).toInt,record(2),record(3)))
//
//           } catch {
//
//             case e: Exception => {
//               print("error:", record)
//               None
//             }
//           }
//   })



    val  events = lines map( l=>{
     try{
       val e= l.split("\\|")
      //Some(true,Event(e(0), e(1).toInt, e(2), e(3)))
       (true,Event(e(0), e(1).toInt, e(2), e(3)))
     }
     catch {
       case e:
         Exception => {
            //val count =10
            //droppedWordsCounter.add(count)
            //println(s"Dropped ${droppedWordsCounter.value} word(s) totally")
            print("error:", l)
            (false,Some(l))
       }
       }
    })


     val invalidEvents=events.filter(!_._1).map(_._2)

    invalidEvents.foreachRDD( rdd =>{

      rdd.foreach(f=>{
        //droppedWordsCounter.add(1L)
        println(f)
        //println(s"Dropped ${droppedWordsCounter.value} word(s) totally")
      })
    })

    val validEvents=events.filter(_._1).map(_._2)


    /** spark SQL DataFrame */

//     validEvents.foreachRDD((rdd , time: Time )=>{
//       val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
//       import spark.implicits._
//
//     })


     validEvents.foreachRDD { (rdd , time: Time )=>
      rdd.foreachPartition { partitionOfRecords =>
        // ConnectionPool is a static, lazily initialized pool of connections
        //val connection = ConnectionPool.getConnection()
       // partitionOfRecords.foreach(record => connection.send(record))
       // ConnectionPool.returnConnection(connection)  // return to the pool for future reuse

//        val spark = SparkSession.builder.config(rdd.sparkContext.getConf).getOrCreate()
//        import spark.implicits._

       //val graph = JanusGraphFactory.open("conf/janusgraph-berkeleyje-es.properties")

       // val graph = JanusGraphFactory.open("/Hadoop/Janusgraph/janusgraph-0.3.1-hadoop2/conf/janusgraph-cassandra-es.properties")


        //val mapper:GryoMapper = GryoMapper.build.addRegistry(JanusGraphIoRegistry.getInstance()).create()
        //val cluster:Cluster = Cluster.build().serializer(new GryoMessageSerializerV3d0(mapper)).create()
       // val cluster:Cluster =  Cluster.build().serializer(new GryoMessageSerializerV3d0(GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()).create())).create()
        //val client:Client = cluster.connect()


//        val builder = GryoMapper.build.version(GryoVersion.V3_0)
//        builder.addRegistry(JanusGraphIoRegistry.getInstance)
//
//        val cluster = Cluster.build.serializer(new GryoMessageSerializerV3d0(builder)).create
//        val client:Client = cluster.connect()

       // import org.apache.tinkerpop.gremlin.driver.Cluster

        val cluster :Cluster = Cluster.open("/Hadoop/Janusgraph/janusgraph-0.3.1-hadoop2/conf/remote-objects.yaml")
        val graph:Graph = EmptyGraph.instance()
        val  g = graph.traversal().withRemote(DriverRemoteConnection.using(cluster, "g"))

        partitionOfRecords.foreach(
          Records =>{

            val event=Records.asInstanceOf[Event]
            val randomNum =(new Random).nextInt(2000)

            println(randomNum,event)
            //g.V().addV()
           // g.addV("human").property("name","stephen"+randomNum)
           val theseus = g.addV("human").property("name","theseus"+randomNum).next()

            //g.tx().commit()
            println(theseus)

            //结果遍历
//            val result2 =g.V().valueMap(true).next(100)
//            val it=result2.iterator()
//            //println(result2.getClass)
//            //result2.forEach()
//            while (it.hasNext) {
//              val item = it.next()
//              val iterator = item.keySet().iterator()
//              while (iterator.hasNext()) {
//                val key:Object = iterator.next()
//                //System.out.println(s"key->${key}:"+s"value->${item.get(key)}")
//              }
//            }


//            val result2 = client.submit("g.V()").all().get()
//            val it=result2.iterator()
//            while (it.hasNext) println(it.next())
//
//            client.close()


          }
        )
        g.close()
        cluster.close()
      }
    }
//    val wordCounts = words.map((_, 1)).reduceByKey(_ + _)
//    wordCounts.foreachRDD { (rdd: RDD[(String, Int)], time: Time) =>
//      // Get or register the blacklist Broadcast
//      val blacklist = WordBlacklist.getInstance(rdd.sparkContext)
//      // Get or register the droppedWordsCounter Accumulator
//      val droppedWordsCounter = DroppedWordsCounter.getInstance(rdd.sparkContext)
//      // Use blacklist to drop words and use droppedWordsCounter to count them
//      val counts = rdd.filter { case (word, count) =>
//        if (blacklist.value.contains(word)) {
//          droppedWordsCounter.add(count)
//          false
//        } else {
//          true
//        }
//      }.collect().mkString("[", ", ", "]")
//      val output = s"Counts at time $time ${counts}"
//      println(output)
//      println(s"Dropped ${droppedWordsCounter.value} word(s) totally")
//      println(s"Appending to ${outputFile.getAbsolutePath}")
//
//
//      //Files.append(output + "\n", outputFile, Charset.defaultCharset())
//    }
    ssc
  }

  def main(args: Array[String]) {
    if (args.length != 4) {
      System.err.println(s"Your arguments were ${args.mkString("[", ", ", "]")}")
      System.err.println(
        """
          |Usage: eventLoadToDailyGraph <hostname> <port> <checkpoint-directory>
          |     <output-file>. <hostname> and <port> describe the TCP server that Spark
          |     Streaming would connect to receive data. <checkpoint-directory> directory to
          |     HDFS-compatible file system which checkpoint data <output-file> file to which the
          |     word counts will be appended
          |
          |In local mode, <master> should be 'local[n]' with n > 1
          |Both <checkpoint-directory> and <output-file> must be absolute paths
        """.stripMargin
      )
      System.exit(1)
    }
   // val Array(ip, IntParam(port), checkpointDirectory, outputPath) = args
   val Array(ip, "5555", checkpointDirectory, outputPath) = args
    val ssc = StreamingContext.getOrCreate(checkpointDirectory,
      () => createContext(ip, 5050, outputPath, checkpointDirectory))
    ssc.start()
    ssc.awaitTermination()
  }
}
// scalastyle:on println
//Bill|Clinton|68