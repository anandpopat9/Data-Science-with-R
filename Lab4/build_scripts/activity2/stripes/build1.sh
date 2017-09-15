hdfs dfs -rm -r ~/output1;
hadoop com.sun.tools.javac.Main WordCoStripes.java;
jar cf wc.jar WordCoStripes*.class;
time hadoop jar wc.jar WordCoStripes ~/input1/ ~/output1

