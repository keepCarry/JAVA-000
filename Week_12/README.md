

1.1 Redis主从复制

- 根据启动端口分别配置文件，分别启动在6379/6380,6379为主。
[Redis配置](http://download.redis.io/redis-stable/redis.conf)
```shell script
>>/conf/redis79.conf

port 6379
bind 127.0.0.1
daemonize no
dbfilename "dump-6379.rdb"
dir ./

>>/conf/redis80.conf

port 6380
bind 127.0.0.1
daemonize no
dbfilename "dump-6380.rdb"
dir ./
replicaof redis-1 6379
```
- docker-compose配置文件

```shell script
version: "3.3"
services:
  redis-1:
    image: "redis:6.0.9-alpine"
    container_name: redis-1
    command: redis-server /usr/local/etc/redis/redis.conf --bind redis-1
    volumes:
      - ./conf/redis79.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 6379:6379

  redis-2:
    image: "redis:6.0.9-alpine"
    container_name: redis-2
    command: redis-server /usr/local/etc/redis/redis.conf --bind redis-2
    volumes:
      - ./conf/redis80.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 6380:6379
```
- 启动命令

```shell script
[root@iZwz9ikgrkjzojrq6oz588Z redis-replica]# docker-compose up -d
[root@iZwz9ikgrkjzojrq6oz588Z redis-replica]# redis-cli -p 6379
127.0.0.1:6379> set name helloworld
127.0.0.1:6379> get name
"helloworld"

[root@iZwz9ikgrkjzojrq6oz588Z redis-replica]# redis-cli -p 6380
127.0.0.1:6380> get name
"helloworld"
```

1.2 Redis哨兵模式
- 引用redis79.conf,redis80.conf的配置部署

- sentinel配置文件如下
```shell script
>>/confi/sentinel.conf

sentinel monitor redis-1 redis-1 6379 1
sentinel down-after-milliseconds redis-1 60000
sentinel failover-timeout redis-1 180000
sentinel parallel-syncs redis-1 1
```
- docker-compose配置文件
```shell script
version: "3.3"
services:
  redis-1:
    image: "redis:6.0.9-alpine"
    container_name: redis-1
    command: redis-server /usr/local/etc/redis/redis.conf --bind redis-1
    volumes:
      - ./conf/redis79.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 6379:6379

  redis-2:
    image: "redis:6.0.9-alpine"
    container_name: redis-2
    command: redis-server /usr/local/etc/redis/redis.conf --bind redis-2
    volumes:
      - ./conf/redis80.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 6380:6379

  sentinel:
    image: "redis:6.0.9-alpine"
    container_name: sentinel
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf --bind sentinel
    volumes:
      - ./conf/sentinel.conf:/usr/local/etc/redis/sentinel.conf
    ports:
      - 26379:26379
```
- 命令
```shell script
[root@iZwz9ikgrkjzojrq6oz588Z redis-sentinel]# docker-compose up -d
[root@iZwz9ikgrkjzojrq6oz588Z redis-sentinel]# redis-cli -p 6379 info | grep role
role:master
[root@iZwz9ikgrkjzojrq6oz588Z redis-sentinel]# redis-cli -p 6380 info | grep role 
role:slave
[root@iZwz9ikgrkjzojrq6oz588Z redis-sentinel]# docker stop redis-1
redis-1
[root@iZwz9ikgrkjzojrq6oz588Z ~]# docker logs -f sentinel
1:X 07 Jan 2021 07:38:49.797 # oO0OoO0OoO0Oo Redis is starting oO0OoO0OoO0Oo
1:X 07 Jan 2021 07:38:49.797 # Redis version=6.0.9, bits=64, commit=00000000, modified=0, pid=1, just started
1:X 07 Jan 2021 07:38:49.797 # Configuration loaded
1:X 07 Jan 2021 07:38:49.814 * Running mode=sentinel, port=26379.
1:X 07 Jan 2021 07:38:49.814 # WARNING: The TCP backlog setting of 511 cannot be enforced because /proc/sys/net/core/somaxconn is set to the lower value of 128.
1:X 07 Jan 2021 07:38:49.830 # Could not rename tmp config file (Resource busy)
1:X 07 Jan 2021 07:38:49.830 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Invalid argument
1:X 07 Jan 2021 07:38:49.830 # Sentinel ID is d308b11e25d8fd38bfc520d11dba757d0daa61fc
1:X 07 Jan 2021 07:38:49.830 # +monitor master redis-1 192.168.80.4 6379 quorum 1
1:X 07 Jan 2021 07:38:50.859 * +slave slave 192.168.80.2:6379 192.168.80.2 6379 @ redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:38:50.862 # Could not rename tmp config file (Resource busy)
1:X 07 Jan 2021 07:38:50.862 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Invalid argument
1:X 07 Jan 2021 07:42:04.731 # +sdown master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.731 # +odown master redis-1 192.168.80.4 6379 #quorum 1/1
1:X 07 Jan 2021 07:42:04.731 # +new-epoch 1
1:X 07 Jan 2021 07:42:04.731 # +try-failover master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.734 # Could not rename tmp config file (Resource busy)
1:X 07 Jan 2021 07:42:04.734 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Invalid argument
1:X 07 Jan 2021 07:42:04.734 # +vote-for-leader d308b11e25d8fd38bfc520d11dba757d0daa61fc 1
1:X 07 Jan 2021 07:42:04.734 # +elected-leader master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.734 # +failover-state-select-slave master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.835 # +selected-slave slave 192.168.80.2:6379 192.168.80.2 6379 @ redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.835 * +failover-state-send-slaveof-noone slave 192.168.80.2:6379 192.168.80.2 6379 @ redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.891 * +failover-state-wait-promotion slave 192.168.80.2:6379 192.168.80.2 6379 @ redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.966 # Could not rename tmp config file (Resource busy)
1:X 07 Jan 2021 07:42:04.966 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Invalid argument
1:X 07 Jan 2021 07:42:04.966 # +promoted-slave slave 192.168.80.2:6379 192.168.80.2 6379 @ redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:04.966 # +failover-state-reconf-slaves master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:05.034 # +failover-end master redis-1 192.168.80.4 6379
1:X 07 Jan 2021 07:42:05.034 # +switch-master redis-1 192.168.80.4 6379 192.168.80.2 6379
1:X 07 Jan 2021 07:42:05.034 * +slave slave 192.168.80.4:6379 192.168.80.4 6379 @ redis-1 192.168.80.2 6379
1:X 07 Jan 2021 07:42:05.038 # Could not rename tmp config file (Resource busy)
1:X 07 Jan 2021 07:42:05.038 # WARNING: Sentinel was not able to save the new configuration on disk!!!: Invalid argument
[root@iZwz9ikgrkjzojrq6oz588Z redis-sentinel]# redis-cli -p 6380 info | grep role
role:master
```

1.3 Redis集群模式
- 配置redis节点1-6，端口分别是7000,7001,7002,7003,7004,7005。redis.conf配置如下
```shell script
bind 0.0.0.0
protected-mode no
port 7000
daemonize no
pidfile /var/run/redis_7000.pid
dir ./
cluster-enabled yes
cluster-config-file nodes-7000.conf
cluster-require-full-coverage no
```

- docker-compose配置文件
```shell script
version: '3'
services:
  redis1:
    image: redis
    container_name: redis1
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis1.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7000:7000

  redis2:
    image: redis
    container_name: redis2
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis2.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7001:7001

  redis3:
    image: redis
    container_name: redis3
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis3.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7002:7002

  redis4:
    image: redis
    container_name: redis4
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis4.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7003:7003

  redis5:
    image: redis
    container_name: redis5
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis5.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7004:7004

  redis6:
    image: redis
    container_name: redis6
    network_mode: host
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis6.conf:/usr/local/etc/redis/redis.conf
    ports:
      - 7005:7005
```

- 使用docker-compose up 启动docker compose配置文件
- 创建redis cluster
```shell script
# 进入容器
docker exec -it redis1 /bin/bash
# 切换至指定目录
cd /usr/local/bin
# 创建redis 集群
# replicas 1 表示集群中的每个主节点创建一个从节点
redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1
[root@iZwz9ikgrkjzojrq6oz588Z redis-cluster]# docker exec -it redis1 /bin/bash
root@iZwz9ikgrkjzojrq6oz588Z:/usr/local/bin# redis-cli --cluster create 127.0.0.1:7000 127.0.0.1:7001 127.0.0.1:7002 127.0.0.1:7003 127.0.0.1:7004 127.0.0.1:7005 --cluster-replicas 1

>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 127.0.0.1:7004 to 127.0.0.1:7000
Adding replica 127.0.0.1:7005 to 127.0.0.1:7001
Adding replica 127.0.0.1:7003 to 127.0.0.1:7002
>>> Trying to optimize slaves allocation for anti-affinity
[WARNING] Some slaves are in the same host as their master
M: 5af1175453a699aa278a9da12875694ae92b9af1 127.0.0.1:7000
   slots:[0-5460] (5461 slots) master
M: 2a6c832151034626ba04ebb4455dc728baa0045b 127.0.0.1:7001
   slots:[5461-10922] (5462 slots) master
M: d7232d6c9834e046196d2db1da2b4a80e45c9c8e 127.0.0.1:7002
   slots:[10923-16383] (5461 slots) master
S: 78cb32d01237fb0d3c84507c5191a0a1a9646f8b 127.0.0.1:7003
   replicates d7232d6c9834e046196d2db1da2b4a80e45c9c8e
S: a3837c59b2a868fcd785259e3320e1968c9144d5 127.0.0.1:7004
   replicates 5af1175453a699aa278a9da12875694ae92b9af1
S: e434ae515a5ea420a25017899a657389ef399aef 127.0.0.1:7005
   replicates 2a6c832151034626ba04ebb4455dc728baa0045b
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.
>>> Performing Cluster Check (using node 127.0.0.1:7000)
M: 5af1175453a699aa278a9da12875694ae92b9af1 127.0.0.1:7000
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 78cb32d01237fb0d3c84507c5191a0a1a9646f8b 127.0.0.1:7003
   slots: (0 slots) slave
   replicates d7232d6c9834e046196d2db1da2b4a80e45c9c8e
M: 2a6c832151034626ba04ebb4455dc728baa0045b 127.0.0.1:7001
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: e434ae515a5ea420a25017899a657389ef399aef 127.0.0.1:7005
   slots: (0 slots) slave
   replicates 2a6c832151034626ba04ebb4455dc728baa0045b
M: d7232d6c9834e046196d2db1da2b4a80e45c9c8e 127.0.0.1:7002
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: a3837c59b2a868fcd785259e3320e1968c9144d5 127.0.0.1:7004
   slots: (0 slots) slave
   replicates 5af1175453a699aa278a9da12875694ae92b9af1
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

## 参考链接
- [配置 redis 的主从复制，sentinel 高可用，Cluster 集群](https://github.com/Johar77/JAVA-000/tree/main/Week_12)