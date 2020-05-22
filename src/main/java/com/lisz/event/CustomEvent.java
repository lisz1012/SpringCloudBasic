package com.lisz.event;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEvent {

    @EventListener
    public void listen(EurekaInstanceCanceledEvent e) {
        // 写在Eureka Server这里，一旦有服务下线，所有的Eureka集群中的机器都会listen到并作出相应
        // 实验：相继下线每一个Eureka Server的机器，直到全部下线也不影响服务提供方和消费方互相调用
        // 但会有报错：DiscoveryClient_API-PASSENGER/api-passenger - was unable to refresh its cache! status = Cannot execute request on any known server
        // 重新启动各台 Eureka Server 会发现服务的提供者和消费者自动又连了上来 ^_^
        // EurekaInstanceCanceledEvent 继承了ApplicationEvent，其publish和使用可见：https://blog.csdn.net/u014231523/article/details/76241152
        System.out.println(e.getServerId() + " 下线了");
    }
}
