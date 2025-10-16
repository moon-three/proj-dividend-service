package seohee.dividend.exception.impl;

import org.springframework.http.HttpStatus;
import seohee.dividend.exception.AbstractException;

public class WrongPasswordException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "비밀번호가 일치하지 않습니다.";
    }
}
