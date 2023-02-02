package buscompany.validator;

import buscompany.utils.AppPropertiesUtils;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class CorrectNameValidator implements ConstraintValidator<CorrectName, String> {

    private AppPropertiesUtils appPropertiesUtils;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null){
            return true;
        }
        return value.length() <= appPropertiesUtils.getMaxNameLength() && !value.isBlank();
    }
}
