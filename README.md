# Spark_Streaming

Basic Memory Streaming

## To submit spark streaming app on your linux system.

1.)First install jdk 8.

2.)Second install sbt
  * Ubuntu and other Debian-based distributions.
    Hit following commands on your terminal to install sbt.(Source Link-> https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html)

	echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
	sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
	sudo apt-get update
	sudo apt-get install sbt

3.) Edit all the patquet save paths in streaming_app.scala, to your desire location.

4.) sbt package  <-to make a spark package (run sbt package command in your project directory home).  

5.) submit spark app For example ->  spark-submit --class com.akshitbhatia.spark.streaming_app /home/akshit/Desktop/Spark_Streaming/target/scala-2.11/spark_project_2.11-0.1.jar  
