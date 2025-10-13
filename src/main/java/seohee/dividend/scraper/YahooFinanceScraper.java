package seohee.dividend.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import seohee.dividend.model.Company;
import seohee.dividend.model.Dividend;
import seohee.dividend.model.ScrapedResult;
import seohee.dividend.model.constant.Month;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class YahooFinanceScraper implements Scraper {

    @Value("${chrome.driver.path}")
    String chromeDriverPath;

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s";

    private static final long START_TIME = 86400;   // 60 * 60 * 24

    @Override
    public ScrapedResult scrap(Company company) {
        var scrapedResult = new ScrapedResult();
        scrapedResult.setCompany(company);

        // 스크래핑
        WebDriver driver = createDriver();

        try {
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            // 모든 table 가져오기
            List<WebElement> tables = wait.until(d -> d.findElements(By.tagName("table")));
            // 첫 번째 table만 선택
            WebElement table = tables.get(0);
            // tbody 안 tr 모두 가져오기
            List<WebElement> trs = table.findElements(By.cssSelector("tbody tr"));
            // 데이터 출력
            List<Dividend> dividends = new ArrayList<>();
            for (WebElement tr : trs) {
                String txt = tr.getText();  // tr 밑의 모든 txt 나옴
                if(!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                int year = Integer.parseInt(splits[2]);
                String dividend = splits[3];

                if(month < 0) {
                    throw new RuntimeException("Unexpected Month enum value -> " + splits[0]);
                }

                dividends.add(
                        Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());
            }

            scrapedResult.setDividendEntities(dividends);
        } finally {
            driver.quit();
        }
        return scrapedResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        WebDriver driver = createDriver();
        try {
            String url = String.format(SUMMARY_URL, ticker);
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement titleEle = wait.until(d -> d.findElements(By.tagName("h1")).get(1));

            String title = titleEle.getText().split("\\(")[0].trim();

            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } finally {
            driver.quit();
        }
    }

    private WebDriver createDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

}
