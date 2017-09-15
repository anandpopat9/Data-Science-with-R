hdfs dfs -rm -r ~/output1;
hadoop com.sun.tools.javac.Main WordCo4a.java;
jar cf wc.jar WordCo4a*.class new_lemmatizer.csv;
time hadoop jar wc.jar WordCo4a ~/input1/ ~/output1

