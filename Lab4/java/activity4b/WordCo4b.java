import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.List;

public class WordCo4b
{
	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>
	{
		//private final static IntWritable one = new IntWritable(1);
    		private Text w;
		private Text v;
		private HashMap hm;

		protected void setup(Context context) throws IOException, InterruptedException 
		{
			File file=new File("new_lemmatizer.csv");
			Scanner sc=new Scanner(file);
        		hm = new HashMap();
			while(sc.hasNextLine())
			{
				String line=sc.nextLine();
				List<String> items = Arrays.asList(line.split("\\s*,\\s*"));
			//	sc1.useDelimiter(",");
				hm.put(items.get(0),items.get(1));
			}
        	}

    		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		{
     			// StringTokenizer itr = new StringTokenizer(value.toString());
			//cannot use stringtokenizer. No index to find neighbours
			String[] text=value.toString().split("\\t");	
		//	System.out.println(text[0]);
			if(text.length>=2)
			{
		//	System.out.println("inside");
			String[] word1=text[1].split("\\s+");
			for (int x=1;x<word1.length;x++) 
			{
				if(x-1>=0 && x+1<word1.length)
				{
					
					word1[x]=word1[x].replaceAll("j","i");
					word1[x]=word1[x].replaceAll("v","u");
					word1[x-1]=word1[x-1].replaceAll("j","i");
					word1[x-1]=word1[x-1].replaceAll("v","u");
					word1[x+1]=word1[x+1].replaceAll("j","i");
					word1[x+1]=word1[x+1].replaceAll("v","u");	

					//check the lemmatizer for normalized spelling
					String word_n1="";
					String word_n2="";
					String word_n3="";
					word_n1=(hm.containsKey(word1[x]))?hm.get(word1[x]).toString():word1[x];
					word_n2=(hm.containsKey(word1[x-1]))?hm.get(word1[x-1]).toString():word1[x-1];
					word_n3=(hm.containsKey(word1[x+1]))?hm.get(word1[x+1]).toString():word1[x+1];	
					
					String total=word_n1+","+word_n2+","+word_n3;
			//		System.out.println(total);
					Text u=new Text(total);
					Text v=new Text(text[0]);
					context.write(u,v);	
				}
			}
			}
		}	
}

	public static class IntSumReducer extends Reducer<Text,Writable,Text,Writable> 
	{
    		private Text result=new Text();

		public void reduce(Text key, Iterable<Writable> values, Context context) throws IOException, InterruptedException 
		{
      			String total="";
      			for (Writable val : values) 
			{
        			total=total+val.toString()+",";
      			}
      			result.set(total);
      			context.write(key, result);
    		}
  	}	

  	public static void main(String[] args) throws Exception 
	{
    		Configuration conf = new Configuration();
		
	//	conf.set("lemmas",sc);
    		Job job = Job.getInstance(conf, "word count");
    		job.setJarByClass(WordCo4b.class);
    		job.setMapperClass(TokenizerMapper.class);
    		job.setCombinerClass(IntSumReducer.class);
    		job.setReducerClass(IntSumReducer.class);
    		job.setOutputKeyClass(Text.class);
    		job.setOutputValueClass(Text.class);
    		FileInputFormat.addInputPath(job, new Path(args[0]));
    		FileOutputFormat.setOutputPath(job, new Path(args[1]));
    		System.exit(job.waitForCompletion(true) ? 0 : 1);
  	}
}
