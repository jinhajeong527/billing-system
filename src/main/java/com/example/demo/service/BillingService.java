package com.example.demo.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BillingPayload;
import com.example.demo.dto.MeteringInfo;
import com.example.demo.dto.OID;
import com.example.demo.entity.Product;
import com.example.demo.repository.PriceHistoryRepository;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BillingService {
    private static final Logger LOG =   LoggerFactory.getLogger(BillingService.class);
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PriceHistoryRepository priceHistoryRepository;

    public void getBillingInfo(int productId, int targetYear, int targetMonth) throws StreamReadException, DatabindException, MalformedURLException, IOException {
        // 프로젝트의 사용 내역 담은 Json 데이터 파싱한다.
        LOG.info("Started Parsing meteringList.json for {} / {}", targetYear, targetMonth);
        BillingPayload billingPayload = parseMeteringList();
        List<MeteringInfo> meteringInfos = billingPayload.getMeteringList();

        // 청구액 확인 Month의 총 Hour를 구한다.
        int totalHour = getHoursOfTheMonth(targetYear, targetMonth);
        LOG.info("Full Fee Will Be Charged If {} Hours Used Per Core", totalHour);
        // Product Price 정보를 통해 시간 당 Core 한 개의 사용 비용을 구한다.
        double hourlyFeePerCore = getHourlyFeePerCore(totalHour, productId);
        LOG.info("{} KRW Will Be Charged Per Hour For Each Core Usage", hourlyFeePerCore);
        
        /* 
        과금 유형이 CORE라고 하고, 한 개의 코어당 7월에 full로 사용했을 때 (24시간 * 31일) 사용료가 25,000KRW 라고 한다면 
        7월 1일 오전 1시에 발생한 사용량의 측정을 위해서는 totalCore가 몇 개 쓰였는지를 확인하고, 총 4개에 같은 IP에 대해서 발생했다면
        과금액은 4 * (25000 / (24*31)) 이 되겠지만, 만약 다른 IP가 있다? 그럼 사용한 것으로 간주하기 때문에 만약 이 시간에 2개의 IP가 발견이 되었다면
        2 * 4 * (25000 / (24*31)) 와 같이 계산하여 7월 1일 오전 1시에 발생한 요금에 대해서 계산이 될 것이다.
        */

        int calculatedTotalHourPerCore = 0;
        double totalFee = 0.0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        for(MeteringInfo meteringInfo : meteringInfos) {

            LOG.info("Metering Begins. Total Fee Starts From {} KRW", totalFee);
            // 과금 일시 로깅하기 위한 date 포맷팅.
            String formatDate = simpleDateFormat.format(meteringInfo.getDatetime());
            // oidsJson 정보 리스트<OID> 객체로 파싱한다.
            List<OID> oids = parseOidsList(meteringInfo.getOidsJson());
           
            // 과금 대상의 최소 단위인 IP가 해당 시간에 몇 개의 코어를 사용했는지를 확인하기 위해 HashMap을 선언했다.
            Map<String, Integer> ipAndCoreMap = new HashMap<>();

            for(OID oid : oids) {
                // 이미 존재하는 IP라면 코어수를 비교해서 더 많은 코어수를 사용한 경우 값을 새로 set 해준다.
                if(ipAndCoreMap.containsKey(oid.getIp())) {
                    int core = ipAndCoreMap.get(oid.getIp()) > oid.getCore() ? ipAndCoreMap.get(oid.getIp()) : oid.getCore();
                    ipAndCoreMap.put(oid.getIp(), core);
                } else {
                    ipAndCoreMap.put(oid.getIp(), oid.getCore());
                }
            }

            // 해당 시간동안 모니터링 제품 사용한 ip와 core수를 담아주었으면 계산을 시작한다
            Iterator<String> keySetIterator = ipAndCoreMap.keySet().iterator();
            while(keySetIterator.hasNext()) {
                // 해당 ip에서 최대 사용 했던 CORE의 수
                String ip = keySetIterator.next();
                int coreCount = ipAndCoreMap.get(ip);
                LOG.info("At {}, {} Uses {} Core(s). Hourly Fee Per Core Is {}", formatDate, ip, coreCount, hourlyFeePerCore);
                // 코어 한 개당 한시간 사용 비용에 사용된 코어수를 곱해준다.
                totalFee += coreCount * hourlyFeePerCore;
                calculatedTotalHourPerCore++;
                LOG.info("Fee For {} With IP {} Using {} Core(s) Is {} KRW", formatDate, ip, coreCount, coreCount * hourlyFeePerCore);
                LOG.info("Current Total Fee Is {} KRW", totalFee);
            }
        }
        LOG.info("Total Fee For Target Month Is: {} KRW", totalFee);
        LOG.info("Total Uesd Hour For Target Month Is: {} hour", calculatedTotalHourPerCore);
    }

    // 제품 비용에서 해당 월의 전체 시간으로 나눠준다.
    private double getHourlyFeePerCore(int totalHour, int productId) {
        Product product = productRepository.findById(productId).get();
        // 상품의 가장 최근 가격 가져온다.
        BigDecimal productPrice = priceHistoryRepository.findFirstByProductOrderByCreateDateDesc(product).getPrice();
        BigDecimal total = new BigDecimal(totalHour);
        BigDecimal devided = productPrice.divide(total, 2, RoundingMode.HALF_EVEN);
        return devided.doubleValue();
    }
    // targetMonth의 전체 시간을 구한다.
    private int getHoursOfTheMonth(int targetYear, int targetMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(targetYear, targetMonth - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH) * 24;
    }

    private List<OID> parseOidsList(String oidsJson) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<List<OID>> typeRef = new TypeReference<List<OID>>(){};
        List<OID> list = objectMapper.readValue(oidsJson, typeRef);
        return list;
    }

    private BillingPayload parseMeteringList() throws StreamReadException, DatabindException, MalformedURLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<BillingPayload> typeRef = new TypeReference<BillingPayload>(){};
        BillingPayload billingPayload = objectMapper.readValue(new URL("file:src/main/resources/meteringList.json"), typeRef);
        return billingPayload;
    }

}
