package com.wrc.cloud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wrc
 * @description: TODO
 * @date : 2023/3/27 22:42
 */
@Configuration
public class DirectRabbitConfig {

    /**
     * 配置交换机，由交换机将消息分发至 Queue
     * */
    @Bean
    DirectExchange AnalysisDirectExchange() {
        //Direct交换机 起名：TestDirectExchange
        //  return new DirectExchange("TestDirectExchange",true,true);
        return new DirectExchange("Cloud.AnalysisExchange",true,false);
    }

    /**
     * 用于汉语分析的队列
     * */
    @Bean
    public Queue AnalysisDirectQueue() {
        //队列 起名：TestDirectQueue
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue("AnalysisOrderQueue",true);
    }

    @Bean
    Binding bindingDirect() {
        //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
        return BindingBuilder.bind(AnalysisDirectQueue()).to(AnalysisDirectExchange()).with("AnalysisOrderRoute");
    }

    /**
     * 用于 英语分析（或者说英语为主，tweet通用） 的队列
     * */
    @Bean
    public Queue AnalysisDirectQueueEN(){
        return new Queue("AnalysisOrderQueueEN", true);
    }

    @Bean
    Binding bindingDirectEN() {
        //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
        return BindingBuilder.bind(AnalysisDirectQueueEN()).to(AnalysisDirectExchange()).with("AnalysisOrderRouteEN");
    }



    /**
     * 用于 冰糖橙价格分析 的队列
     * */
    @Bean
    public Queue PricePredictQueueBTC(){
        return new Queue("PricePredictQueueBTC", true);
    }

    @Bean
    Binding bindingDirectPricePredictQueueBTC() {
        //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
        return BindingBuilder.bind(PricePredictQueueBTC())
                .to(AnalysisDirectExchange())
                .with("PricePredictRouteBTC");
    }


}
