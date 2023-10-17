package com.kh.demo1.web.form;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateForm {
  private Long productId;
  @NotBlank
  private String pname;
  @NotNull
  @Positive
  private Long quantity;
  @NotNull
  @Positive
  @Min(1_000)
  private Long price;
}
