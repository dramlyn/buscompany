package buscompany.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CorrectPasswordValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CorrectPassword {
    String message() default "You entered wrong password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
