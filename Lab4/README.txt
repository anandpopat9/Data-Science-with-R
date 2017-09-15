For each activity, build_scripts are given. The compile code and build jar file code is inside 'build_scripts'. Seperate folder for each activity is created. In 'build1.sh', commands to build and compile the code for the respective activity is given.

A Detailed description according to the activity:

ACTIVITY 1:
1. In folder 'wordcloud', go to folder 'python notebook', use file 'lab4_part1_1.ipynb' to download tweets and clean the tweets. The text of tweets along with seperate files for hashtags and mentions will be created.

2. In folder 'wordcloud', inside folder 'input', two files, hashtags.txt and mentions.txt will be there which is the output of the first notebook.

3. These files serve as the input to the wordcount jar of MR. Under 'jars' folder, inside 'activity1', jar file can be found. Input and Output files of this activity are given under corresponding folders.

4. The output file, under 'Output' folder, in 'activity1', is given as the input to the second python notebook found under 'wordcloud' folder, in 'python notebook'

5. Output of this notebook will be a wordcloud.


NOTE: For each activity below, there is a 'build1.sh' file which will remove the output folder from hdfs if one exists, compile the java file, make the jar file, and run that jar file.
While 'delete.sh' file will delete the input folder found in hdfs and upload the new input folder on hdfs.

ACTIVITY 2:
1. Input: 'input->activity2->books'. Input command same as in MRGuide.pdf
2. Java file: 'java->activity2->WordCoPairs.java' and the same for stripes
3. Corresponding class files are given in 'class folder'
4. Output: 'output->activity2->pairs->' and the same for stripes
5. jar file: 'jar->activity1->wc.jar'
6. To run the jar file
	hadoop com.sun.tools.javac.Main WordCoPairs.java;
	jar cf wc.jar WordCoPairs*.class;
	time hadoop jar wc.jar WordCoPairs ~/input1/ ~/output1
NOTE: For stripes, in older version of hadoop, map objects are printed instead of strings.

ACTIVITY 3:
1. Input: 'input->activity3->input2'. Input command same as in MRGuide.pdf
2. Java file: 'java->activity3->WordCoLa.java'
3. Corresponding class files are given in 'class folder'
4. Output: 'output->activity3->'
5. jar file: 'jar->activity3->wc.jar'
6. To run the jar file
	hadoop com.sun.tools.javac.Main WordCoLa.java;
	jar cf wc.jar WordCoLa*.class new_lemmatizer.csv;
	time hadoop jar wc.jar WordCoLa ~/input1/ ~/output1
7. 'new_lemmatizer.csv' is inside corresponding 'java->activity3->new_lemmatizer.csv'

ACTIVITY 4a:
1. Input: 'input->activity4a->input2'. Input command same as in MRGuide.pdf
2. Java file: 'java->activity4a->WordCo4a.java'
3. Corresponding class files are given in 'class folder'
4. Output: 'output->activity4a->'
5. jar file: 'jar->activity4a->wc.jar'
6. To run the jar file
        hadoop com.sun.tools.javac.Main WordCo4a.java;
        jar cf wc.jar WordCo4a*.class new_lemmatizer.csv;
        time hadoop jar wc.jar WordCo4a ~/input1/ ~/output1
7. 'new_lemmatizer.csv' is inside corresponding 'java->activity4a->new_lemmatizer

ACTIVITY 4b:
1. Input: 'input->activity4b->input2'. Input command same as in MRGuide.pdf
2. Java file: 'java->activity4b->WordCo4b.java'
3. Corresponding class files are given in 'class folder'
4. Output: 'output->activity4b->'
5. jar file: 'jar->activity4b->wc.jar'
6. To run the jar file
        hadoop com.sun.tools.javac.Main WordCo4b.java;
        jar cf wc.jar WordCo4b*.class new_lemmatizer.csv;
        time hadoop jar wc.jar WordCo4b ~/input1/ ~/output1
7. 'new_lemmatizer.csv' is inside corresponding 'java->activity4b->new_lemmatizer

PLOT:
1. Both activity 4a and 4b were run on file ranging from 2 to 100. The corresponding times are given in '4a.csv' and '4b.csv' inside 'plot' folder.
2. The 'lab4_plot_4a_4b.ipynb' is used to plot the line graph.
3. '4a.png' and '4b.png' show the graph that is plotted.
