package seohee.dividend.web;

import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import seohee.dividend.model.Company;
import seohee.dividend.model.constants.CacheKey;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManger;

     // 회사이름을 검색할 때 자동완성을 해주는 API
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = companyService.autocomplete(keyword);
//        var result = companyService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    // 회사 리스트를 조회하는 API
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    // 회사 데이터 저장
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")   // 쓰기 권한이 있는 사람만 접근
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
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = companyService.deleteCompany(ticker);

        // 캐시에서 회사 데이터 삭제
        clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
        Cache cache = redisCacheManger.getCache(CacheKey.KEY_FINANCE);
        if (cache != null) { cache.evict(companyName); }
    }

}
