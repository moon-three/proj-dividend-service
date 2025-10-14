package seohee.dividend.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import seohee.dividend.model.Company;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

     // 회사이름을 검색할 때 자동완성을 해주는 API
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = companyService.autocomplete(keyword);
//        var result = companyService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    // 회사 리스트를 조회하는 API
    @GetMapping()
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    // 회사 데이터 저장
    @PostMapping()
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = companyService.save(ticker);
        companyService.addAutocompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    // 회사 데이터 삭제
    @DeleteMapping("/company")
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
