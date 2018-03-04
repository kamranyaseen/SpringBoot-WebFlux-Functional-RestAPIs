package com.kamranyaseen.webflux.repo.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.kamranyaseen.webflux.model.Customer;
import com.kamranyaseen.webflux.repo.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository{
	private Map<Long, Customer> custStores = new HashMap<Long, Customer>();
	
	@PostConstruct
    public void initIt() throws Exception {
        custStores.put(Long.valueOf(1), new Customer(1, "test", "tester1", 20));
        custStores.put(Long.valueOf(2), new Customer(2, "test", "tester2", 24));
        custStores.put(Long.valueOf(3), new Customer(3, "test", "tester3", 28));
        custStores.put(Long.valueOf(4), new Customer(4, "test", "tester4", 32));
    }

	@Override
	public Mono<Customer> getCustomerById(Long id) {
		return Mono.just(custStores.get(id));
	}

	@Override
	public Flux<Customer> getAllCustomers() {
		return Flux.fromIterable(this.custStores.values());
	}

	@Override
	public Mono<Void> saveCustomer(Mono<Customer> monoCustomer) {
		Mono<Customer> customerMono =  monoCustomer.doOnNext(customer -> {
            // do post
            custStores.put(customer.getCustId(), customer);
            
            // log on console
            System.out.println("POST:" + customer);
        });
		
		return customerMono.then();
	}
	
	@Override
	public Mono<Customer> putCustomer(Long id, Mono<Customer> monoCustomer) {
		Mono<Customer> customerMono =  monoCustomer.doOnNext(customer -> {
			// reset customer.Id
			customer.setCustId(id);
			
			// do put
			custStores.put(id, customer);
			
			// log on console
			System.out.println("PUT:" + customer);
        });
		
		return customerMono;
	}
	
	@Override
	public Mono<String> deleteCustomer(Long id) {
		// delete processing
    	custStores.remove(id);
    	return Mono.just("{ \"Status\": \"Delete Succesfully!\"}");
	}
}
