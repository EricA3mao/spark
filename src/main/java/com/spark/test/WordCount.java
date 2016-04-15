package com.spark.test;

import java.util.Arrays;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * sh spark-submit --class com.spark.test.WordCount --master local[2] --jars /opt/workspace/eric-work/spark/target/spark-1.0.jar 
 *  
 * @author zzg
 *
 */
public class WordCount {
	@SuppressWarnings("serial")
	private static final FlatMapFunction<String, String> WORDS_EXTRACTOR =
		      new FlatMapFunction<String, String>() {
		        @Override
		        public Iterable<String> call(String s) throws Exception {
		          return Arrays.asList(s.split(" "));
		        }
		      };

		  @SuppressWarnings("serial")
		private static final PairFunction<String, String, Integer> WORDS_MAPPER =
		      new PairFunction<String, String, Integer>() {
		        @Override
		        public Tuple2<String, Integer> call(String s) throws Exception {
		          return new Tuple2<String, Integer>(s, 1);
		        }
		      };

		  @SuppressWarnings("serial")
		private static final Function2<Integer, Integer, Integer> WORDS_REDUCER =
		      new Function2<Integer, Integer, Integer>() {
		        @Override
		        public Integer call(Integer a, Integer b) throws Exception {
		        	System.out.println("aaaaaaaaaaaaaaaa="+a+";b="+b);
		          return a + b;
		        }
		      };

		  public static void main(String[] args) {
//		    if (args.length < 1) {
//		      System.err.println("Please provide the input file full path as argument");
//		      System.exit(0);
//		    }
if(args.length != 0){
	System.out.println("args0 : "+args[0]);
}
		    SparkConf conf = new SparkConf().setAppName("org.sparkexample.WordCount").setMaster("local");
		    JavaSparkContext context = new JavaSparkContext(conf);
		    String inputFile = "file:///opt/workspace/eric-work/spark/src/main/resources/loremipsum.txt";
		    JavaRDD<String> file = context.textFile(inputFile, 2);
		    JavaRDD<String> words = file.flatMap(WORDS_EXTRACTOR);
		    JavaPairRDD<String, Integer> pairs = words.mapToPair(WORDS_MAPPER);
		    int psnum = pairs.getNumPartitions();
		    System.out.println("xxxxxxxxxxxxxx="+psnum);
		    
		    JavaPairRDD<String, Integer> counter = pairs.reduceByKey(WORDS_REDUCER);
String outFile = "file:///opt/r.txt";
		    counter.saveAsTextFile(outFile);
		  }

}
