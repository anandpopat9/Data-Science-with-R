import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCoPairs
{
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable>
	{
		private final static IntWritable one = new IntWritable(1);
    		private Text word = new Text();

    		public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
		{
			//cannot use stringtokenizer. No index to find neighbours
			String[] text=value.toString().split("\\s+");	
			for (int x=0;x<text.length;x++) 
			{
				for (int y=x;y<text.length;y++)
				{
					if(y!=x)
					{
						String pair=text[x]+", "+text[y];
						word.set(pair);
						context.write(word,one);
					}
				}
			}
		}
	}

	public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable> 
	{
    		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
		{
      			int sum = 0;
      			for (IntWritable val : values) 
			{
        			sum += val.get();
      			}
      			result.set(sum);
      			context.write(key, result);
    		}
  	}	

  	public static void main(String[] args) throws Exception 
	{
    		Configuration conf = new Configuration();
    		Job job = Job.getInstance(conf, "word count");
    		job.setJarByClass(WordCoPairs.class);
    		job.setMapperClass(TokenizerMapper.class);
    		job.setCombinerClass(IntSumReducer.class);
    		job.setReducerClass(IntSumReducer.class);
    		job.setOutputKeyClass(Text.class);
    		job.setOutputValueClass(IntWritable.class);
    		FileInputFormat.addInputPath(job, new Path(args[0]));
    		FileOutputFormat.setOutputPath(job, new Path(args[1]));
    		System.exit(job.waitForCompletion(true) ? 0 : 1);
  	}
}
