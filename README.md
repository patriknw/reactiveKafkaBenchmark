# reactiveKafkaBenchmark

add topics
<ul>
<li>./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic1 --partitions 1</li>
<li>./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic10 --partitions 10</li>
<li>./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic30 --partitions 30</li>
<li>./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic100 --partitions 100</li>
<li>./kafka-topics.sh --zookeeper 192.168.100.100 --create --replication-factor 1 --topic testTopic1000 --partitions 1000</li>
</ul>

Repo contains:
<ul>
<li>RequestApp is a load generator</li>
<li>committableSource - benchmark for committableSource</li>
<li>CommittablePartitionedSource - benchmark for committablePartitionedSource</li>
</ul>

remember to set up proper kafka address !