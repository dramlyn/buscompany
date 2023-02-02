package buscompany.validator;

import buscompany.utils.AppPropertiesUtils;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class CorrectPasswordValidator implements ConstraintValidator<CorrectPassword, String> {

    private AppPropertiesUtils appPropertiesUtils;



    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return false;
        }
        return value.length() >= appPropertiesUtils.getMinPasswordLength();
    }
}
