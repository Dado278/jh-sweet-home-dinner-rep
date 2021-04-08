package it.davide.sweethome.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import it.davide.sweethome.domain.Customer;
import it.davide.sweethome.repository.CustomerRepository;
import it.davide.sweethome.repository.search.CustomerSearchRepository;
import it.davide.sweethome.service.CustomerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Optional<Customer> partialUpdate(Customer customer) {
        log.debug("Request to partially update Customer : {}", customer);

        return customerRepository
            .findById(customer.getId())
            .map(
                existingCustomer -> {
                    if (customer.getNickname() != null) {
                        existingCustomer.setNickname(customer.getNickname());
                    }
                    if (customer.getFreshman() != null) {
                        existingCustomer.setFreshman(customer.getFreshman());
                    }
                    if (customer.getEmail() != null) {
                        existingCustomer.setEmail(customer.getEmail());
                    }
                    if (customer.getPhoneNumber() != null) {
                        existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                    }
                    if (customer.getGender() != null) {
                        existingCustomer.setGender(customer.getGender());
                    }
                    if (customer.getCreateDate() != null) {
                        existingCustomer.setCreateDate(customer.getCreateDate());
                    }
                    if (customer.getUpdateDate() != null) {
                        existingCustomer.setUpdateDate(customer.getUpdateDate());
                    }

                    return existingCustomer;
                }
            )
            .map(customerRepository::save)
            .map(
                savedCustomer -> {
                    customerSearchRepository.save(savedCustomer);

                    return savedCustomer;
                }
            );
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
        return customerSearchRepository.search(queryStringQuery(query), pageable);
    }
}
