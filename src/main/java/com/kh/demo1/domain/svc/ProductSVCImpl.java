package com.kh.demo1.domain.svc;

import com.kh.demo1.domain.dao.Product;
import com.kh.demo1.domain.dao.ProductDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSVCImpl implements ProductSVC{

  // ProductDAO 인터페이스를 구현한 객체를 주입받는 필드
  private  final ProductDAO productDAO;

  @Override
  public Long save(Product product){
    // ProductDAO를 통해 상품 정보를 데이터베이스에 저장하고 생성된 상품 아이디를 반환합니다.
//    Long productId = productDAO.save(product);
//    return productId;
    return productDAO.save(product);
  }

  @Override
  public Optional<Product> findById(Long productId) {
//    Optional<Product> product = productDAO.findById(productId);
//    return product;
    return productDAO.findById(productId);
  }

  @Override
  public List<Product> findAll() {
    return productDAO.findAll();
  }

  @Override
  public int deleteById(Long productId) {
    return productDAO.deleteById(productId);
  }

  @Override
  public int updateById(Long productId, Product product) {
    return productDAO.updateById(productId,product);
  }
}
