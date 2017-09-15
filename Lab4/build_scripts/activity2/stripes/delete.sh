hdfs dfs -rm -r ~/input1;
hdfs dfs -mkdir -p ~/input1;
hdfs dfs -put ~/books/* ~/input1;
hdfs dfs -ls ~/input1/
