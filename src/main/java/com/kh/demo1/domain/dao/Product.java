package com.kh.demo1.domain.dao;

import lombok.Data;

@Data
public class Product {
  private Long productId; // 상품 아이디
  private String pname;   // 상품명
  private Long quantity;  // 상품수량
  private Long price;     // 상품가격
}
