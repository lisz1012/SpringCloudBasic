## 踩坑
1. 置服务注册中心的URL, 应该用：service-url，而不是server-url  
2. Mac机器要配置一下/etc/hosts 把各个yml文件的IP-host映射，比如`hostname: eureka-7901 或eureka-7902等`中的`eureka-7901`映射给127.0.0.1. PS: 
   每个域名都对应一个IP地址，但一个IP 地址可有对应多个域名