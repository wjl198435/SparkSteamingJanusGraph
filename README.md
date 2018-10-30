# Spark streaming

## 1 实时入库
### 1. bin/JanusGraph start  [启动图数据库 默认storage backend cassandra，Index backend ES]
### 2. nc -lk 5050  启动数据生产服务
### 3. 打开IDEA 启动运行spark streaming  eventLoadToDailyGraph.scala
### 4. 在 数据生产终端输入  dsf|200|abc|test 测试数据

## 2 基于spark引擎进行数据遍历 关系分析
 ### 1.graph = GraphFactory.open('conf/hadoop-graph/hadoop-gryo.properties')  【选择数据库】
 ### 2.g = graph.traversal().withComputer(SparkGraphComputer)      【获取遍历实例】
 ### 3 g.V().count() 【基于Spark 关系图进行数据分析 ，数据遍历】
 
 
 
 
 #BulkLoad example
 
 ## 1. gremlin> hdfs.copyFromLocal('data/grateful-dead.kryo', 'grateful-dead.kryo')
 ## 2.gremlin> readGraph = GraphFactory.open('conf/hadoop-graph/hadoop-gryo.properties')
 
 ## 3.gremlin> writeGraph = 'conf/janusgraph-cassandra-es.properties'
 ## 4.gremlin>blvp = BulkLoaderVertexProgram.build().keepOriginalIds(false).writeGraph(writeGraph).create(readGraph)
 ## 5.gremlin> readGraph.compute(SparkGraphComputer).workers(1).program(blvp).submit().get()
 ### 
     1. :plugin use tinkerpop.hadoop
     2.:plugin use tinkerpop.spark
     
 ## 6 gremlin> :set max-iteration 10  
     graph = GraphFactory.open(writeGraph)
 ## 7.gremlin> g=graph.traversal()
 
 ## 8. gremlin> g.V().valueMap()
 
 ### 
 
 '# hadoop-grateful-gryo.properties
 
 
 //# Hadoop Graph Configuration
 //#
 gremlin.graph=org.apache.tinkerpop.gremlin.hadoop.structure.HadoopGraph
 gremlin.hadoop.graphReader=org.apache.tinkerpop.gremlin.hadoop.structure.io.gryo.GryoInputFormat
 gremlin.hadoop.graphWriter=org.apache.hadoop.mapreduce.lib.output.NullOutputFormat
 gremlin.hadoop.inputLocation=grateful-dead.kryo
 gremlin.hadoop.outputLocation=output
 gremlin.hadoop.jarsInDistributedCache=true
 
 //#
 //# GiraphGraphComputer Configuration
 
 
 //#
 giraph.minWorkers=1
 giraph.maxWorkers=1
 giraph.useOutOfCoreGraph=true
 giraph.useOutOfCoreMessages=true
 mapred.map.child.java.opts=-Xmx1024m
 mapred.reduce.child.java.opts=-Xmx1024m
 giraph.numInputThreads=4
 giraph.numComputeThreads=4
 giraph.maxMessagesInMemory=100000
 
 //# tinkergraph-gryo.properties
 
 gremlin.graph=org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph
 gremlin.tinkergraph.graphFormat=gryo
 gremlin.tinkergraph.graphLocation=/tmp/tinkergraph.kryo
 
 
 
   
 
 