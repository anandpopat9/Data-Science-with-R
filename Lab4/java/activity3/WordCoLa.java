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

public class WordCoLa
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
	//			System.out.println(hm.get(items.get(0)));
			}
        	}

    		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		{
     			// StringTokenizer itr = new StringTokenizer(value.toString());
			//cannot use stringtokenizer. No index to find neighbours
			String[] text=value.toString().split("\\t");
		//	System.out.println(text[1]);
			if(text.length>=2)
			{
			String[] word1=text[1].split("\\s+");
			for (int x=0;x<word1.length;x++) 
			{
				word1[x]=word1[x].replaceAll("j","i");
				word1[x]=word1[x].replaceAll("v","u");

				//check the lemmatizer for normalized spelling
				if(hm.containsKey(word1[x]))
				{
					String temp=hm.get(word1[x]).toString();
				//	System.out.println(temp);
					w=new Text(temp);
					v=new Text(text[0]);
					context.write(w,v);
				}
				else
				{
					w=new Text(word1[x]);
					v=new Text(text[0]);
					context.write(w,v);
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
			context.write(key,result);

    		}
  	}	

  	public static void main(String[] args) throws Exception 
	{
    		Configuration conf = new Configuration();
		
	//	conf.set("lemmas",sc);
    		Job job = Job.getInstance(conf, "word count");
    		job.setJarByClass(WordCoLa.class);
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
