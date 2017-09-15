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
			for j in range(i,len(s2)):
				if(i is not j):
					s2[i]=s2[i].replace("j","i")
					s2[i]=s2[i].replace("v","u")
					s2[j]=s2[j].replace("j","i")
					s2[j]=s2[j].replace("v","u")

				#return (s2[i]+","+s2[j],s1)
					if s2[i] in dict and s2[j] in dict:
						return (dict[s2[i]]+","+dict[s2[j]],s1[0])
					if s2[i] in dict and s2[j] not in dict:
						return (dict[s2[i]]+","+s2[j],s1[0])
					if s2[i] not in dict and s2[j] in dict:
						return (s2[i]+","+dict[s2[j]],s1[0])
					if s2[i] not in dict and s2[j] not in dict:
						return (s2[i]+","+s2[j],s1[0])
	return("Jane","Doe")

mapper=input_file.map(myfunc)
reducer=mapper.reduceByKey(lambda a,b:a+b)
reducer=reducer.sortByKey()
reducer.saveAsTextFile("output_2grams")