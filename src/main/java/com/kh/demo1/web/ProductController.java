package com.kh.demo1.web;

import com.kh.demo1.domain.dao.Product;
import com.kh.demo1.domain.svc.ProductSVC;
import com.kh.demo1.web.form.UpdateForm;
import com.kh.demo1.web.form.product.AllForm;
import com.kh.demo1.web.form.product.DetailForm;
import com.kh.demo1.web.form.product.SaveForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/products")    // http://localhost:9080/products(상품관리)
@RequiredArgsConstructor  // 생성자 자동 생성
public class ProductController {

  private  final ProductSVC productSVC;

  // 등록 양식
  @GetMapping("/add")           // http://localhost:9080/products/add
  public String addForm(Model model){
    log.info("addForm 호출됨");

    model.addAttribute("saveForm", new SaveForm());
    return "product/add";
  }

  // 등록 처리
  @PostMapping("/add")
  public String add(
      @Valid @ModelAttribute SaveForm saveForm,
      BindingResult bindingResult,  // 검증 결과를 담는 객체
      RedirectAttributes redirectAttributes
  ){
    log.info("add 호출됨 ={}", saveForm);

    // 1. 유효성 체크 :어노테이션 기반 필드검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/add";
    }

    // 2. 코드 기반 필드 및 글로벌 오류(필드2개이상) 검증
    // 2-1. 상품단가 최소 1000원 이상
    if(saveForm.getPrice() < 1000) {
      bindingResult.rejectValue("price","product","상품가격은 1,000원이 최소입니다.");
    }
    // 총액(상품수량 * 단가) 1억원 초과 금지
    if (saveForm.getQuantity() * saveForm.getPrice() > 100_000_000L) {
      bindingResult.reject("product",null,"수량 * 가격은 1억원을 초과할 수 없습니다.");
    }

    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/add";
    }

    // 상품등록
    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());
    Long productId = productSVC.save(product);

    log.info("상품아이디={}", productId);
    redirectAttributes.addAttribute("id", productId);

        return "redirect:/products/{id}/detail";    // 302 GET http://localhost:9080/products/1/detail
  }

  // 조회
  @GetMapping("/{id}/detail")   //GET http://localhost:9080/products/1/detail
  public String findById(
      @PathVariable("id") Long id,
      Model model){
    // 상품조회
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setProductId(product.getProductId());
    detailForm.setPname(product.getPname());
    detailForm.setQuantity(product.getQuantity());
    detailForm.setPrice(product.getPrice());

    model.addAttribute("detailForm", detailForm);
    return "product/detailForm";
  }

  // 목록
  @GetMapping   //GET http://localhost:9080/products
  public String findAll(Model model){
    log.info("findAll() 호출됨");

    // 상품 목록 조회
    List<Product> list = productSVC.findAll();
    List<AllForm> all = new ArrayList<>();
    for(Product product :list){
      AllForm allForm = new AllForm();
      allForm.setProductId(product.getProductId());
      allForm.setPname(product.getPname());
      all.add(allForm);
    }
    model.addAttribute("all",all);
    return "product/all";
  }

  //삭제
//  @GetMapping("/{id}/del")      // DELETE http://localhost:9080/products/1
  @DeleteMapping("/{id}")
  public String deleteById(@PathVariable("id") Long id){

    int deletedRowCnt = productSVC.deleteById(id);

    return "redirect:/products";        // 302 get  redirectUrl : http://localhost:9080/products
  }

  //수정양식
  @GetMapping("/{id}")        // GET http://localhost:9080/products/1
  public String updateForm(
      @PathVariable("id") Long id,
      Model model){
    log.info("updateForm()호출됨!");
    Optional<Product> findedProduct = productSVC.findById(id);

    Product product = findedProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setProductId(product.getProductId());
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());

    model.addAttribute("updateForm", updateForm);
    return "product/updateForm";
  }

  //수정처리
  @PatchMapping("/{id}")
  public String update(
      @PathVariable("id") Long productId,
      @Valid @ModelAttribute UpdateForm updateForm,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes){
    log.info("update()호출됨!");
    log.info("updateForm={}",updateForm);

// 요청데이터 유효성 체크
    // 1. 어노테이션 기반 필드 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }

    // 2. 필드오류 , 상품 가격 최소 1000원 이상
    if(updateForm.getPrice() < 1_000) {
      bindingResult.rejectValue("price","product",new Object[]{1000},null);
    }

    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }

    //상품수정

    Product product = new Product();
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());
    int updatedRow = productSVC.updateById(productId, product);

    //상품조회 리다이렉트
    redirectAttributes.addAttribute("id",productId);
    return "redirect:/products/{id}/detail";   // 302 get http://localhost:9080/products/1/detail
  }

}
