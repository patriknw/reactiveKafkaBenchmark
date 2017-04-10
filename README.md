# reactiveKafkaBenchmark

add topcis
./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic1 --partitions 1
./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic10 --partitions 10
./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic30 --partitions 30
./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic100 --partitions 100
./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic1000 --partitions 1000


Repo contains:
RequestApp is a load generator
committableSource - benchmark for committableSource
CommittablePartitionedSource - benchmark for committablePartitionedSource

remember to set up proper kafka address !