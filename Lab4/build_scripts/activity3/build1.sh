hdfs dfs -rm -r ~/output1;
hadoop com.sun.tools.javac.Main WordCoLa.java;
jar cf wc.jar WordCoLa*.class new_lemmatizer.csv;
time hadoop jar wc.jar WordCoLa ~/input1/ ~/output1

