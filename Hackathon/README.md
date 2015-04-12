Need OOYLA spark-jab server

build code using gradle

Use the below links to initialise the execution environment in the job server:

curl -d "" 'localhost:8090/contexts/BOSON?num-cpu-cores=8&mem-per-node=2G' 
curl --data-binary @build/libs/Hackathon-1.0.jar localhost:8090/jars/HIGGS

Use the below link to initialize the execution

curl -d "init.string=INIT" 'localhost:8090/jobs?appName=HIGGS&classPath=com.hack.streaming.Main.LoadHotelData&context=BOSON'

Data must be posted to RabbitMQ queue named com.hack.queue, user name is guest, passwd is guest

Use the below links to receive some simple statistics on a moving window of 10 seconds

curl -d "init.getdata=FREQUENT_SEARCHES" 'localhost:8090/jobs?appName=HIGGS&classPath=com.hack.streaming.Main.ReadHotelData&context=BOSON&sync=true'

curl -d "init.getdata=FREQUENT_PAGES" 'localhost:8090/jobs?appName=HIGGS&classPath=com.hack.streaming.Main.ReadHotelData&context=BOSON&sync=true'







