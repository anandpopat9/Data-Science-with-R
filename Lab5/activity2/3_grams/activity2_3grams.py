import pyspark  
from pyspark.mllib.regression import LabeledPoint
from pyspark.mllib.classification import LogisticRegressionWithSGD
from pyspark.mllib.tree import DecisionTree

sc = pyspark.SparkContext()
input_file=sc.textFile("input")
lemma=open("new_lemmatizer.csv","rb")
dict=dict()
for lines in lemma:
	lines1=lines.split(",")
	dict[lines1[0]]=lines1[1]

def myfunc(s):
	s1=s.split("\t")
	if(len(s1)>=2):
		s2=s1[1].split(" ")
		for i in range(0,len(s2)):
			if(i-1>=0 and i+1<len(s2)):
				s2[i]=s2[i].replace("j","i")
				s2[i]=s2[i].replace("v","u")
				s2[i-1]=s2[i-1].replace("j","i")
				s2[i-1]=s2[i-1].replace("v","u")
				s2[i+1]=s2[i+1].replace("j","i")
				s2[i+1]=s2[i+1].replace("v","u")

				word1=dict[s2[i]] if s2[i] in dict else s2[i]
				word2=dict[s2[i-1]] if s2[i-1] in dict else s2[i-1]
				word3=dict[s2[i+1]] if s2[i+1] in dict else s2[i+1]

				total=word1+","+word2+","+word3
				return (total,s1[0])
	return ("Jane","Doe")

mapper=input_file.map(myfunc)
reducer=mapper.reduceByKey(lambda a,b:a+b)
reducer=reducer.sortByKey()
reducer.saveAsTextFile("output_3grams")