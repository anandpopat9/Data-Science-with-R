hdfs dfs -rm -r ~/output1;
hadoop com.sun.tools.javac.Main WordCoPairs.java;
jar cf wc.jar WordCoPairs*.class;
time hadoop jar wc.jar WordCoPairs ~/input1/ ~/output1

