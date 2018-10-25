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