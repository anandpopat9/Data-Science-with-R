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
import java.util.Set;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Writable;
public class WordCoStripes 
{
	public static class TokenizerMapper extends Mapper<Object, Text, Text, MapWritable>{

	private IntWritable one=new IntWritable(1);
	private MapWritable map=new MapWritable();
    	private Text word = new Text();

    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException 
	{
		//cannot use stringtokenizer. No index to find neighbours
		String[] text=value.toString().split("\\s+");
		for (int x=0;x<text.length;x++) 
		{
			map.clear();
			word.set(text[x]);
			for (int y=x;y<text.length;y++)
			{
				if(y!=x)
				{
					Text u=new Text(text[y]);
					if(map.containsKey(u))
					{
						IntWritable val=(IntWritable)map.get(u);
						int temp=val.get();
						val.set(temp+1);
						map.put(u,val);
					}
					else
					{
						map.put(u,one);
					}
				}
			}
			context.write(word,map);
		}
	}
}
public static class IntSumReducer extends Reducer<Text,MapWritable,Text,MapWritable> 
{
	private MapWritable result_map=new MapWritable();
	public void reduce(Text key, Iterable<MapWritable> values, Context context) throws IOException, InterruptedException 
	{
      		result_map.clear();
      		for (MapWritable val : values) 
		{
        		Set<Writable> set=val.keySet();
			for(Writable key1:set)
			{
				if(result_map.containsKey(key1))
				{
					IntWritable value=(IntWritable)val.get(key1);
					IntWritable total=(IntWritable)result_map.get(key);
					int temp=total.get();
					total.set(temp+value.get());
					result_map.put(key,total);
				}
				else
				{
					IntWritable value=(IntWritable)val.get(key1);
					result_map.put(key,value);
				}
			}
		}
		context.write(key,result_map);
			
      }
}
  

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCoStripes.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(IntSumReducer.class);
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(MapWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
