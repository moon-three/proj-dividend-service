package seohee.dividend.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import seohee.dividend.model.Company;
import seohee.dividend.model.Dividend;
import seohee.dividend.model.ScrapedResult;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.persist.entity.DividendEntity;
import seohee.dividend.persist.repository.CompanyRepository;
import seohee.dividend.persist.repository.DividendRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {
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
