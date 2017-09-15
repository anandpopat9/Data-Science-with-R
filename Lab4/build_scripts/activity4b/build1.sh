hdfs dfs -rm -r ~/output1;
hadoop com.sun.tools.javac.Main WordCo4b.java;
jar cf wc.jar WordCo4b*.class new_lemmatizer.csv;
time hadoop jar wc.jar WordCo4b ~/input1/ ~/output1

