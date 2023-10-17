package com.kh.demo1.web.form.product;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SaveForm {
  @NotBlank
  private String pname;

  @NotNull
  @Positive
  private Long quantity;

  @NotNull
  @Positive
  @Min(value=1_000)
  private Long price;


}
