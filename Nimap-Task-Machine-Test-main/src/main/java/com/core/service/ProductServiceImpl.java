package com.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.core.dao.ProductDao;
import com.core.dao.CategoryDao;
import com.core.entity.Product;
import com.core.entity.Category;
import com.core.exception.ResourceNotFoundException;

@Service
public class ProductServiceImpl implements ProductService {
@Autowired
    private final ProductDao productDao;
    private final CategoryDao categoryDao;

    public ProductServiceImpl(ProductDao productDao, CategoryDao categoryDao) {
        this.productDao = productDao;
        this.categoryDao = categoryDao;
    }

    @Override
    public Product createProduct(Product product) {
        long categoryId = product.getCategory().getcId();

        // Validate category existence
        Category existingCategory = categoryDao.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        product.setCategory(existingCategory);
        return productDao.save(product);
    }

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productDao.findAll(pageable);
    }

    @Override
    public Product getProductById(long id) throws ResourceNotFoundException {
        return productDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    @Override
    public Product updateProduct(long id, Product productDetails) throws ResourceNotFoundException {
        Product existingProduct = productDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Update product attributes
        existingProduct.setpName(productDetails.getpName());
        existingProduct.setPrice(productDetails.getPrice());

        // Handle category update
        if (productDetails.getCategory() != null && productDetails.getCategory().getcId() != null) {
            long categoryId = productDetails.getCategory().getcId();
            Category category = categoryDao.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
            existingProduct.setCategory(category);
        }

        return productDao.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) throws ResourceNotFoundException {
        Product product = productDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));

        productDao.delete(product);
    }
}
