package nsa.datawave.query.rewrite.postprocessing.tf;

import java.util.Collections;
import java.util.Map;

import nsa.datawave.query.rewrite.attributes.Document;
import nsa.datawave.query.util.Tuple2;
import nsa.datawave.query.util.Tuple3;
import nsa.datawave.query.util.Tuples;

import org.apache.accumulo.core.data.Key;

import com.google.common.base.Function;

public class EmptyTermFrequencyFunction implements Function<Tuple2<Key,Document>,Tuple3<Key,Document,Map<String,Object>>> {
    
    private final static Map<String,Object> emptyContext = Collections.emptyMap();
    
    @Override
    public Tuple3<Key,Document,Map<String,Object>> apply(Tuple2<Key,Document> from) {
        return Tuples.tuple(from.first(), from.second(), emptyContext);
    }
    
}