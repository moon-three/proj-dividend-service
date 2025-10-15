package seohee.dividend.model;

import lombok.Getter;
import lombok.Setter;
import seohee.dividend.persist.entity.MemberEntity;

import java.util.List;

public class Auth {

    // 로그인 시
    @Getter
    @Setter
    public static class SignIn {
        private String username;
        private String password;
    }

    // 회원가입 시
    @Getter
    @Setter
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                        .username(username)
                        .password(password)
                        .roles(roles)
                        .build();
        }
    }
}
