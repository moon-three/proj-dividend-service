package seohee.dividend.exception.impl;

import org.springframework.http.HttpStatus;
import seohee.dividend.exception.AbstractException;

public class NotExistUserException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 사용자명입니다.";
    }
}
