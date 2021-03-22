package it.davide.sweethome.service.impl;

import it.davide.sweethome.service.CustomerService;
import it.davide.sweethome.domain.Customer;
import it.davide.sweethome.repository.CustomerRepository;
import it.davide.sweethome.repository.search.CustomerSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    private final CustomerSearchRepository customerSearchRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerSearchRepository customerSearchRepository) {
        this.customerRepository = customerRepository;
        this.customerSearchRepository = customerSearchRepository;
    }

    @Override
    public Customer save(Customer customer) {
        log.debug("Request to save Customer : {}", customer);
        Customer result = customerRepository.save(customer);
        customerSearchRepository.save(result);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> findAll(Pageable pageable) {
        log.debug("Request to get all Customers");
        return customerRepository.findAll(pageable);
    }


    public Page<Customer> findAllWithEagerRelationships(Pageable pageable) {
        return customerRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
        customerSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Customer> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Customers for query {}", query);
        return customerSearchRepository.search(queryStringQuery(query), pageable);    }
}
