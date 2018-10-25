package Graph;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.MessageSerializer;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV3d0;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoMapper;
import org.apache.tinkerpop.gremlin.structure.io.gryo.GryoVersion;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;

public class Test {

    public static void main(String args[]) {
        //GryoMapper mapper = GryoMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()).create();
//        final GryoMapper.Builder builder = GryoMapper.build().version(GryoVersion.V3_0);
//        builder.addRegistry(JanusGraphIoRegistry.getInstance());
//
//        Cluster cluster = Cluster.build().serializer(new GryoMessageSerializerV3d0(builder)).create();
//        Client client = cluster.connect();
//        try {
//            client.submit("g.V()").all().get();
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            //System.out.print(e.printStackTrace());
//        }


    }

}
