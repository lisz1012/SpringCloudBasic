## 踩坑
1. 置服务注册中心的URL, 应该用：service-url，而不是server-url  
2. Mac机器要配置一下/etc/hosts 把各个yml文件的IP-host映射，比如`hostname: eureka-7901 或eureka-7902等`中的`eureka-7901`映射给127.0.0.1. PS: 
   每个域名都对应一个IP地址，但一个IP 地址可有对应多个域名
3. Eureka集群内部不可以像这样注册：A->B, B->C, C->A. A向B注册，B会把A上的服务同步到C。当前节点接收到注册之后会往后面的节点（peer）推送它上面的所有注册的节点
## 手写Eureka注册和查看

查看：`curl -GET http://127.0.0.1:7901/eureka/apps/api-passenger` 前提是`instance-id: api-passenger`的服务已经注册过了

yml配置分段, 用"---"：
```
spring:
  profiles: 7901
...
...
---
spring:
  profiles: 7902
...
...
```

## 笔记
集群下是一个服务名对应多个IP，然后根据策略分发，由Ribbon决定。  
Eureka也可以单独使用。不管是服务的提供者还是消费者，都是Eureka server的客户端。他们会向Eureka server注册、续约、下线、获取注册表信息。服务的消费者获取到注册表
信息之后，它本地就有了服务名和其对应IP地址，调用服务的时候就直接通过http调用了，就不通过Eureka了。所以没有Eureka server就不能调用了，这种说法是不对的。只要服务
提供者不挂、不改服务名和IP就可以。注册表在Eureka Server那里是这样的：Map<name, Map<id, InstanceInfo>>. Eureka Server可以接受别人的和拉取注册，互相之间可以
同步共享注册表。手动注册获取注册表：`https://github.com/Netflix/eureka/wiki/Eureka-REST-operations`  

自己做一个服务发现注册中心该怎么做？

com.netflix.discovery.DiscoveryClient中有所有客户端与Eureka Server之间的互动：从Eureka Server拉取注册表、服务注册、初始化心跳、缓存刷新（定时拉取注册表信息），
按需注册定时任务等，贯穿了Eureka Client启动阶段的各项任务。eurreka.client yml配置对应的就是EurekaClientConfigBean这个类. 增量拉取，以3分钟为一个delta。拉取
完了之后进行服务注册. 心跳计时器每隔30秒renew一次：renewalIntervalInSecs。监控网页上的红字就是因为自我保护机制被触发了，自我保护模式下不会剔除注册表中的条目，等
续约请求回来之后，再退出自我保护模式. 不来续约的达到一定的阈值就开启自我保护了，是服务端的功能

eureka-core下的com.netflix.eureka.resourcesInstanceResource是用来接受注册的

注册信息的集群同步有两种方式： 1. 拉取peer的 2. 别人来注册时同步到peer