package seohee.dividend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import seohee.dividend.model.Company;
import seohee.dividend.model.ScrapedResult;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.persist.entity.DividendEntity;
import seohee.dividend.persist.repository.CompanyRepository;
import seohee.dividend.persist.repository.DividendRepository;
import seohee.dividend.scraper.Scraper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public Company save(String ticker) {
        boolean exists = companyRepository.existsByTicker(ticker);
        if(exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return companyRepository.findAll(pageable);
    }

    private Company storeCompanyAndDividend(String ticker) {
        // ticker 를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);

        if(ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " +  ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult  = yahooFinanceScraper.scrap(company);

        // 스크래핑 결과 저장
        CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities =
                scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);
        return company;
    }

}
