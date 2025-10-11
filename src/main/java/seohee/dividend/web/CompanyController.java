package seohee.dividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
    /**
     * 회사이름을 검색할 때 자동완성을 해주는 API
     */
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    /**
     * 회사 리스트를 조회하는 API
     */
    @GetMapping()
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    /**
     * 회사 데이터 저장
     * @return
     */
    @PostMapping()
    public ResponseEntity<?> addCompany() {
        return null;
    }

    /**
     * 회사 데이터 삭제
     * @return
     */
    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
