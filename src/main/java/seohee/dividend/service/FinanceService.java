package seohee.dividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import seohee.dividend.model.Company;
import seohee.dividend.model.Dividend;
import seohee.dividend.model.ScrapedResult;
import seohee.dividend.model.constants.CacheKey;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.persist.entity.DividendEntity;
import seohee.dividend.persist.repository.CompanyRepository;
import seohee.dividend.persist.repository.DividendRepository;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {
        log.info("search company -> {}", companyName);
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity companyEntity = companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다."));

        // 2. 조회된 회사 ID 로 배당금을 조회한다
        List<DividendEntity> dividendEntities =
                dividendRepository.findAllByCompanyId(companyEntity.getId());

        // 3. 결과 조합 후 반환
        Company company = Company.builder()
                                .ticker(companyEntity.getTicker())
                                .name(companyEntity.getName())
                                .build();

        List<Dividend> dividends = dividendEntities.stream()
                                    .map(e ->
                                            Dividend.builder()
                                                    .date(e.getDate())
                                                    .dividend(e.getDividend())
                                                    .build())
                                    .toList();

        return new ScrapedResult(company, dividends);
    }

}
