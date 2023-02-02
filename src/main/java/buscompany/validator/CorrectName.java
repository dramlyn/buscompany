package buscompany.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectName {
    String message() default "You entered wrong name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
