## 踩坑
1. 置服务注册中心的URL, 应该用：service-url，而不是server-url  
2. Mac机器要配置一下/etc/hosts 把各个yml文件的IP-host映射，比如`hostname: eureka-7901 或eureka-7902等`中的`eureka-7901`映射给127.0.0.1. PS: 
   每个域名都对应一个IP地址，但一个IP 地址可有对应多个域名
3. Eureka集群内部不可以像这样注册：A->B, B->C, C->A. A向B注册，B会把A上的服务同步到C。当前节点接收到注册之后会往后面的节点（peer）推送它上面的所有注册的节点
## 手写Eureka注册和查看

查看：`curl -GET http://127.0.0.1:7901/eureka/apps/api-passenger` 前提是`instance-id: api-passenger`的服务已经注册过了

## 笔记
集群下是一个服务名对应多个IP，然后根据策略分发，由Ribbon决定