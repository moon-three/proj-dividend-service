package seohee.dividend.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import seohee.dividend.model.Company;
import seohee.dividend.model.ScrapedResult;
import seohee.dividend.persist.entity.CompanyEntity;
import seohee.dividend.persist.entity.DividendEntity;
import seohee.dividend.persist.repository.CompanyRepository;
import seohee.dividend.persist.repository.DividendRepository;
import seohee.dividend.scraper.YahooFinanceScraper;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final YahooFinanceScraper yahooFinanceScraper;

    // 일정 주기마다 수행
    @Scheduled(cron = "0 0 0 1 * *")
    public void yahooFinanceScheduling() {
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companyEntities = companyRepository.findAll();

        // 회사를 순회하며 배당금 정보를 새로 스크래핑
        for(var companyEntity : companyEntities) {
            log.info("scraping schedular is started -> {}", companyEntity.getName());
            ScrapedResult scrapedResult = yahooFinanceScraper.scrap(Company.builder()
                                            .name(companyEntity.getName())
                                            .ticker(companyEntity.getTicker())
                                            .build());

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    // dividend 모델을 dividendEntity로 매핑
                    .map(e -> new DividendEntity(companyEntity.getId(), e))
                    // element를 하나씩 dividend 테이블에 삽입
                    .forEach(e -> {
                        boolean exists = dividendRepository.existByCompanyIdAndDate(
                                e.getCompanyId(), e.getDate());

                        if(!exists) {
                            dividendRepository.save(e);
                        }
                    });

            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

}
