package br.com.gabrielmotta.config.rabbitmq;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

@Component
public class RabbitConfig {
	
	private static final String EXCHANGE_NAME = "amq.direct";
	private static final String QUEUE_NAME = "pedidos";
	
	private AmqpAdmin amqpAdmin;
	
	public RabbitConfig(AmqpAdmin amqpAdmin) {
		this.amqpAdmin = amqpAdmin;
	}
	
	private Queue queue() {
		return new Queue(QUEUE_NAME, true, false, false);
	}
	
	private DirectExchange exchange() {
		return new DirectExchange(EXCHANGE_NAME);
	}
	
	private Binding binding (Queue queue, DirectExchange exchange) {
		return new Binding(queue.getName(),
				Binding.DestinationType.QUEUE, exchange.getName(), queue.getName(), null);
	}
	
	@PostConstruct
	private void createQueue() {
		var ordersQueue = this.queue();
		var exchange = this.exchange();
		var ordersBinding = this.binding(ordersQueue, exchange);
		
		this.amqpAdmin.declareQueue(ordersQueue);
		this.amqpAdmin.declareExchange(exchange);
		this.amqpAdmin.declareBinding(ordersBinding);
	}
}
