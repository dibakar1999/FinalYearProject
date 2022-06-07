package com.FYP.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.FYP.Entities.Orders;
import com.FYP.Repository.OrderRepository;

@Service
public class OrderServices {
	@Autowired
	OrderRepository orderRepository;
	
	public void saveOrder(Orders orders) {
		orderRepository.save(orders);	
	}
	public Page<Orders> getAllorder(Pageable pageable) {
		return orderRepository.findAll(pageable);
	}
}
