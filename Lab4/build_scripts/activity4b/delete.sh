hdfs dfs -rm -r ~/input1;
hdfs dfs -mkdir -p ~/input1;
hdfs dfs -put ~/input2/* ~/input1;
hdfs dfs -ls ~/input1/
